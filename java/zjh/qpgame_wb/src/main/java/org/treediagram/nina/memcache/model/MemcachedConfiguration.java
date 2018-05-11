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

package org.treediagram.nina.memcache.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.treediagram.nina.core.reflect.ReflectionUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.MemcacheIndex;
import org.treediagram.nina.memcache.annotation.Memcached;
import org.treediagram.nina.memcache.annotation.MemcachedInitial;
import org.treediagram.nina.memcache.enhance.EnhancedEntity;
import org.treediagram.nina.memcache.exception.MemcacheConfigurationException;
import org.treediagram.nina.memcache.exception.MemcacheStateException;

/**
 * 内存缓存实体配置
 * 
 * @author kidalsama
 * 
 */
public class MemcachedConfiguration implements Serializable {
	/**
	 * 序列化标识
	 */
	private static final long serialVersionUID = 1925794635665119310L;

	/**
	 * 校验
	 */
	public static boolean validate(Class<? extends IEntity<?>> entityClass, Map<String, Integer> constants) {
		if (Modifier.isAbstract(entityClass.getModifiers())) {
			return false;
		} else if (Modifier.isInterface(entityClass.getModifiers())) {
			return false;
		} else if (!IEntity.class.isAssignableFrom(entityClass)) {
			return false;
		} else if (!entityClass.isAnnotationPresent(Memcached.class)) {
			return false;
		}
		Memcached memcached = entityClass.getAnnotation(Memcached.class);
		if (!constants.containsKey(memcached.size())) {
			throw new MemcacheConfigurationException("缓存实体[" + entityClass.getName() + "]要求的缓存数量定义[" + memcached.size()
					+ "]不存在");
		}
		switch (memcached.unit()) {
		case ENTITY:
			if (ReflectionUtils.getDeclaredFieldsWith(entityClass, MemcacheIndex.class).length > 0) {
				throw new MemcacheConfigurationException("缓存单位为[" + memcached.unit() + "]的实体[" + entityClass.getName()
						+ "]不支持索引属性配置");
			}
			break;
		case INDEX:
			if (ReflectionUtils.getDeclaredFieldsWith(entityClass, MemcacheIndex.class).length == 0) {
				throw new MemcacheConfigurationException("缓存单位为[" + memcached.unit() + "]的实体[" + entityClass.getName()
						+ "]没有配置任何索引");
			}
			break;
		default:
			throw new MemcacheConfigurationException("实体[" + entityClass.getName() + "]使用了未支持的缓存单位[" + memcached.unit()
					+ "]配置");
		}
		return true;
	}

	/**
	 * 实体类型
	 */
	private Class<? extends IEntity<?>> entityClass;

	/**
	 * 缓存配置
	 */
	private Memcached memcached;

	/**
	 * 初始化配置
	 */
	private MemcachedInitial memcachedInitial;

	/**
	 * 缓存数量
	 */
	private int size;

	/**
	 * 索引
	 */
	private transient Map<String, MemcacheIndex> indices;

	/**
	 * 索引字段
	 */
	private transient Map<String, Field> indexFields;

	/**
	 * 索引同步锁
	 */
	private transient Map<String, ReentrantReadWriteLock> indexLocks;

	/**
	 * 创建内存缓存实体配置
	 */
	@SuppressWarnings("unused")
	private MemcachedConfiguration() {

	}

	/**
	 * 创建内存缓存实体配置
	 */
	public MemcachedConfiguration(Class<? extends IEntity<?>> entityClass, Map<String, Integer> constants) {
		// 保存参数
		this.entityClass = entityClass;
		this.memcached = entityClass.getAnnotation(Memcached.class);
		this.memcachedInitial = entityClass.getAnnotation(MemcachedInitial.class);
		this.size = constants.get(memcached.size());

		// 字段
		Field[] fields = null;

		// 初始化索引信息
		fields = ReflectionUtils.getDeclaredFieldsWith(entityClass, MemcacheIndex.class);

		if (fields != null && fields.length > 0) {
			indices = new HashMap<String, MemcacheIndex>(fields.length);
			indexFields = new HashMap<String, Field>(fields.length);
			indexLocks = new HashMap<String, ReentrantReadWriteLock>(fields.length);

			for (Field field : fields) {
				MemcacheIndex memcacheIndex = field.getAnnotation(MemcacheIndex.class);
				String name = field.getName();

				ReflectionUtils.makeAccessible(field);

				indices.put(name, memcacheIndex);
				indexFields.put(name, field);
				indexLocks.put(name, new ReentrantReadWriteLock());
			}
		}
	}

	// index getters

	/**
	 * 获取索引名集合
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getIndicesNames() {
		if (indices != null) {
			return new HashSet<String>(indices.keySet());
		} else {
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * 获取索引命名查询名
	 */
	public String getIndexQuery(String name) {
		if (indices == null) {
			throw new MemcacheStateException("实体" + entityClass.getName() + "的索引属性名" + name + "无效");
		} else {
			MemcacheIndex index = indices.get(name);
			if (index != null) {
				return index.query();
			} else {
				throw new MemcacheStateException("实体" + entityClass.getName() + "的索引属性名" + name + "无效");
			}
		}
	}

	/**
	 * 获取试题索引键值
	 */
	public Map<String, Object> getIndexValues(IEntity<?> entity) {
		if (indexFields == null) {
			throw new MemcacheStateException("实体" + entityClass.getName() + "没有索引属性配置无法获取索引属性值");
		}

		if (entity instanceof EnhancedEntity) {
			entity = ((EnhancedEntity) entity).getEntity();
		}

		try {
			HashMap<String, Object> result = new HashMap<String, Object>(indexFields.size());
			for (Entry<String, Field> e : indexFields.entrySet()) {
				Object value = e.getValue().get(entity);
				result.put(e.getKey(), value);
			}
			return result;
		} catch (Exception e) {
			throw new MemcacheStateException("无法获取索引属性值:" + e.getMessage());
		}
	}

	/**
	 * 获取指定的索引属性值
	 */
	public Object getIndexValue(String name, IEntity<?> entity) {
		Map<String, Object> values = getIndexValues(entity);
		if (values.containsKey(name)) {
			return values.get(name);
		} else {
			throw new MemcacheStateException("索引属性" + name + "不存在");
		}
	}

	/**
	 * 获取指定索引属性域的读锁
	 */
	public ReadLock getIndexReadLock(String name) {
		ReentrantReadWriteLock lock = indexLocks.get(name);
		if (lock == null) {
			throw new MemcacheStateException("实体" + entityClass.getName() + "的索引属性名" + name + "]无效");
		}
		return lock.readLock();
	}

	/**
	 * 获取指定索引属性域的写锁
	 */
	public WriteLock getIndexWriteLock(String name) {
		ReentrantReadWriteLock lock = indexLocks.get(name);
		if (lock == null) {
			throw new MemcacheStateException("实体" + entityClass.getName() + "的索引属性名" + name + "]无效");
		}
		return lock.writeLock();
	}

	/**
	 * 检查是否有索引属性域
	 */
	public boolean hasIndexField(String name) {
		if (indexFields == null) {
			return false;
		}
		if (indexFields.containsKey(name)) {
			return true;
		}
		return false;
	}

	/**
	 * 检查实体单位
	 */
	public boolean isUnit(MemcachedUnit unit) {
		return memcached.unit().equals(unit);
	}

	// getters

	/**
	 * @return 实体类型
	 */
	public Class<? extends IEntity<?>> getEntityClass() {
		return entityClass;
	}

	/**
	 * @return 缓存配置
	 */
	public Memcached getMemcached() {
		return memcached;
	}

	/**
	 * @return 初始化配置
	 */
	public MemcachedInitial getMemcachedInitial() {
		return memcachedInitial;
	}

	/**
	 * @return 实体缓存数量
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return 回写器名称
	 */
	public String getWriterName() {
		return memcached.writebackPolicy();
	}
}
