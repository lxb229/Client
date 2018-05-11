/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treediagram.nina.memcache.writeback.timing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.Assert;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.exception.MemcacheConfigurationException;
import org.treediagram.nina.memcache.exception.MemcacheStateException;
import org.treediagram.nina.memcache.orm.Accessor;
import org.treediagram.nina.memcache.writeback.Element;
import org.treediagram.nina.memcache.writeback.EventListener;
import org.treediagram.nina.memcache.writeback.EventType;
import org.treediagram.nina.memcache.writeback.Writer;

/**
 * 定时回写器
 * 
 * @author kidalsama
 * 
 */
@SuppressWarnings("rawtypes")
public class TimingWriter implements Writer {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(TimingWriter.class);

	/**
	 * 正在等待更新的信息缓存
	 */
	private ConcurrentHashMap<String, Element> elements = new ConcurrentHashMap<String, Element>();

	/**
	 * 对应实体的处理监听器
	 */
	private ConcurrentHashMap<Class<? extends IEntity>, EventListener> listeners = new ConcurrentHashMap<Class<? extends IEntity>, EventListener>();

	/**
	 * 初始化标识
	 */
	private boolean initialized;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 消费线程
	 */
	private TimingConsumer consumer;

	// 实现接口的方法

	@Override
	public synchronized void initialize(String name, Accessor accessor, String cron) {
		if (initialized) {
			throw new MemcacheConfigurationException("重复初始化异常");
		}

		Assert.notNull(accessor, "持久层数据访问器不能为 null");

		try {
			DateUtils.getNextTime(cron, new Date());
			this.elements = new ConcurrentHashMap<String, Element>();
			this.consumer = new TimingConsumer(name, cron, accessor, this);
			this.name = name;
			initialized = true;
		} catch (Exception e) {
			throw new MemcacheConfigurationException("定时持久化处理器[" + name + "]初始化失败:" + e.getMessage());
		}
	}

	@Override
	public void addEventListener(Class<? extends IEntity> clz, EventListener listener) {
		if (listener == null) {
			throw new MemcacheConfigurationException("被添加的监听器实例不能为空");
		} else {
			listeners.put(clz, listener);
		}
	}

	@Override
	public EventListener getEventListener(Class<? extends IEntity> clz) {
		return listeners.get(clz);
	}

	/**
	 * 将指定元素插入此队列中，将等待可用的空间（如果有必要）。
	 * 
	 * @param element 被添加元素(元素为null时直接返回)
	 */
	@Override
	public void put(Element element) {
		if (element == null) {
			return;
		}
		if (stop) {
			FormattingTuple message = MessageFormatter.format("实体更新队列已经停止,更新元素[{}]将不被接受", element);
			logger.error(message.getMessage());
			throw new MemcacheStateException(message.getMessage());
		}

		String id = element.getIdentity();
		rwLock.readLock().lock();
		Lock lock = lockIdLock(id);
		try {
			Element prev = elements.get(id);

			// 更新元素不存在的场景
			if (prev == null) {
				elements.put(id, element);
				return;
			}

			// 更新元素已经存在的场景
			EventType prevType = prev.getType();
			if (!prev.update(element)) {
				elements.remove(id);
			} else {
				// 当从REMOVE合并为UPDATE的时候要让监听器通知缓存服务将内部的临时失效主键清除
				if (prevType == EventType.REMOVE && prev.getType() == EventType.UPDATE) {
					EventListener listener = getEventListener(element.getEntityClass());
					if (listener != null) {
						listener.onNotify(EventType.REMOVE, true, prev.getId(), null, null);
					}
				}
			}
		} finally {
			releaseIdLock(id, lock);
			rwLock.readLock().unlock();
		}
	}

	@Override
	public Map<String, String> getInformation() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("size", Integer.toString(size()));
		result.put("state", consumer.getState().name());
		result.put("nextTime", DateUtils.date2String(consumer.getNextTime(), DateUtils.PATTERN_DATE_TIME));
		return result;
	}

	// 自身的方法

	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

	Collection<Element> clearElements() {
		rwLock.writeLock().lock();
		try {
			List<Element> result = new ArrayList<Element>(elements.values());
			elements.clear();
			return result;
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	/**
	 * 获取队列中的元素数量
	 */
	public int size() {
		return elements.size();
	}

	/**
	 * 停止状态
	 */
	private volatile boolean stop;

	/**
	 * 停止更新队列并等待全部入库完成
	 */
	@Override
	public void stopAndWriteback() {
		if (logger.isDebugEnabled()) {
			logger.debug("停机回写队列[{}]开始...", name);
		}
		stop = true;
		consumer.stop();
		while (consumer.getState() != TimingConsumerState.STOPPED) {
			Thread.yield();
		}
		logger.error("停机回写队列[{}]完成...", name);
	}

	public void flush() {
		consumer.interrupt();
	}

	public TimingConsumer getConsumer() {
		return consumer;
	}

	// 内部方法

	/**
	 * 队列内更新元素的操作锁
	 */
	private ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<String, ReentrantLock>();

	/**
	 * 获取标识锁对象
	 */
	private Lock lockIdLock(String id) {
		// 获取当前的主键写锁
		ReentrantLock lock = locks.get(id);
		if (lock == null) {
			lock = new ReentrantLock();
			ReentrantLock prevLock = locks.putIfAbsent(id, lock);
			lock = prevLock != null ? prevLock : lock;
		}
		lock.lock();
		return lock;
	}

	/**
	 * 释放标识锁
	 */
	private void releaseIdLock(String id, Lock lock) {
		lock.unlock();
		locks.remove(id);
	}
}
