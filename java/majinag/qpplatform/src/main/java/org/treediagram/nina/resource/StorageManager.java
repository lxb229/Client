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

package org.treediagram.nina.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.treediagram.nina.resource.other.ResourceDefinition;

/**
 * 资源管理器
 * 
 * @author kidal
 */
@SuppressWarnings("rawtypes")
public class StorageManager implements ApplicationContextAware {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(StorageManager.class);

	/**
	 * 静态类资源定义
	 */
	private ConcurrentHashMap<Class, ResourceDefinition> definitions = new ConcurrentHashMap<Class, ResourceDefinition>();

	/**
	 * 资源存储空间
	 */
	private ConcurrentHashMap<Class<?>, Storage<?, ?>> storages = new ConcurrentHashMap<Class<?>, Storage<?, ?>>();

	/**
	 * 初始化静态类资源
	 * 
	 * @param definition 资源定义
	 */
	public void initialize(ResourceDefinition definition) {
		Class<?> clz = definition.getClz();
		if (definitions.putIfAbsent(clz, definition) != null) {
			ResourceDefinition prev = definitions.get(clz);
			FormattingTuple message = MessageFormatter.format("类[{}]的资源定义[{}]已经存在", clz, prev);
			logger.error(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}
		initializeStorage(clz);
	}

	/**
	 * 验证静态类资源
	 * 
	 * @param definition 资源定义
	 */
	public void validate(ResourceDefinition definition) {
		Class<?> clz = definition.getClz();
		Storage<?, ?> storage = getStorage(clz);
		storage.validate();
	}

	/**
	 * 重新加载静态类资源
	 * 
	 * @param clz 要重新加载的类资源
	 */
	public void reload(Class<?> clz) {
		ResourceDefinition definition = definitions.get(clz);
		if (definition == null) {
			FormattingTuple message = MessageFormatter.format("类[{}]的资源定义不存在", clz);
			logger.error(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}

		Storage<?, ?> storage = getStorage(clz);
		storage.reload();
	}

	@SuppressWarnings("unchecked")
	public <T> T getResource(Object key, Class<T> clz) {
		Storage storage = getStorage(clz);
		return (T) storage.get(key, false);
	}

	/**
	 * 获取指定类资源的存储空间
	 * 
	 * @param clz 类实例
	 */
	public Storage<?, ?> getStorage(Class clz) {
		if (storages.containsKey(clz)) {
			return storages.get(clz);
		}
		return initializeStorage(clz);
	}

	/**
	 * 获取全部的存储空间对象数组
	 */
	public Storage<?, ?>[] listStorages() {
		List<Storage<?, ?>> storages = new ArrayList<Storage<?, ?>>();
		for (Storage<?, ?> storage : this.storages.values()) {
			storages.add(storage);
		}
		return storages.toArray(new Storage[0]);
	}

	/**
	 * 初始化类资源的存储空间
	 * 
	 * @param clz 类实例
	 */
	private Storage initializeStorage(Class clz) {
		ResourceDefinition definition = this.definitions.get(clz);
		if (definition == null) {
			FormattingTuple message = MessageFormatter.format("静态资源[{}]的信息定义不存在，可能是配置缺失", clz.getSimpleName());
			logger.error(message.getMessage());
			throw new IllegalStateException(message.getMessage());
		}
		AutowireCapableBeanFactory beanFactory = this.applicationContext.getAutowireCapableBeanFactory();
		Storage storage = beanFactory.createBean(Storage.class);

		Storage prev = storages.putIfAbsent(clz, storage);
		if (prev == null) {
			storage.initialize(definition);
		}
		return prev == null ? storage : prev;
	}

	// 实现接口的方法

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
