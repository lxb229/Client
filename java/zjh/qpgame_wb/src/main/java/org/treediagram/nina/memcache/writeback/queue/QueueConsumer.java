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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.treediagram.nina.memcache.orm.Accessor;
import org.treediagram.nina.memcache.writeback.Element;
import org.treediagram.nina.memcache.writeback.EventListener;
import org.treediagram.nina.stat.StatFactory;
import org.treediagram.nina.stat.model.Stat;

/**
 * 队列消费者
 * 
 * @author kidalsama
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class QueueConsumer implements Runnable {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(QueueConsumer.class);

	/**
	 * 队列消费个数统计
	 */
	private static final Stat consumeCountStat = StatFactory.getStat(Stat.class, "count-QueueWriter", "consumeCount");

	/**
	 * 错误个数统计
	 */
	private static final Stat errorCountStat = StatFactory.getStat(Stat.class, "count-QueueWriter", "errorCount");

	/**
	 * 更新队列名
	 */
	private final String name;

	/**
	 * 更新队列
	 */
	private final BlockingQueue<Element> queue;

	/**
	 * 持久层的存储器
	 */
	private final Accessor accessor;

	/**
	 * 所有者
	 */
	private final QueueWriter owner;

	/**
	 * 当前消费者线程自身
	 */
	private final Thread me;

	/**
	 * 错误计数器
	 */
	private final AtomicInteger error = new AtomicInteger();

	/**
	 * 暂停状态
	 */
	private volatile boolean pause;

	/**
	 * 创建队列消费者
	 */
	public QueueConsumer(String name, BlockingQueue<Element> queue, Accessor accessor, QueueWriter owner) {
		this.name = name;
		this.queue = queue;
		this.accessor = accessor;
		this.owner = owner;

		this.me = new Thread(this, "回写器[" + name + ":队列]");
		this.me.setDaemon(true);
		this.me.start();
	}

	/**
	 * 线程执行
	 */
	@Override
	public void run() {
		for (;;) {
			for (; pause;) {
				Thread.yield();
			}

			Element element = null;
			Class entityClass = null;

			try {
				element = queue.take();
				entityClass = element.getEntityClass();

				switch (element.getType()) {
				case SAVE:
					accessor.save(entityClass, element.getEntity());
					break;

				case REMOVE:
					accessor.remove(entityClass, element.getId());
					break;

				case UPDATE:
					owner.removeUpdating(element.getIdentity());
					accessor.update(entityClass, element.getEntity());
					break;

				default:
					logger.error("未支持的更新队列元素类型[{}]", element);
					break;
				}

				consumeCountStat.increment();

				EventListener listener = owner.getEventListener(entityClass);

				if (listener != null) {
					listener.onNotify(element.getType(), true, element.getId(), element.getEntity(), null);
				}
			} catch (RuntimeException e) {
				error.incrementAndGet();

				String message = MessageFormatter.arrayFormat("实体更新队列[{}]处理元素[{}]时出现异常",
						new Object[] { name, element, e }).getMessage();
				logger.error(message, e);

				if (element != null) {
					EventListener listener = owner.getEventListener(entityClass);

					if (listener != null) {
						listener.onNotify(element.getType(), false, element.getId(), element.getEntity(), e);
					}
				}
			} catch (Exception e) {
				error.incrementAndGet();
				errorCountStat.set(error.get());

				if (element == null) {
					logger.error("获取更新队列元素时线程被非法打断", e);
				} else {
					logger.error("更新队列处理出现未知异常", e);
				}
			}
		}
	}

	/**
	 * @return 错误次数
	 */
	public int getError() {
		return error.get();
	}
}
