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

package org.treediagram.nina.network.command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 前端方法同步调用器
 * 
 * @author kidal
 * 
 */
public class FacadeMethodSyncCaller {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(FacadeMethodSyncCaller.class);

	/**
	 * 同步队列集合
	 */
	private final ConcurrentMap<String, BlockingQueue<Runnable>> queues = new ConcurrentHashMap<String, BlockingQueue<Runnable>>();

	/**
	 * 处理线程集合
	 */
	private final ConcurrentMap<String, Thread> threads = new ConcurrentHashMap<String, Thread>();

	/**
	 * 同步队列线程操作锁
	 */
	private final ConcurrentMap<String, ReentrantLock> locks = new ConcurrentHashMap<String, ReentrantLock>();

	/**
	 * 执行
	 */
	public void execute(String key, Runnable runnable) {
		if (key == null || runnable == null) {
			throw new IllegalArgumentException("同步键或任务不能为空");
		}

		BlockingQueue<Runnable> queue = loadSyncQueue(key);
		queue.add(runnable);

		Lock lock = loadSyncLock(key);
		lock.lock();
		try {
			if (!threads.containsKey(key)) {
				SyncRunnable syncRunnable = new SyncRunnable(key, queue);
				Thread thread = new Thread(syncRunnable, "通信同步处理:" + key);
				thread.setDaemon(true);
				if (threads.putIfAbsent(key, thread) == null) {
					thread.start();
				}
			}
		} catch (Exception e) {
			logger.error("创建同步队列 " + key + " 处理线程时出现未知错误", e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取同步操作锁
	 */
	private Lock loadSyncLock(String key) {
		ReentrantLock result = locks.get(key);
		if (result != null) {
			return result;
		}
		result = new ReentrantLock();
		ReentrantLock prev = locks.putIfAbsent(key, result);
		return prev == null ? result : prev;
	}

	/**
	 * 获取同步处理队列
	 */
	private BlockingQueue<Runnable> loadSyncQueue(String key) {
		BlockingQueue<Runnable> result = queues.get(key);
		if (result != null) {
			return result;
		}
		result = new LinkedBlockingQueue<Runnable>();
		BlockingQueue<Runnable> prev = queues.putIfAbsent(key, result);
		return prev == null ? result : prev;
	}

	/**
	 * 同步线程执行对象
	 * 
	 * @author kidal
	 * 
	 */
	private class SyncRunnable implements Runnable {

		/** 同步键 */
		private final String key;
		/** 处理队列 */
		private final BlockingQueue<Runnable> queue;

		/**
		 * 创建同步线程执行对象
		 */
		public SyncRunnable(String key, BlockingQueue<Runnable> queue) {
			this.key = key;
			this.queue = queue;
		}

		/**
		 * {@link Runnable#run()}
		 */
		@Override
		public void run() {
			mainloop: for (;;) {
				try {
					Runnable runnable = queue.take();
					runnable.run();
				} catch (InterruptedException e) {
					logger.error("同步队列 " + key + "处理线程被打断", e);
				} catch (Exception e) {
					logger.error("同步队列 " + key + "处理线程发生未知异常", e);
				}

				if (queue.isEmpty()) {
					Lock lock = loadSyncLock(key);
					lock.lock();
					try {
						if (queue.isEmpty()) {
							threads.remove(key);
							break mainloop;
						}
					} catch (Exception e) {
						logger.debug("结束同步队列 " + key + " 处理线程时出现未知错误", e);
					} finally {
						lock.unlock();
					}
				}
			}
		}

	}
}
