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

package org.treediagram.nina.core.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import org.treediagram.nina.core.configuration.annotation.Config;
import org.treediagram.nina.core.io.IOUtils;
import org.treediagram.nina.core.reflect.ReflectionUtils;

/**
 * 配置服务
 * 
 * @author kidal
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConfigService extends InstantiationAwareBeanPostProcessorAdapter {
	/**
	 * Nina配置文件
	 */
	public static final String NINA_PROPERTIES = "nina.properties";

	/** 转换服务 */
	@Autowired
	private ConversionService conversionService;

	/** Properties配置操作器 */
	private PropertiesConfiguration propertiesConfiguration;
	/** Apache综合配置操作器 */
	private CompositeConfiguration compositConfiguration;

	/** 已经注入的全部值 */
	private final Map<String, ConfigValue> configurationValues = new HashMap<String, ConfigValue>();

	/**
	 * 初始化
	 */
	@PostConstruct
	protected void init() throws ConfigurationException {
		propertiesConfiguration = new PropertiesConfiguration(IOUtils.newNinaFile(NINA_PROPERTIES));
		propertiesConfiguration.setAutoSave(true);

		compositConfiguration = new CompositeConfiguration();
		compositConfiguration.addConfiguration(propertiesConfiguration);
	}

	/**
	 * 载入只读属性配置
	 * 
	 * @param fileName 配置文件名
	 * @throws ConfigurationException 读取配置时发生异常
	 */
	public void loadReadonlyProperties(String fileName) throws ConfigurationException {
		final PropertiesConfiguration loadedReadonlyProperties = new PropertiesConfiguration();
		loadedReadonlyProperties.setEncoding("UTF-8");
		loadedReadonlyProperties.setFileName(fileName);
		loadedReadonlyProperties.load();
		loadedReadonlyProperties.setFileName(null);

		compositConfiguration.addConfiguration(loadedReadonlyProperties);
	}

	/**
	 * 读取配置值
	 * 
	 * @param key 配置键
	 * @return 如果配置键对应的值存在则返回配置的值;否则返回null
	 */
	public Object get(String key) {
		return compositConfiguration.getProperty(key);
	}

	/**
	 * 获取字符串数据配置
	 * 
	 * @param key 配置键
	 * @return 如果配置键对应的值存在则返回配置的值;否则返回null
	 */
	public String[] getStringArray(String key) {
		return compositConfiguration.getStringArray(key);
	}

	/**
	 * 设置配置值
	 * 
	 * @param key 配置键
	 * @param value 配置值
	 * @throws ConfigurationException 保存配置时发生异常
	 */
	public void set(String key, Object value) throws ConfigurationException {
		propertiesConfiguration.setProperty(key, value);
	}

	/**
	 * {@link InstantiationAwareBeanPostProcessorAdapter#postProcessAfterInstantiation(Object, String)}
	 */
	@Override
	public boolean postProcessAfterInstantiation(final Object bean, String beanName) throws BeansException {
		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				final Config configuration = field.getAnnotation(Config.class);
				if (configuration != null) {
					doInject(configuration, bean, field);
				}
			}
		}, new FieldFilter() {
			@Override
			public boolean matches(Field field) {
				return field.getType() == ConfigValue.class;
			}
		});
		return true;
	}

	/**
	 * 执行注入
	 */
	private void doInject(Config configuration, Object bean, Field field) throws IllegalArgumentException,
			IllegalAccessException {
		final String key = configuration.value();
		final Class<?> valueType = valueTypeOfField(configuration, field);

		ConfigValue<?> configurationValue = configurationValues.get(key);
		if (configurationValue == null) {
			configurationValue = new ConfigValue(conversionService, this, key, valueType);
			configurationValues.put(key, configurationValue);
		}

		ReflectionUtils.makeAccessible(field);
		field.set(bean, configurationValue);
	}

	/**
	 * 获取字段的配置值配型
	 */
	private Class<?> valueTypeOfField(Config configuration, Field field) {
		Class<?> valueType = configuration.valueType();

		if (valueType.equals(Object.class)) {
			final Type type = field.getGenericType();
			final Type[] types = ((ParameterizedType) type).getActualTypeArguments();

			valueType = (Class<?>) types[0];
		}

		return valueType;
	}
}
