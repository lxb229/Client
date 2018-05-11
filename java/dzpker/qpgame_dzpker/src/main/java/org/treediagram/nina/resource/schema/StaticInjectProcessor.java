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

package org.treediagram.nina.resource.schema;

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
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.StorageManager;
import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.Static;

/**
 * 静态注入处理器，负责完成 {@link Static} 声明的资源的注入工作
 * 
 * @author kidal
 */
public class StaticInjectProcessor extends InstantiationAwareBeanPostProcessorAdapter {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(StaticInjectProcessor.class);

	/**
	 * 注入类型定义
	 * 
	 * @author kidal
	 */
	public static enum InjectType {
		/**
		 * 存储空间
		 */
		STORAGE,

		/**
		 * 实例
		 */
		INSTANCE;
	}

	public  StaticInjectProcessor ()
	{
	}
	@Autowired
	private StorageManager manager;
	@Autowired
	private ConversionService conversionService;

	@Override
	public boolean postProcessAfterInstantiation(final Object bean, String beanName) throws BeansException {
		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				Static anno = field.getAnnotation(Static.class);
				if (anno == null) {
					return;
				}
				InjectType type = checkInjectType(field);
				switch (type) {
				case STORAGE:
					injectStorage(bean, field, anno);
					break;
				case INSTANCE:
					injectInstance(bean, field, anno);
					break;
				}
			}
		});
		return super.postProcessAfterInstantiation(bean, beanName);
	}

	/**
	 * 注入静态资源实例
	 * 
	 * @param bean 被注入对象
	 * @param field 注入属性
	 * @param anno 注入声明
	 */
	private void injectInstance(Object bean, Field field, Static anno) {
		// 获取注入资源主键
		Class<?> clz = getIdType(field.getType());
		Object key = conversionService.convert(anno.value(), clz);

		// 添加监听器
		@SuppressWarnings("rawtypes")
		Storage storage = manager.getStorage(field.getType());
		StaticObserver observer = new StaticObserver(bean, field, anno, key);
		storage.addObserver(observer);

		@SuppressWarnings("unchecked")
		Object instance = storage.get(key, false);
		if (anno.required() && instance == null) {
			FormattingTuple message = MessageFormatter.format("属性[{}]的注入值不存在", field);
			logger.debug(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}
		inject(bean, field, instance);

	}

	/**
	 * 获取唯一标识类型
	 */
	private Class<?> getIdType(Class<?> clz) {
		Field field = org.treediagram.nina.core.reflect.ReflectionUtils.getFirstDeclaredFieldWith(clz, ResourceId.class);
		return field.getType();
	}

	/**
	 * 注入存储空间对象
	 * 
	 * @param bean 被注入对象
	 * @param field 注入属性
	 * @param anno 注入声明
	 */
	@SuppressWarnings("rawtypes")
	private void injectStorage(Object bean, Field field, Static anno) {
		Type type = field.getGenericType();
		if (!(type instanceof ParameterizedType)) {
			String message = "类型声明不正确";
			logger.debug(message);
			throw new RuntimeException(message);
		}

		Type[] types = ((ParameterizedType) type).getActualTypeArguments();
		if (!(types[1] instanceof Class)) {
			String message = "类型声明不正确";
			logger.debug(message);
			throw new RuntimeException(message);
		}

		Class clz = (Class) types[1];
		Storage storage = manager.getStorage(clz);

		boolean required = anno.required();
		if (required && storage == null) 
		{
			FormattingTuple message = MessageFormatter.format("静态资源类[{}]不存在", clz);
			logger.debug(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}
		inject(bean, field, storage);
	}


	/**
	 * 注入属性值
	 */
	private void inject(Object bean, Field field, Object value) {
		ReflectionUtils.makeAccessible(field);
		try {
			field.set(bean, value);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("属性[{}]注入失败", field);
			logger.debug(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}
	}

	/**
	 * 检测注入类型
	 */
	private InjectType checkInjectType(Field field) {
		if (field.getType().equals(Storage.class)) {
			return InjectType.STORAGE;
		}
		return InjectType.INSTANCE;
	}
}
