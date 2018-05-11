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

package org.treediagram.nina.memcache.sync.lock;

import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对象锁持有者
 * 
 * @author kidal
 * 
 */
@SuppressWarnings("rawtypes")
public class ObjectLockHolder {
	/**
	 * 持有者集合
	 */
	private ConcurrentMap<Class, Holder> holders = new ConcurrentHashMap<Class, Holder>();

	/**
	 * 创建对象锁持有者
	 */
	public ObjectLockHolder() {

	}

	/**
	 * 获取对象锁
	 */
	public ObjectLock getObjectLock(Object object) {
		Holder holder = getHolder(object.getClass());
		ObjectLock objectLock = holder.getObjectLock(object);
		return objectLock;
	}

	/**
	 * 获取紧缩锁
	 */
	public Lock getTieLock(Class type) {
		Holder holder = getHolder(type);
		return holder.getTieLock();
	}

	/**
	 * 获取类型锁数量
	 */
	public int count(Class type) {
		if (holders.containsKey(type)) {
			Holder holder = getHolder(type);
			return holder.count();
		} else {
			return 0;
		}
	}

	/**
	 * 获取锁持有者
	 */
	private Holder getHolder(Class type) {
		Holder holder = holders.get(type);
		if (holder != null) {
			return holder;
		} else {
			holder = new Holder();
			Holder prevHolder = holders.putIfAbsent(type, holder);
			return prevHolder != null ? prevHolder : holder;
		}
	}

	/**
	 * 单一类型的锁持有者
	 * 
	 * @author kidal
	 * 
	 */
	class Holder {
		/**
		 * 紧缩使用的锁
		 */
		private final Lock tieLock = new ReentrantLock();

		/**
		 * 对象实体对应的锁缓存
		 */
		private final WeakHashMap<Object, ObjectLock> locks = new WeakHashMap<Object, ObjectLock>();

		/**
		 * 创建单一类型的锁持有者
		 */
		public Holder() {

		}

		/**
		 * 获取对象锁
		 */
		public ObjectLock getObjectLock(Object object) {
			ObjectLock objectLock = locks.get(object);
			if (objectLock != null) {
				return objectLock;
			} else {
				return createObjectLock(object);
			}
		}

		/**
		 * 获取紧缩使用的锁
		 */
		public Lock getTieLock() {
			return tieLock;
		}

		/**
		 * 获取锁的数量
		 */
		public int count() {
			return locks.size();
		}

		/**
		 * 创建对象锁
		 */
		private synchronized ObjectLock createObjectLock(Object object) {
			ObjectLock objectLock = locks.get(object);
			if (objectLock != null) {
				return objectLock;
			} else {
				objectLock = new ObjectLock(object);
				locks.put(object, objectLock);
				return objectLock;
			}
		}
	}
}
