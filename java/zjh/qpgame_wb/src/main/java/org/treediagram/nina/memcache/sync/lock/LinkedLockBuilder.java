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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;

/**
 * 锁链构造器
 * 
 * @author kidal
 * 
 */
public class LinkedLockBuilder {
	/**
	 * 对象锁持有者
	 */
	private ObjectLockHolder objectLockHolder = new ObjectLockHolder();

	/**
	 * 创建锁链
	 */
	public LinkedLock createLinkedLock(Object... objects) {
		List<? extends Lock> locks = createLocks(objects);
		return new LinkedLock(locks);
	}

	/**
	 * 创建锁列表
	 */
	public List<? extends Lock> createLocks(Object... objects) {
		// 获取对象锁
		List<ObjectLock> locks = new ArrayList<ObjectLock>();
		for (Object object : objects) {
			ObjectLock objectLock = objectLockHolder.getObjectLock(object);
			locks.add(objectLock);
		}

		// 排序对象锁
		Collections.sort(locks);

		// 紧锁
		TreeSet<Integer> ties = new TreeSet<Integer>();
		Integer start = null;
		for (int i = 0; i < locks.size(); i++) {
			if (start == null) {
				start = i;
			} else {
				ObjectLock objectLock1 = locks.get(start);
				ObjectLock objectLock2 = locks.get(i);
				if (objectLock1.isTie(objectLock2)) {
					ties.add(start);
				} else {
					start = i;
				}
			}
		}

		// 没有紧锁直接返回
		if (ties.size() == 0) {
			return locks;
		} else {
			// 添加紧锁
			List<Lock> newLocks = new ArrayList<Lock>(locks.size() + ties.size());
			newLocks.addAll(locks);
			Iterator<Integer> it = ties.descendingIterator();
			while (it.hasNext()) {
				Integer i = it.next();
				ObjectLock lock = locks.get(i);
				Lock tieLock = objectLockHolder.getTieLock(lock.getType());
				newLocks.add(i, tieLock);
			}
			return newLocks;
		}
	}
}
