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

package org.treediagram.nina.network.client;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

/**
 * 客户端配置
 * 
 * @author kidal
 * 
 */
public class ClientConfig implements ApplicationContextAware {
	/**
	 * 读取缓存大小设置
	 */
	private static final String KEY_BUFFER_READ = "client.socket.buffer.read";

	/**
	 * 写入缓存大小设置
	 */
	private static final String KEY_BUFFER_WRITE = "client.socket.buffer.write";

	/**
	 * 最大重试次数
	 */
	private static final String KEY_MAX_RETRY = "client.socket.maxRetry";

	/**
	 * 连接超时设置
	 */
	private static final String KEY_CONNECT_TIMEOUT_MILLIS = "client.socket.connectTimeoutMillis";

	/**
	 * 应答超时设置
	 */
	private static final String KEY_RESPONSE_TIMEOUT_MILLIS = "client.socket.responseTimeoutMillis";

	/**
	 * 移除时间设置
	 */
	private static final String KEY_REMOVE_TIME_MILLIS = "client.socket.removeTimeMillis";

	/**
	 * 必须的配置键
	 */
	private static final String[] KEYS = { KEY_BUFFER_READ, KEY_BUFFER_WRITE };

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(ClientConfig.class);

	/**
	 * 程序上下文
	 */
	private ApplicationContext applicationContext;

	/**
	 * 配置文件位置
	 */
	private String location;

	/**
	 * 配置
	 */
	private Properties properties;

	/**
	 * 读取缓存
	 */
	private int bufferRead = 2048;

	/**
	 * 读取缓存
	 */
	private int bufferWrite = 2048;

	/**
	 * 最大连接尝试次数
	 */
	private int maxRetry = 10;

	/**
	 * 连接超时毫秒
	 */
	private int connectTimeoutMillis = 5000;

	/**
	 * 应答超时毫秒
	 */
	private int responseTimeoutMillis = 5000;

	/**
	 * 客户端过期超时毫秒
	 */
	private int removeTimeMillis = 300000;

	/**
	 * {@link ApplicationContextAware#setApplicationContext(ApplicationContext)}
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 初始化
	 */
	@PostConstruct
	protected void init() {
		// 载入资源
		Resource resource = applicationContext.getResource(location);

		if (resource == null) {
			FormattingTuple message = MessageFormatter.format("客户端配置[{}]不存在", location);
			logger.error(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}

		// 读取属性
		properties = new Properties();

		try {
			properties.load(resource.getInputStream());
		} catch (IOException e) {
			FormattingTuple message = MessageFormatter.format("客户端资源[{}]加载失败", location);
			logger.error(message.getMessage(), e);
			throw new RuntimeException(message.getMessage(), e);
		}

		// 检查配置是否完整
		for (String key : KEYS) {
			if (!properties.containsKey(key)) {
				FormattingTuple message = MessageFormatter.format("客户端配置缺失，配置键[{}]", key);
				logger.error(message.getMessage());
				throw new RuntimeException(message.getMessage());
			}
		}

		// 读取配置
		String value;

		value = properties.getProperty(KEY_BUFFER_READ);
		bufferRead = Integer.valueOf(value);
		value = properties.getProperty(KEY_BUFFER_WRITE);
		bufferWrite = Integer.valueOf(value);

		value = properties.getProperty(KEY_MAX_RETRY);
		if (StringUtils.isNotBlank(value)) {
			maxRetry = Integer.valueOf(value);
		}
		value = properties.getProperty(KEY_CONNECT_TIMEOUT_MILLIS);
		if (StringUtils.isNotBlank(value)) {
			connectTimeoutMillis = Integer.valueOf(value);
		}
		value = properties.getProperty(KEY_RESPONSE_TIMEOUT_MILLIS);
		if (StringUtils.isNotBlank(value)) {
			responseTimeoutMillis = Integer.valueOf(value);
		}
		value = properties.getProperty(KEY_REMOVE_TIME_MILLIS);
		if (StringUtils.isNotBlank(value)) {
			removeTimeMillis = Integer.valueOf(value);
		}
	}

	// getters & setters

	public void setLocation(String location) {
		this.location = location;
	}

	public int getBufferRead() {
		return bufferRead;
	}

	public int getBufferWrite() {
		return bufferWrite;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public int getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}

	public int getResponseTimeoutMillis() {
		return responseTimeoutMillis;
	}

	public int getRemoveTimeMillis() {
		return removeTimeMillis;
	}
}
