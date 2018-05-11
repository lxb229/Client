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

package org.treediagram.nina.memcache;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.Assert;
import org.treediagram.nina.memcache.exception.MemcacheConfigurationException;
import org.treediagram.nina.memcache.exception.MemcacheStateException;
import org.treediagram.nina.memcache.model.MemcachedConfiguration;
import org.treediagram.nina.memcache.model.MemcachedUnit;
import org.treediagram.nina.memcache.orm.Accessor;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.CacheFinder;
import org.treediagram.nina.memcache.service.EntityIndexMemcache;
import org.treediagram.nina.memcache.service.EntityIndexMemcacheImpl;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.memcache.service.EntityMemcacheImpl;
import org.treediagram.nina.memcache.writeback.Writer;
import org.treediagram.nina.memcache.writeback.WriterConfig;
import org.treediagram.nina.memcache.writeback.queue.QueueWriter;
import org.treediagram.nina.memcache.writeback.timing.TimingWriter;

/**
 * 内存缓存管理器
 * 
 * @author kidalsama
 * 
 */
public class MemcacheManager implements MemcacheManagerMBean {
	/**
	 * 日志
	 */
	private Logger logger = LoggerFactory.getLogger(MemcacheManager.class);

	/**
	 * 访问器
	 */
	private final Accessor accessor;

	/**
	 * 查询器
	 */
	private final Querier querier;

	/**
	 * 回写器配置
	 */
	private final Map<String, WriterConfig> writerConfigs;

	/**
	 * 配置
	 */
	private final Map<Class<? extends IEntity<?>>, MemcachedConfiguration> memcachedConfigurations = new HashMap<Class<? extends IEntity<?>>, MemcachedConfiguration>();

	/**
	 * 回写器
	 */
	private final Map<String, Writer> writers = new HashMap<String, Writer>();

	/**
	 * 实体缓存
	 */
	private final Map<Class<? extends IEntity<?>>, EntityMemcache<?, ?>> entityMemcaches = new HashMap<Class<? extends IEntity<?>>, EntityMemcache<?, ?>>();

	/**
	 * 实体索引缓存
	 */
	private final Map<Class<? extends IEntity<?>>, EntityIndexMemcache<?, ?>> entityIndexMemcaches = new HashMap<Class<? extends IEntity<?>>, EntityIndexMemcache<?, ?>>();

	/**
	 * 创建内存缓存管理器
	 */
	public MemcacheManager(Accessor accessor, Querier querier, Set<Class<IEntity<?>>> entityClasses,
			Map<String, WriterConfig> writerConfigs, Map<String, Integer> constants) {
		// 检查
		Assert.notNull(accessor, "访问器不能为 null");
		Assert.notNull(querier, "查询器不能为 null");
		Assert.notNull(entityClasses, "实体集合 null");

		// 保存值
		this.accessor = accessor;
		this.querier = querier;
		this.writerConfigs = writerConfigs;

		// 缓存声明
		for (Class<IEntity<?>> entityClass : entityClasses) {
			if (memcachedConfigurations.containsKey(entityClass)) {
				continue;
			} else {
				if (MemcachedConfiguration.validate(entityClass, constants)) {
					MemcachedConfiguration memcachedConfiguration = new MemcachedConfiguration(entityClass, constants);
					memcachedConfigurations.put(entityClass, memcachedConfiguration);
				} else {
					FormattingTuple message = MessageFormatter.format("实体[{}]配置错误", entityClass);
					logger.debug(message.getMessage());
					throw new MemcacheConfigurationException(message.getMessage());
				}
			}
		}

		// 注册 MBean
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName objectName = new ObjectName("org.treediagram.nina:type=MemcacheManagerMBean");
			mbs.registerMBean(this, objectName);
		} catch (Exception e) {
			logger.error("注册 [{}] 的 JMX 管理接口失败", this, e);
		}
	}

	/**
	 * 销毁
	 */
	public void destory() {
		if (logger.isDebugEnabled()) {
			logger.debug("开始停止实体更新队列");
		}

		for (Writer writer : writers.values()) {
			writer.stopAndWriteback();
		}
	}

	/**
	 * 获取回写器
	 */
	private Writer getWriter(String name) {
		Writer writer = writers.get(name);

		if (writer == null) {
			if (writerConfigs.containsKey(name)) {
				WriterConfig writerConfig = writerConfigs.get(name);

				switch (writerConfig.getType()) {
				case QUEUE:
					writer = new QueueWriter();
					break;

				case TIMING:
					writer = new TimingWriter();
					break;

				}

				writer.initialize(name, accessor, writerConfig.getValue());
				writers.put(name, writer);
			} else {
				throw new MemcacheConfigurationException("回写器 " + name + "的配置信息不存在");
			}
		}

		return writer;
	}

	/**
	 * 获取实体缓存
	 */
	@SuppressWarnings("rawtypes")
	public EntityMemcache getMemcache(Class<? extends IEntity<?>> entityClass, boolean createOneIfNotExists) {
		MemcachedConfiguration configuration = memcachedConfigurations.get(entityClass);
		if (configuration == null) {
			throw new MemcacheStateException("类" + entityClass.getName() + "不是有效的缓存实体");
		}
		if (!configuration.isUnit(MemcachedUnit.ENTITY)) {
			throw new MemcacheStateException("类" + entityClass.getName() + "的缓存单位不是" + MemcachedUnit.ENTITY);
		}
		EntityMemcache entityMemcache = entityMemcaches.get(entityClass);
		if (entityMemcache == null && createOneIfNotExists) {
			entityMemcache = createEntityMemcache(entityClass);
		}
		return entityMemcache;
	}

	/**
	 * 创建实体缓存
	 */
	@SuppressWarnings({ "rawtypes" })
	private synchronized EntityMemcache createEntityMemcache(Class<? extends IEntity<?>> entityClass) {
		if (entityMemcaches.containsKey(entityClass)) {
			return entityMemcaches.get(entityClass);
		} else {
			MemcachedConfiguration memcachedConfiguration = memcachedConfigurations.get(entityClass);
			Writer writer = getWriter(memcachedConfiguration.getWriterName());

			EntityMemcacheImpl entityMemcache = new EntityMemcacheImpl();
			entityMemcache.init(memcachedConfiguration, writer, accessor, querier);
			entityMemcaches.put(entityClass, entityMemcache);
			return entityMemcache;
		}
	}

	/**
	 * 获取实体缓存
	 */
	@SuppressWarnings("rawtypes")
	public EntityIndexMemcache getIndexMemcache(Class<? extends IEntity<?>> entityClass, boolean createOneIfNotExists) {
		MemcachedConfiguration configuration = memcachedConfigurations.get(entityClass);
		if (configuration == null) {
			throw new MemcacheStateException("类" + entityClass.getName() + "不是有效的缓存实体");
		}
		if (!configuration.isUnit(MemcachedUnit.INDEX)) {
			throw new MemcacheStateException("类" + entityClass.getName() + "的缓存单位不是" + MemcachedUnit.INDEX);
		}
		EntityIndexMemcache entityMemcache = entityIndexMemcaches.get(entityClass);
		if (entityMemcache == null && createOneIfNotExists) {
			entityMemcache = createEntityIndexMemcache(entityClass);
		}
		return entityMemcache;
	}

	/**
	 * 创建实体缓存
	 */
	@SuppressWarnings({ "rawtypes" })
	private synchronized EntityIndexMemcache createEntityIndexMemcache(Class<? extends IEntity<?>> entityClass) {
		if (entityIndexMemcaches.containsKey(entityClass)) {
			return entityIndexMemcaches.get(entityClass);
		} else {
			MemcachedConfiguration memcachedConfiguration = memcachedConfigurations.get(entityClass);
			Writer writer = getWriter(memcachedConfiguration.getWriterName());

			EntityIndexMemcacheImpl entityIndexMemcache = new EntityIndexMemcacheImpl();
			entityIndexMemcache.init(memcachedConfiguration, writer, accessor, querier);
			entityIndexMemcaches.put(entityClass, entityIndexMemcache);
			return entityIndexMemcache;
		}
	}

	/**
	 * {@link MemcacheManagerMBean#getAllMemcachedConfiguration()}
	 */
	@Override
	public Map<String, MemcachedConfiguration> getAllMemcachedConfiguration() {
		Map<String, MemcachedConfiguration> result = new LinkedHashMap<String, MemcachedConfiguration>();
		for (Entry<Class<? extends IEntity<?>>, EntityMemcache<?, ?>> entry : entityMemcaches.entrySet()) {
			result.put(entry.getKey().getName(), entry.getValue().getConfiguration());
		}
		return result;
	}

	@Override
	public Map<String, Map<String, String>> getAllWriterInfo() {
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		for (Entry<String, Writer> e : writers.entrySet()) {
			result.put(e.getKey(), e.getValue().getInformation());
		}
		return result;
	}

	@Override
	public Map<String, String> getWriterInfo(String name) {
		Writer writer = writers.get(name);
		return writer != null ? writer.getInformation() : null;
	}

	@Override
	public Map<String, Integer> getCachedEntityCount() {
		Map<String, Integer> result = new HashMap<String, Integer>(entityMemcaches.size());
		for (Entry<Class<? extends IEntity<?>>, EntityMemcache<?, ?>> e : entityMemcaches.entrySet()) {
			Class<? extends IEntity<?>> key = e.getKey();
			EntityMemcache<?, ?> value = e.getValue();
			CacheFinder<?, ?> finder = value.getFinder();
			int size = finder.all().size();

			result.put(key.getName(), size);
		}
		return result;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Map<String, Integer>> getCachedIndexEntityCount() {
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>(
				entityIndexMemcaches.size());
		for (Entry<Class<? extends IEntity<?>>, EntityIndexMemcache<?, ?>> e : entityIndexMemcaches.entrySet()) {
			Class<? extends IEntity<?>> key = e.getKey();
			EntityIndexMemcache<?, ?> value = e.getValue();
			Map<String, Integer> r = new HashMap<String, Integer>();
			try {
				int total = 0;
				Field f = value.getClass().getDeclaredField("cache");
				f.setAccessible(true);
				Map<String, Map> cache = (Map<String, Map>) f.get(value);
				for (Entry<String, Map> s : cache.entrySet()) {
					String idx = s.getKey();
					Map<?, Map> map = s.getValue();
					for (Entry<?, Map> o : map.entrySet()) {
						total += o.getValue().size();
					}
					r.put(idx, map == null ? 0 : map.size());
				}
				r.put("ENTITYS", total);
			} catch (Exception ex) {
				logger.error("", ex);
			}
			result.put(key.getName(), r);
		}
		return result;
	}
}
