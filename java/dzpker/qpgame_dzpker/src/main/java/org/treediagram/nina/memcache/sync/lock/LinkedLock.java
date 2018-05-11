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

import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 锁链
 * 
 * @author kidal
 * 
 */
public class LinkedLock {
	/**
	 * 当前锁
	 */
	private final Lock current;

	/**
	 * 下一个锁
	 */
	private final LinkedLock next;

	/**
	 * 创建锁链
	 */
	public LinkedLock(List<? extends Lock> locks) {
		// 参数检查
		if (locks == null || locks.size() == 0) {
			throw new IllegalArgumentException("创建锁链的锁数不能为0");
		}

		// 保存锁
		this.current = locks.remove(0);

		// 创建下一个节点
		if (locks.size() > 0) {
			this.next = new LinkedLock(locks);
		} else {
			this.next = null;
		}
	}

	/**
	 * 锁定
	 */
	public void lock() {
		if (next != null) {
			current.lock();
			next.lock();
		} else {
			current.lock();
		}
	}

	/**
	 * 解锁
	 */
	public void unlock() {
		if (next != null) {
			next.unlock();
			current.unlock();
		} else {
			current.unlock();
		}
	}
}
