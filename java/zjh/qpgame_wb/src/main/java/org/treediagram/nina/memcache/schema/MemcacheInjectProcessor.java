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

package org.treediagram.nina.memcache.schema;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.MemcacheManager;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.exception.MemcacheConfigurationException;
import org.treediagram.nina.memcache.service.EntityIndexMemcache;
import org.treediagram.nina.memcache.service.EntityMemcache;

/**
 * 镜像缓存注入处理器
 * 
 * @author kidalsama
 * 
 */
public class MemcacheInjectProcessor extends InstantiationAwareBeanPostProcessorAdapter {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(MemcacheInjectProcessor.class);

	/**
	 * 内存缓存服务管理器
	 */
	@Autowired
	private MemcacheManager memcacheServiceManager;
	
	public  MemcacheInjectProcessor ()
	{
		
		System.out.println("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
	}

	/**
	 * {@link InstantiationAwareBeanPostProcessorAdapter#postProcessAfterInstantiation(Object, String)}
	 */
	@Override
	public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {
		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				// 没有注解需要注入则跳过
				if (field.getAnnotation(Inject.class) == null) {
					return;
				}

				// 分类注入
				Class<?> type = field.getType();

				if (type.equals(EntityMemcache.class)) {
					injectMemcache(bean, beanName, field);
				} else if (type.equals(EntityIndexMemcache.class)) {
					injectIndexMemcache(bean, beanName, field);
				} else {
					FormattingTuple message = MessageFormatter.format("Bean[{}]的注入属性[{}]类型声明错误", beanName,
							field.getName());
					logger.error(message.getMessage());
					throw new MemcacheConfigurationException(message.getMessage());
				}
			}
		});
		return super.postProcessAfterInstantiation(bean, beanName);
	}

	/**
	 * 注入镜像缓存
	 */
	@SuppressWarnings("unchecked")
	private void injectMemcache(Object bean, String beanName, Field field) {
		// 字段
		Class<? extends IEntity<?>> entityClass = null;
		EntityMemcache<?, ?> entityMemcache = null;

		// 获取镜像缓存
		try {
			Type type = field.getGenericType();
			Type[] types = ((ParameterizedType) type).getActualTypeArguments();

			entityClass = (Class<? extends IEntity<?>>) types[1];
			entityMemcache = memcacheServiceManager.getMemcache(entityClass, true);
		} catch (Exception ex) {
			FormattingTuple message = MessageFormatter.format("Bean[{}]的注入属性[{}]类型声明错误", beanName, field.getName());
			logger.error(message.getMessage());
			throw new MemcacheConfigurationException(message.getMessage(), ex);
		}

		// 检查镜像缓存是否获取成功
		if (entityMemcache == null) {
			FormattingTuple message = MessageFormatter.format("实体[{}]缓存对象不存在", entityClass.getName());
			logger.debug(message.getMessage());
			throw new MemcacheConfigurationException(message.getMessage());
		}

		// 注入
		inject(bean, field, entityMemcache);
	}

	/**
	 * 注入镜像缓存
	 */
	@SuppressWarnings("unchecked")
	private void injectIndexMemcache(Object bean, String beanName, Field field) {
		// 字段
		Class<? extends IEntity<?>> entityClass = null;
		EntityIndexMemcache<?, ?> entityMemcache = null;

		// 获取镜像缓存
		try {
			Type type = field.getGenericType();
			Type[] types = ((ParameterizedType) type).getActualTypeArguments();

			entityClass = (Class<? extends IEntity<?>>) types[1];
			entityMemcache = memcacheServiceManager.getIndexMemcache(entityClass, true);
		} catch (Exception ex) {
			FormattingTuple message = MessageFormatter.format("Bean[{}]的注入属性[{}]类型声明错误", beanName, field.getName());
			logger.error(message.getMessage());
			throw new MemcacheConfigurationException(message.getMessage(), ex);
		}

		// 检查镜像缓存是否获取成功
		if (entityMemcache == null) {
			FormattingTuple message = MessageFormatter.format("实体[{}]缓存对象不存在", entityClass.getName());
			logger.debug(message.getMessage());
			throw new MemcacheConfigurationException(message.getMessage());
		}

		// 注入
		inject(bean, field, entityMemcache);
	}

	/**
	 * 注入
	 */
	private void inject(Object bean, Field field, Object value) {
		ReflectionUtils.makeAccessible(field);
		try {
			field.set(bean, value);
		} catch (Exception ex) {
			FormattingTuple message = MessageFormatter.format("属性[{}]注入失败", field);
			logger.debug(message.getMessage());
			throw new MemcacheConfigurationException(message.getMessage());
		}
	}
}
