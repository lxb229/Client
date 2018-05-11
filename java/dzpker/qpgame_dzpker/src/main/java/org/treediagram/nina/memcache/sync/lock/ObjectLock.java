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

import java.util.concurrent.locks.ReentrantLock;

import org.treediagram.nina.memcache.IEntity;

/**
 * 对象锁
 * 
 * @author kidal
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ObjectLock extends ReentrantLock implements Comparable<ObjectLock> {
	/**
	 * 序列化标识
	 */
	private static final long serialVersionUID = -5202252273848987240L;

	/**
	 * 实体类型
	 */
	private static final Class<IEntity> entityClass = IEntity.class;

	/**
	 * 锁定对象类型
	 */
	private final Class type;

	/**
	 * 排序依据
	 */
	private final Comparable value;

	/**
	 * 是否实体
	 */
	private final boolean entity;

	/**
	 * 创建对象锁
	 */
	public ObjectLock(Object object) {
		this(object, false);
	}

	/**
	 * 创建对象锁
	 */
	public ObjectLock(Object object, boolean fair) {
		super(fair);

		// 获取对象类型
		type = object.getClass();

		// 获取排序依据
		if (object instanceof IEntity) {
			value = (Comparable) ((IEntity) object).getId();
		} else {
			value = new Integer(System.identityHashCode(object));
		}

		// 判断是否实体
		if (entityClass.isAssignableFrom(type)) {
			entity = true;
		} else {
			entity = false;
		}
	}

	/**
	 * 是否紧缩，当前锁是否无法和另一锁分出先后顺序
	 */
	public boolean isTie(ObjectLock other) {
		if (type != other.type) {
			return false;
		} else if (value.compareTo(other.value) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * {@link Comparable#compareTo(Object)}
	 */
	@Override
	public int compareTo(ObjectLock other) {
		if (isEntity() && !other.isEntity()) {
			return 1;
		} else if (!isEntity() && other.isEntity()) {
			return -1;
		} else if (type != other.type) {
			if (type.hashCode() < other.type.hashCode()) {
				return -1;
			} else if (type.hashCode() > other.type.hashCode()) {
				return 1;
			} else {
				return type.getName().compareTo(other.type.getName());
			}
		} else {
			return value.compareTo(other.value);
		}
	}

	// getters

	public Class getType() {
		return type;
	}

	public Comparable getValue() {
		return value;
	}

	public boolean isEntity() {
		return entity;
	}
}
