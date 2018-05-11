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

package org.treediagram.nina.memcache.sync;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;
import org.treediagram.nina.memcache.sync.lock.LinkedLock;
import org.treediagram.nina.memcache.sync.lock.LinkedLockBuilder;

/**
 * 对象同步服务
 * 
 * @author kidal
 * 
 */
@Service
public class ObjectSyncService 
{
	/**
	 * 锁链构造器
	 */
	private LinkedLockBuilder linkedLockBuilder = new LinkedLockBuilder();

	/**
	 * 模板集合
	 */
	private ConcurrentMap<Method, MethodSyncTemplate> templates = new ConcurrentHashMap<Method, MethodSyncTemplate>();

	/**
	 * 线程锁定标记
	 */
	private ThreadLocal<Object> threadLockMark = new ThreadLocal<Object>();

	/**
	 * 同步执行
	 */
	public Object syncCall(boolean forceLock, Method method, ObjectSyncCallable<Object> callable, Object[] args)
			throws Throwable {
		// 强制锁定
		if (forceLock) {
			return lockCall(method, callable, args);
		} else {
			// 非强制锁定，每个线程只锁定一次
			if (threadLockMark.get() == null) {
				threadLockMark.set(new Object());
				try {
					return lockCall(method, callable, args);
				} finally {
					threadLockMark.remove();
				}
			} else {
				return callable.call(args, true);
			}
		}
	}

	/**
	 * 执行锁定
	 */
	private Object lockCall(Method method, ObjectSyncCallable<Object> callable, Object[] args) throws Throwable {
		// 获取锁定模板
		MethodSyncTemplate template = templates.get(method);
		if (template == null) {
			template = createTemplate(method);
		}

		// 获取锁
		LinkedLock linkedLock = template.getLinkedLock(args);
		if (linkedLock == null) {
			return callable.call(args, false);
		}

		// 锁定
		linkedLock.lock();
		try {
			return callable.call(args, true);
		} finally {
			linkedLock.unlock();
		}
	}

	/**
	 * 创建模板
	 */
	private MethodSyncTemplate createTemplate(Method method) {
		MethodSyncTemplate template = new MethodSyncTemplate(linkedLockBuilder, method);
		MethodSyncTemplate prevTemplate = templates.putIfAbsent(method, template);
		return prevTemplate != null ? prevTemplate : template;
	}
}
