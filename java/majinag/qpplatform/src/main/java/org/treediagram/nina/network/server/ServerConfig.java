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

package org.treediagram.nina.network.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.integration.beans.InetSocketAddressEditor;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.treediagram.nina.core.thread.NamedThreadFactory;

/**
 * 服务器配置
 * 
 * @author kidal
 * 
 */
public class ServerConfig implements ApplicationContextAware {
	/**
	 * 分隔符
	 */
	private static final String SPLIT_REGEX = ",";

	/**
	 * 服务器的地址与端口配置，允许通过分隔符","指定多个地址
	 */
	private static final String KEY_ADDRESS = "server.socket.address";

	/**
	 * 读取缓存大小设置
	 */
	private static final String KEY_BUFFER_READ = "server.socket.buffer.read";

	/**
	 * 写入缓存大小设置
	 */
	private static final String KEY_BUFFER_WRITE = "server.socket.buffer.write";

	/**
	 * 连接超时设置
	 */
	private static final String KEY_TIMEOUT = "server.socket.timeout";

	/**
	 * 最小线程数设置
	 */
	private static final String KEY_POOL_MIN = "server.socket.pool.min";

	/**
	 * 最大线程数设置
	 */
	private static final String KEY_POOL_MAX = "server.socket.pool.max";

	/**
	 * 线程空闲时间设置，单位:millisecond
	 */
	private static final String KEY_POOL_IDLE = "server.socket.pool.idle";

	/**
	 * 设置服务器是否自动启动
	 */
	private static final String KEY_AUTO_START = "server.config.auto_start";

	/**
	 * 服务器的jetty地址与端口配置，允许通过分隔符","指定多个地址
	 */
	private static final String KEY_JETTY_ADDRESS = "server.socket.jetty.address";

	/**
	 * 服务器的jetty白名单
	 */
	private static final String KEY_JETTY_WHITE_IPS = "server.socket.jetty.whiteips";

	/**
	 * 游戏平台地址
	 * */
	private static final String KEY_PLATFORM_ADDRESS = "platform.address";
	
	/**
	 * 必须的配置键
	 */
	private static final String[] KEYS = { KEY_ADDRESS, KEY_BUFFER_READ, KEY_BUFFER_WRITE, KEY_TIMEOUT, KEY_POOL_MIN,
			KEY_POOL_MAX, KEY_POOL_IDLE, KEY_PLATFORM_ADDRESS};

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(ServerConfig.class);

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
	 * 绑定地址
	 */
	private List<InetSocketAddress> addresses;

	/**
	 * 会话配置
	 */
	private SocketSessionConfig sessionConfig;

	/**
	 * 执行器最小线程数
	 */
	private int executorMin;

	/**
	 * 执行器最大线程数
	 */
	private int executorMax;

	/**
	 * 执行器线程空闲时间
	 */
	private long executorIdle;

	/**
	 * 是否自动启动服务器
	 */
	private boolean autoStart;

	/**
	 * jetty地址
	 */
	private String jettyAddress;

	/**
	 * jetty白名单
	 */
	private Map<String, Pattern> jettyWhiteIps = new HashMap<String, Pattern>();
	
	/**
	 * 游戏服向登陆服验证地址
	 * */
	private String platformAddress;
	

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
			FormattingTuple message = MessageFormatter.format("服务器配置 {} 不存在", location);
			logger.error(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}

		// 读取属性
		properties = new Properties();

		try {
			properties.load(resource.getInputStream());
		} catch (IOException e) {
			FormattingTuple message = MessageFormatter.format("资源 {} 加载失败", location);
			logger.error(message.getMessage(), e);
			throw new RuntimeException(message.getMessage(), e);
		}

		// 检查配置是否完整
		for (String key : KEYS) {
			if (!properties.containsKey(key)) {
				FormattingTuple message = MessageFormatter.format("配置缺失，配置键 {}", key);
				logger.error(message.getMessage());
				throw new RuntimeException(message.getMessage());
			}
		}

		// 初始化主要配置
		initAddress();
		initExecutor();
		initSession();

		// 初始化可选配置
		String value = properties.getProperty(KEY_AUTO_START);
		if (value != null) {
			autoStart = Boolean.valueOf(value);
		}

		initOptionalJetty();
	}

	/**
	 * 初始化地址配置
	 */
	private void initAddress() {
		addresses = new ArrayList<InetSocketAddress>();

		String value = properties.getProperty(KEY_ADDRESS);
		InetSocketAddressEditor addressEditor = new InetSocketAddressEditor();

		for (String s : value.split(SPLIT_REGEX)) {
			addressEditor.setAsText(s);
			addresses.add((InetSocketAddress) addressEditor.getValue());
		}
		platformAddress = properties.getProperty(KEY_PLATFORM_ADDRESS);
	}

	/**
	 * 初始化执行线程池配置
	 */
	private void initExecutor() {
		String min = properties.getProperty(KEY_POOL_MIN);
		String max = properties.getProperty(KEY_POOL_MAX);
		String idel = properties.getProperty(KEY_POOL_IDLE);

		executorMin = Integer.parseInt(min);
		executorMax = Integer.parseInt(max);
		executorIdle = Long.parseLong(idel);
	}

	/**
	 * 初始化会话配置
	 */
	private void initSession() {
		sessionConfig = new DefaultSocketSessionConfig();

		String value = properties.getProperty(KEY_BUFFER_READ);
		sessionConfig.setReadBufferSize(Integer.parseInt(value));

		value = properties.getProperty(KEY_BUFFER_WRITE);
		sessionConfig.setWriteTimeout(Integer.parseInt(value));

		value = properties.getProperty(KEY_TIMEOUT);
		sessionConfig.setBothIdleTime(Integer.parseInt(value));
	}

	/**
	 * 初始化可选的jetty配置
	 */
	private void initOptionalJetty() {
		String value = properties.getProperty(KEY_JETTY_ADDRESS);
		if (value != null) {
			jettyAddress = value;
		}

		value = properties.getProperty(KEY_JETTY_WHITE_IPS);
		if (StringUtils.isNotBlank(value)) {
			String[] values = value.split(",");
			for (String pairString : values) {
				String[] nameIp = pairString.split("=");
				if (nameIp.length != 2) {
					FormattingTuple message = MessageFormatter.format("配置错误，配置键 {}", KEY_JETTY_WHITE_IPS);
					logger.error(message.getMessage());
					throw new RuntimeException(message.getMessage());
				}
				String reg = nameIp[1].replace(".", "[.]").replace("*", "[0-9]*");
				Pattern pattern = Pattern.compile(reg);
				jettyWhiteIps.put(nameIp[0], pattern);
			}
		}
	}

	// getters & setters

	public void setLocation(String location) {
		this.location = location;
	}

	public List<InetSocketAddress> getAddresses() {
		return addresses;
	}

	public SocketSessionConfig getSessionConfig() {
		return sessionConfig;
	}

	public boolean isAutoStart() {
		return autoStart;
	}

	public ExecutorFilter getExecutorFilter() {
		ThreadGroup group = new ThreadGroup("通信模块");
		NamedThreadFactory threadFactory = new NamedThreadFactory(group, "通信线程");
		return new ExecutorFilter(executorMin, executorMax, executorIdle, TimeUnit.MILLISECONDS, threadFactory);
	}

	public String getJettyAddress() {
		return jettyAddress;
	}

	public Map<String, Pattern> getJettyWhiteIps() {
		return jettyWhiteIps;
	}

	public String getPlatformAddress() {
		return platformAddress;
	}

	public void setPlatformAddress(String platformAddress) {
		this.platformAddress = platformAddress;
	}

		
}
