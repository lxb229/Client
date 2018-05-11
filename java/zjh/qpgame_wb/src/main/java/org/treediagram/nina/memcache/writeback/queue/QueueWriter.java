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

package org.treediagram.nina.memcache.writeback.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.Assert;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.exception.MemcacheConfigurationException;
import org.treediagram.nina.memcache.exception.MemcacheStateException;
import org.treediagram.nina.memcache.orm.Accessor;
import org.treediagram.nina.memcache.writeback.Element;
import org.treediagram.nina.memcache.writeback.EventListener;
import org.treediagram.nina.memcache.writeback.EventType;
import org.treediagram.nina.memcache.writeback.Writer;
import org.treediagram.nina.stat.StatFactory;
import org.treediagram.nina.stat.model.Stat;

/**
 * 队列写入器
 * 
 * @author kidalsama
 * 
 */
@SuppressWarnings("rawtypes")
public class QueueWriter implements Writer {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(QueueWriter.class);

	/**
	 * 队列大小统计
	 */
	private static final Stat queueSizeStat = StatFactory.getStat(Stat.class, "count-QueueWriter", "queueSize");

	/**
	 * 放入队列数量统计
	 */
	private static final Stat putCountStat = StatFactory.getStat(Stat.class, "count-QueueWriter", "putCount");

	/**
	 * 分隔表达式
	 */
	private static final String REGEX = ":";

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 更新队列
	 */
	private BlockingQueue<Element> queue;

	/**
	 * 侦听器
	 */
	private ConcurrentMap<Class<? extends IEntity>, EventListener> listeners = new ConcurrentHashMap<Class<? extends IEntity>, EventListener>();

	/**
	 * 初始化标识
	 */
	private boolean initialized;

	/**
	 * 抑制重复更新状态
	 */
	private boolean flag;

	/**
	 * 正在等待更新的信息缓存
	 */
	private ConcurrentMap<String, Object> updating = new ConcurrentHashMap<String, Object>();

	/**
	 * 消费线程
	 */
	private QueueConsumer consumer;

	/**
	 * 停止状态
	 */
	private volatile boolean stop = false;

	/**
	 * 创建队列写入器
	 */
	public QueueWriter() {

	}

	/**
	 * 移除更新抑制
	 */
	public void removeUpdating(String identity) {
		if (flag) {
			updating.remove(identity);
		}
	}

	/**
	 * @return 队列元素个数
	 */
	public int size() {
		return queue.size();
	}

	@Override
	public void initialize(String name, Accessor accessor, String configuration) {
		if (initialized) {
			throw new MemcacheConfigurationException("重复初始化:" + getClass().getName());
		}

		Assert.notNull(accessor);

		try {
			String[] array = configuration.split(REGEX);
			int capacity = Integer.parseInt(array[0]);

			if (capacity >= 0) {
				this.queue = new ArrayBlockingQueue<Element>(capacity);
			} else {
				this.queue = new LinkedBlockingQueue<Element>();
			}

			this.flag = Boolean.parseBoolean(array[1]);
			this.name = name;
			this.consumer = new QueueConsumer(name, this.queue, accessor, this);
			this.initialized = true;
		} catch (Exception e) {
			throw new MemcacheConfigurationException("回写器[" + name + "]初始化异常", e);
		}
	}

	@Override
	public void put(Element element) {
		if (element == null) {
			return;
		}

		if (stop) {
			FormattingTuple message = MessageFormatter.format("实体更新队列[{}]已经停止,更新元素[{}]将不被接受", name, element);
			logger.error(message.getMessage());
			throw new MemcacheStateException(message.getMessage());
		}

		try {
			if (flag && element.getType().equals(EventType.UPDATE)) {
				String identity = element.getIdentity();

				if (updating.containsKey(identity)) {
					return;
				}

				updating.put(identity, Boolean.TRUE);
			}

			queue.put(element);
			queueSizeStat.set(queue.size());
			putCountStat.increment();
		} catch (InterruptedException e) {
			// 这种情况是不应该会出现的
			logger.error("等待将元素[{}]添加到队列时被打断", new Object[] { element, e });

			if (element.getType().equals(EventType.UPDATE)) {
				updating.remove(element.getIdentity());
			}
		}
	}

	@Override
	public void addEventListener(Class<? extends IEntity> entityClass, EventListener listener) {
		if (listener == null) {
			throw new MemcacheConfigurationException("事件侦听器为 null");
		} else {
			listeners.put(entityClass, listener);
		}
	}

	@Override
	public EventListener getEventListener(Class<? extends IEntity> entityClass) {
		return listeners.get(entityClass);
	}

	@Override
	public void stopAndWriteback() {
		if (logger.isDebugEnabled()) {
			logger.debug("停机回写队列[{}]开始...", name);
		}

		stop = true;

		for (;;) {
			if (queue.isEmpty()) {
				break;
			}

			Thread.yield();
		}

		logger.error("停机回写队列[{}]完成...", name);
	}

	@Override
	public Map<String, String> getInformation() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("size", Integer.toString(size()));
		result.put("error", Integer.toString(consumer.getError()));
		return result;
	}
}
