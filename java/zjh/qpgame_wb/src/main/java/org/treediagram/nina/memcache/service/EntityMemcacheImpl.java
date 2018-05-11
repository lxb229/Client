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

package org.treediagram.nina.memcache.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Memcached;
import org.treediagram.nina.memcache.annotation.MemcachedInitial;
import org.treediagram.nina.memcache.enhance.EnhancedEntity;
import org.treediagram.nina.memcache.enhance.Enhancer;
import org.treediagram.nina.memcache.enhance.EntityMemcacheEnhancer;
import org.treediagram.nina.memcache.exception.MemcacheStateException;
import org.treediagram.nina.memcache.model.MemcachedConfiguration;
import org.treediagram.nina.memcache.orm.Accessor;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.writeback.AbstractEventListener;
import org.treediagram.nina.memcache.writeback.Element;
import org.treediagram.nina.memcache.writeback.Writer;
import org.treediagram.nina.stat.StatFactory;
import org.treediagram.nina.stat.model.Stat;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap.Builder;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;

/**
 * 实体内存缓存实现
 * 
 * @author kidalsama
 */
public class EntityMemcacheImpl<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> implements
		EntityMemcache<PK, T>, CacheFinder<PK, T> {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(EntityMemcacheImpl.class);

	/**
	 * 加载统计
	 */
	private Stat loadStat;

	/**
	 * 更新统计
	 */
	private Stat updateStat;

	/**
	 * LRU释放统计
	 */
	private Stat evictionStat;

	/**
	 * 是否初始化
	 */
	private boolean initialized = false;

	/**
	 * 配置
	 */
	private MemcachedConfiguration config;

	/**
	 * 实体类型
	 */
	private Class<T> entityClass;

	/**
	 * 访问器
	 */
	private Accessor accessor;

	/**
	 * 查询器
	 */
	private Querier querier;

	/**
	 * 回写器
	 */
	private Writer writer;

	/**
	 * 实体缓存
	 */
	private ConcurrentMap<PK, T> cache;

	/**
	 * 增强器
	 */
	private Enhancer enhancer;

	/**
	 * 正在移除的实体缓存
	 */
	private final ConcurrentMap<PK, Object> removing = new ConcurrentHashMap<PK, Object>();

	/**
	 * 主键锁
	 */
	private final ConcurrentMap<PK, Lock> locks = new ConcurrentHashMap<PK, Lock>();

	/**
	 * 创建实体内存缓存实现 实体内存缓存实现
	 */
	public EntityMemcacheImpl() {

	}

	/**
	 * 初始化
	 */
	public synchronized void init(MemcachedConfiguration config, Writer writer, Accessor accessor, Querier querier) {
		if (initialized) {
			throw new MemcacheStateException("重复初始化:" + getClass().getName());
		} else {
			initFields(config, writer, accessor, querier);
			initCache();
			initStat();
			initialized = true;
		}
	}

	/**
	 * 初始化字段
	 */
	@SuppressWarnings("unchecked")
	private void initFields(MemcachedConfiguration config, Writer writer, Accessor accessor, Querier querier) {
		// 初始化字段
		this.config = config;
		this.entityClass = (Class<T>) config.getEntityClass();
		this.accessor = accessor;
		this.querier = querier;
		this.writer = writer;

		// 侦听回写事件
		this.writer.addEventListener(entityClass, new AbstractEventListener() {
			@Override
			protected void onRemoveSuccess(Object id) {
				removing.remove(id);
			}

			@Override
			protected void onRemoveError(Object id, RuntimeException ex) {
				removing.remove(id);
			}
		});

		// 初始化增强器
		if (config.getMemcached().enhance()) {
			enhancer = new EntityMemcacheEnhancer(this);
		}

		// 初始化缓存管理策略
		switch (config.getMemcached().type()) {
		case LRU:
			createLRUCache(config.getMemcached());
			break;

		case MANUAL:
			cache = new ConcurrentHashMap<PK, T>();
			break;
		}
	}

	/**
	 * 创建 LRU 缓存
	 */
	private void createLRUCache(Memcached memcached) {
		Builder<PK, T> builder = new Builder<PK, T>().initialCapacity(memcached.initialCapacity())
				.maximumWeightedCapacity(config.getSize()).concurrencyLevel(memcached.concurrencyLevel());
		builder.listener(new EvictionListener<PK, T>() {
			@Override
			public void onEviction(PK key, T value) {
				evictionStat.increment();
			}
		});
		cache = builder.build();
	}

	/**
	 * 初始化缓存
	 */
	private void initCache() {
		MemcachedInitial initial = config.getMemcachedInitial();
		if (initial == null) {
			return;
		}

		List<T> entities = loadEntities(initial);
		if (entities == null) {
			return;
		}

		for (T entity : entities) {
			PK pk = entity.getId();
			if (enhancer != null) {
				entity = enhancer.transform(entity);
			}
			cache.put(pk, entity);
		}
	}

	/**
	 * 初始化统计
	 */
	private void initStat() {
		String entityName = config.getEntityClass().getSimpleName();

		loadStat = StatFactory.getStat(Stat.class, "count-EntityMemcache." + entityName, "load");
		updateStat = StatFactory.getStat(Stat.class, "count-EntityMemcache." + entityName, "update");
		evictionStat = StatFactory.getStat(Stat.class, "count-EntityMemcache." + entityName, "eviction");
	}

	/**
	 * 载入实体
	 */
	private List<T> loadEntities(MemcachedInitial initial) {
		List<T> entities = null;

		switch (initial.initialType()) {
		case LOAD_ALL:
			entities = querier.all(entityClass);
			break;

		case USE_QUERY:
			entities = querier.list(entityClass, initial.query());
			break;
		}

		return entities;
	}

	/**
	 * 锁定主键
	 */
	private Lock lockPrimaryKey(PK id) {
		Lock lock = locks.get(id);
		if (lock == null) {
			lock = new ReentrantLock();
			Lock prevLock = locks.putIfAbsent(id, lock);
			lock = prevLock == null ? lock : prevLock;
		}
		lock.lock();
		return lock;
	}

	/**
	 * 解锁主键
	 */
	private void unlockPrimaryKey(PK id, Lock lock) {
		lock.unlock();
		locks.remove(id);
	}

	@Override
	public T load(PK id) {
		return loadOrCreate(id, null);
	}

	@Override
	public T loadOrCreate(PK id, EntityBuilder<PK, T> builder) {
		// 判断主键是否有效
		if (removing.containsKey(id)) {
			return null;
		}

		// 尝试获取实体
		T entity = cache.get(id);

		// 实体不存在则尝试加载
		if (entity == null) {
			// 锁定主键
			Lock lock = lockPrimaryKey(id);

			try {
				// 判断主键是否有效
				if (removing.containsKey(id)) {
					return null;
				}

				// 尝试获取实体
				entity = cache.get(id);

				if (entity == null) {
					entity = accessor.load(entityClass, id);
					loadStat.increment();

					// 载入失败则创建实体
					if (entity == null && builder != null) {
						entity = builder.createInstance(id);

						if (entity != null) {
							writer.put(Element.saveOf(entity));
						}
					}

					// 保存实体
					if (entity != null) {
						if (enhancer != null) {
							entity = enhancer.transform(entity);
						}
						cache.put(id, entity);
					}
				}
			} finally {
				
				// 解锁主键
				unlockPrimaryKey(id, lock);
			}
		}

		// 返回实体
		return entity;
	}

	@Override
	public List<T> loadByQuery(String queryname, Object... params) {
		List<T> cachedEntities = new ArrayList<T>();
		List<T> uncachedEntities = querier.list(entityClass, queryname, params);
		for (T t : uncachedEntities) {
			T entity = loadOrCreate(t.getId(), null);
			if (entity == null) {
				continue;
			}
			cachedEntities.add(entity);
		}
		return cachedEntities;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T saveOrUpdate(T entity) {
		// 去壳
		if (entity instanceof EnhancedEntity) {
			entity = (T) ((EnhancedEntity) entity).getEntity();
		}

		// 获取编号
		PK id = entity.getId();

		if (id == null) {
			throw new IllegalStateException("实体主键为 null 不能保存或更新");
		}

		// 判断主键是否有效
		if (removing.containsKey(id)) {
			return null;
		}

		// 锁定主键
		Lock lock = lockPrimaryKey(id);

		try {
			// 尝试载入实体
			T existsEntity = load(id);

			// 如果实体不存在则直接保存，否则回写
			if (existsEntity == null) {
				accessor.save(entityClass, entity);
			} else {
				writeBack(entity);
			}

			// 增强实体
			if (enhancer != null) {
				if (!(entity instanceof EnhancedEntity)) {
					entity = enhancer.transform(entity);
				}
			}

			// 刷新缓存
			cache.put(id, entity);

			// 返回实体
			return entity;
		} finally {
			// 解锁主键
			unlockPrimaryKey(id, lock);
		}
	}

	@Override
	public List<T> find(Filter<T> filter) {
		List<T> result = new ArrayList<T>();
		for (T entity : cache.values()) {
			if (filter.isExclude(entity)) {
				continue;
			} else {
				result.add(entity);
			}
		}
		return result;
	}

	@Override
	public List<T> sort(Comparator<T> comparator) {
		List<T> result = new ArrayList<T>(cache.values());
		Collections.sort(result, comparator);
		return result;
	}

	@Override
	public List<T> find(Filter<T> filter, Comparator<T> comparator) {
		List<T> result = new ArrayList<T>();
		for (T entity : cache.values()) {
			if (filter.isExclude(entity)) {
				continue;
			} else {
				result.add(entity);
			}
		}
		Collections.sort(result, comparator);
		return result;
	}

	@Override
	public List<T> all() {
		List<T> result = new ArrayList<T>(cache.values());
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void writeBack(T entity) {
		if (entity instanceof EnhancedEntity) {
			entity = (T) ((EnhancedEntity) entity).getEntity();
		}

		PK id = entity.getId();

		if (removing.containsKey(id)) {
			if (logger.isWarnEnabled()) {
				logger.warn("尝试更新处于待删除状态的实体[{}:{}],操作将被忽略", entityClass.getSimpleName(), id);
			}
			return;
		}

		if (logger.isDebugEnabled()) {
			//logger.debug("回写实体 id:{} class:{}", id, entity.getClass());
		}

		writer.put(Element.updateOf(entity));
		updateStat.increment();
	}

	@Override
	public T remove(PK id) {
		if (removing.containsKey(id)) {
			return null;
		}

		Lock lock = lockPrimaryKey(id);

		try {
			if (removing.containsKey(id)) {
				return null;
			}

			removing.put(id, Boolean.TRUE);
			T removed = cache.remove(id);
			writer.put(Element.removeOf(id, entityClass));

			return removed;
		} finally {
			unlockPrimaryKey(id, lock);
		}
	}

	@Override
	public void clear(PK id) {
		T entity = cache.get(id);

		if (entity == null) {
			return;
		}

		Lock lock = lockPrimaryKey(id);

		try {
			cache.remove(id);
		} finally {
			unlockPrimaryKey(id, lock);
		}
	}

	@Override
	public void truncate() {
		for (PK id : cache.keySet()) {
			clear(id);
		}
	}

	@Override
	public CacheFinder<PK, T> getFinder() {
		return this;
	}

	@Override
	public MemcachedConfiguration getConfiguration() {
		return config;
	}

	@Override
	public Writer getWriter() {
		return writer;
	}
}
