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

import java.util.Observable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.core.convert.ConversionService;

/**
 * 配置的值
 * 
 * @author kidal
 *
 */
public class ConfigValue<T> extends Observable {
	/** 同步锁 */
	private final Lock lock = new ReentrantLock();
	/** 转换服务 */
	private final ConversionService conversionService;
	/** 配置服务 */
	private final ConfigService configurationService;
	/** 配置键 */
	private final String key;
	/** 配置的值类型 */
	private final Class<?> valueType;

	/** 值 */
	private T value;

	/**
	 * 创建配置值
	 */
	ConfigValue(ConversionService conversionService, ConfigService configurationService, String key,
			Class<?> valueType) {
		this.conversionService = conversionService;
		this.configurationService = configurationService;
		this.key = key;
		this.valueType = valueType;
	}

	/**
	 * 获取配置的值
	 */
	public T get() {
		if (value == null) {
			lock.lock();
			try {
				if (value == null) {
					final Object rawValue = configurationService.get(key);
					if (rawValue != null) {
						value = convertRawValue(rawValue);
					}
				}
			} finally {
				lock.unlock();
			}
		}
		return value;
	}

	/**
	 * 设置配置的值
	 * 
	 * @param newValue 新的值
	 * @throws ConfigurationException 配置写入异常时
	 */
	public void set(T newValue) throws ConfigurationException {
		lock.lock();
		try {
			if (newValue != value) {
				final T oldValue = value;
				configurationService.set(key, newValue);
				value = newValue;

				setChanged();
				notifyObservers(oldValue);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 转换纯值
	 */
	@SuppressWarnings("unchecked")
	private T convertRawValue(Object rawValue) {
		return (T) conversionService.convert(rawValue, valueType);
	}

	/**
	 * {@link Object#toString()}
	 */
	@Override
	public String toString() {
		return key + "=" + String.valueOf(get());
	}
}
