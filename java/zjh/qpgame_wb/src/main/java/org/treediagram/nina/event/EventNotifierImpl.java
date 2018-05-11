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

package org.treediagram.nina.event;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treediagram.nina.core.thread.NamedThreadFactory;
import org.treediagram.nina.stat.StatFactory;
import org.treediagram.nina.stat.model.NanoTimeStat;
import org.treediagram.nina.stat.model.Stat;

/**
 * 事件通知器
 * 
 * @author kidal
 * 
 */
public class EventNotifierImpl implements EventNotifier, EventNotifierMBean {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(EventNotifierImpl.class);

	/**
	 * 抛出事件统计
	 */
	private static final Stat postCountStat = StatFactory.getStat(Stat.class, "count-EventNotifierImpl", "postCount");

	/**
	 * 分发事件统计
	 */
	private static final Stat dispatchCountStat = StatFactory.getStat(Stat.class, "count-EventNotifierImpl",
			"dispatchCount");

	/**
	 * 消费队列长度统计
	 */
	private static final Stat consumeQueueSizeStat = StatFactory.getStat(Stat.class, "count-EventNotifierImpl",
			"consumeQueueSize");

	/**
	 * 消费个数统计
	 */
	private static final Stat consumeCountStat = StatFactory.getStat(Stat.class, "count-EventNotifierImpl",
			"consumeCount");

	/**
	 * 队列同步消费个数统计
	 */
	private static final Stat syncConsumeCountStat = StatFactory.getStat(Stat.class, "count-EventNotifierImpl",
			"syncConsumeCount");

	/**
	 * 事件队列大小
	 */
	private int queueSize = 10000;

	/**
	 * 执行池大小
	 */
	private int poolSize = 5;

	/**
	 * 执行池最大大小
	 */
	private int poolMaxSize = 10;

	/**
	 * 执行池活动时间
	 */
	private int poolAliveTime = 60;

	/**
	 * 执行池关闭时间
	 */
	private int poolAwaitTime = 60;

	/**
	 * 事件队列
	 */
	private BlockingQueue<EventContext<?>> eventQueue;

	/**
	 * 执行池
	 */
	private ExecutorService pool;

	/**
	 * 停止状态
	 */
	private volatile boolean stop = false;

	/**
	 * 队列满了
	 */
	private volatile boolean poolFull = false;

	/**
	 * 事件接收器
	 */
	private final ConcurrentMap<String, CopyOnWriteArraySet<EventListener<?>>> listeners = new ConcurrentHashMap<String, CopyOnWriteArraySet<EventListener<?>>>();

	/**
	 * 创建事件通知器
	 */
	public EventNotifierImpl() {
	}

	/**
	 * 初始化
	 */
	@PostConstruct
	protected void init() {
		// 创建线程工厂
		ThreadGroup threadGroup = new ThreadGroup("事件通知");
		NamedThreadFactory threadFactory = new NamedThreadFactory(threadGroup, "事件处理");

		// 创建事件队列
		eventQueue = new LinkedBlockingQueue<EventContext<?>>(queueSize);

		// 创建执行池
		pool = new ThreadPoolExecutor(poolSize, poolMaxSize, poolAliveTime, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(queueSize), threadFactory);

		// 启动消费线程
		Thread consumer = new Thread(new ConsumerRunnable(), "事件消费线程");
		consumer.setDaemon(true);
		consumer.start();

		// 注册MBean
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName name = new ObjectName("org.treediagram.nina:type=EventNotifierMBean");
			mbs.registerMBean(this, name);
		} catch (Exception e) {
			logger.error("注册 [{}] 的 JMX 管理接口失败", this, e);
		}
	}

	/**
	 * 销毁
	 */
	@PreDestroy
	public void destory() {

	}

	/**
	 * 关闭
	 */
	public void shutdown() {
		// 防止重复执行
		if (isStop()) {
			return;
		} else {
			stop = true;
		}

		// 等待队列被执行完毕
		for (;;) {
			if (eventQueue.isEmpty()) {
				break;
			}
			Thread.yield();
		}

		// 等待线程池关闭
		pool.shutdown();

		try {
			logger.info("开始关闭事件执行线程池");

			if (!pool.awaitTermination(poolAwaitTime, TimeUnit.SECONDS)) {
				logger.warn("无法在规定时间 {} 内完成事件执行线程池的关闭，强制关闭");

				pool.shutdownNow();

				if (!pool.awaitTermination(poolAwaitTime, TimeUnit.SECONDS)) {
					logger.error("无法关闭事件执行线程池");
				}
			}
		} catch (InterruptedException e) {
			logger.error("事件执行线程池在关闭时被打断，强制关闭", e);
			pool.shutdownNow();
		}
	}

	/**
	 * {@link EventNotifier#post(EventContext)}
	 */
	@Override
	public void post(EventContext<?> eventContext) {
		if (stop) {
			throw new IllegalStateException("事件通知器正在停止，无法抛出事件");
		}

		postCountStat.increment();

		try {
			if (poolFull) {
				postSync(eventContext);

				if (getPollQueueSize() < (queueSize / 2)) {
					poolFull = false;
				}
			} else {
				eventQueue.put(eventContext);
			}
		} catch (InterruptedException e) {
			logger.error("在添加事件时发生异常", e);
		}
	}

	@Override
	public void postSync(EventContext<?> eventContext) {
		String name = eventContext.getName();

		if (!listeners.containsKey(name)) {
			if (logger.isDebugEnabled()) {
				logger.warn("事件 {} 没有注册侦听器", name);
			}
			return;
		}

		for (EventListener<?> listener : listeners.get(name)) {
			onEvent(listener, eventContext);
		}

		syncConsumeCountStat.increment();
	}

	@Override
	public <T> void post(T event) {
		post(new EventContext<T>(event));
	}

	@Override
	public <T> void postSync(T event) {
		postSync(new EventContext<T>(event));
	}

	/**
	 * {@link EventNotifier#register(String, EventListener)}
	 */
	@Override
	public void register(String name, EventListener<?> listener) {
		CopyOnWriteArraySet<EventListener<?>> set = listeners.get(name);

		if (set == null) {
			set = new CopyOnWriteArraySet<EventListener<?>>();
			CopyOnWriteArraySet<EventListener<?>> prevSet = listeners.putIfAbsent(name, set);
			set = prevSet == null ? set : prevSet;
		}

		set.add(listener);
	}

	/**
	 * {@link EventNotifier#unregister(String, EventListener)}
	 */
	@Override
	public void unregister(String name, EventListener<?> listener) {
		CopyOnWriteArraySet<EventListener<?>> set = listeners.get(name);

		if (set != null) {
			set.remove(listener);
		}
	}

	/**
	 * {@link EventNotifier#register(Class, EventListener)}
	 */
	@Override
	public void register(Class<?> name, EventListener<?> listener) {
		register(name.getName(), listener);
	}

	/**
	 * {@link EventNotifier#unregister(Class, EventListener)}
	 */
	@Override
	public void unregister(Class<?> name, EventListener<?> listener) {
		unregister(name.getName(), listener);
	}

	/**
	 * 执行事件
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void onEvent(EventListener listener, EventContext eventContext) {
		NanoTimeStat nanoTimeStat = getNanoTimeStat(eventContext);
		nanoTimeStat.begin();
		try {
			listener.onEvent(eventContext);
		} catch (ClassCastException e) {
			logger.error("事件 {} 对象类型不符合侦听器 {} 的声明", new Object[] { eventContext.getName(), listener.getClass(), e });
		} catch (Throwable t) {
			logger.error("事件 {} 的侦听器 {} 运行时发生异常", new Object[] { eventContext.getName(), listener.getClass(), t });
		} finally {
			nanoTimeStat.end();
			getStat(eventContext).increment();
		}
		consumeCountStat.increment();
	}

	/**
	 * 事件通知器是否正在停止
	 */
	public boolean isStop() {
		return stop;
	}

	/**
	 * 获取计数
	 */
	@SuppressWarnings("rawtypes")
	private Stat getStat(EventContext eventContext) {
		return StatFactory.getStat(Stat.class, "count-EventNotifierImpl.$", eventContext.getName());
	}

	/**
	 * 获取计时
	 */
	@SuppressWarnings("rawtypes")
	private NanoTimeStat getNanoTimeStat(EventContext eventContext) {
		return StatFactory.getStat(NanoTimeStat.class, "time-EventNotifierImpl.$", eventContext.getName());
	}

	/**
	 * 消费线程执行
	 * 
	 * @author kidal
	 * 
	 */
	class ConsumerRunnable implements Runnable {

		/**
		 * {@link Runnable#run()}
		 */
		@Override
		public void run() {
			for (;;) {
				try {
					EventContext<?> eventContext = eventQueue.take();
					String name = eventContext.getName();

					if (!listeners.containsKey(name)) {
						if (logger.isDebugEnabled()) {
							logger.warn("事件 {} 没有注册侦听器", name);
						}
						continue;
					}

					for (EventListener<?> listener : listeners.get(name)) {
						Runnable runnable = createRunnable(listener, eventContext);

						try {
							pool.submit(runnable);
							dispatchCountStat.increment();
							consumeQueueSizeStat.set(getPollQueueSize());
						} catch (RejectedExecutionException e) {
							logger.warn("事件执行线程池已满，请调整配置参数，事件 {} 被强制同步执行", name);
							poolFull = true;
							onEvent(listener, eventContext);
						}
					}
				} catch (InterruptedException e) {
					logger.error("获取事件时出现异常", e);
				}
			}
		}

		/**
		 * 创建运行器
		 */
		@SuppressWarnings({ "rawtypes" })
		private Runnable createRunnable(final EventListener listener, final EventContext eventContext) {
			return new Runnable() {
				@Override
				public void run() {
					onEvent(listener, eventContext);
				}
			};
		}

	}

	@Override
	public int getQueueSize() {
		return eventQueue.size();
	}

	@Override
	public int getPollQueueSize() {
		return ((ThreadPoolExecutor) pool).getQueue().size();
	}

	@Override
	public int getPoolActiveCount() {
		return ((ThreadPoolExecutor) pool).getActiveCount();
	}

	@Override
	public List<String> getEvents() {
		List<EventContext<?>> dump = new ArrayList<EventContext<?>>(eventQueue);
		List<String> r = new ArrayList<String>(dump.size());
		for (EventContext<?> e : dump) {
			r.add(e.getName());
		}
		return r;
	}

	// getters and setters

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public void setPoolMaxSize(int poolMaxSize) {
		this.poolMaxSize = poolMaxSize;
	}

	public void setPoolAliveTime(int poolAliveTime) {
		this.poolAliveTime = poolAliveTime;
	}

	public void setPoolAwaitTime(int poolAwaitTime) {
		this.poolAwaitTime = poolAwaitTime;
	}
}
