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

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.treediagram.nina.network.codec.MessageDecoder;
import org.treediagram.nina.network.codec.MessageEncoder;
import org.treediagram.nina.network.codec.WSMessageDecoder;
import org.treediagram.nina.network.codec.WSMessageEncoder;
import org.treediagram.nina.network.handler.JettyHandler;
import org.treediagram.nina.network.handler.JettyRequestHandler;
import org.treediagram.nina.network.handler.ServerHandler;

/**
 * 服务器
 * 
 * @author kidal
 * 
 */
public class SocketServer implements ApplicationListener<ApplicationEvent>, ApplicationContextAware {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

	/**
	 * 应用程序上下文
	 */
	private ApplicationContext applicationContext;

	/**
	 * 服务器配置
	 */
	private ServerConfig config;

	/**
	 * 接收器
	 */
	private SocketAcceptor acceptor;

	/**
	 * 处理器
	 */
	private ServerHandler handler;

	/**
	 * 过滤器
	 */
	private Map<String, IoFilter> filters;

	/**
	 * 执行过滤器
	 */
	private ExecutorFilter executorFilter;

	/**
	 * jetty服务器
	 */
	private Server jettyServer;

	/**
	 * jetty请求处理器
	 */
	private Map<String, JettyRequestHandler> jettyRequestHandlers;

	/**
	 * 初始化
	 */
	@PostConstruct
	protected void init() {
		// 设置会话配置
		acceptor.getSessionConfig().setAll(config.getSessionConfig());

		// 设置处理器
		acceptor.setHandler(handler);

		// 设置过滤器
		if (filters != null) {
			for (Entry<String, IoFilter> entry : filters.entrySet()) {
				acceptor.getFilterChain().addLast(entry.getKey(), entry.getValue());
			}
		}

		// 设置编码解码过滤器
		//acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageEncoder(), new MessageDecoder()));
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new WSMessageEncoder(), new WSMessageDecoder()));
		
		// 设置处理线程池
		executorFilter = config.getExecutorFilter();
		acceptor.getFilterChain().addLast("executor", executorFilter);
		// jetty处理器
		initJetty();
	}

	/**
	 * 初始化jetty
	 */
	private void initJetty() {
		String jettyAddress = config.getJettyAddress();
		if (jettyAddress == null) {
			return;
		}

		String[] jettyAddresses = jettyAddress.split(",");
		if (jettyAddress.length() < 1) {
			throw new IllegalStateException("无效的jettyAddress[" + jettyAddress + "]");
		}

		// 获取请求处理器
		jettyRequestHandlers = new HashMap<String, JettyRequestHandler>();
		for (JettyRequestHandler handler : applicationContext.getBeansOfType(JettyRequestHandler.class).values()) {
			final String path = handler.getPath();
			if (jettyRequestHandlers.containsKey(path)) {
				throw new IllegalStateException("重复注册jetty请求处理器路径[" + path + "]");
			}
			jettyRequestHandlers.put(path, handler);
		}

		// 创建jetty服务器
		jettyServer = new Server();
		jettyServer.setHandler(new JettyHandler(handler, config.getJettyWhiteIps(), jettyRequestHandlers));

		for (String ja : jettyAddresses) {
			// 解析host和port
			final String[] hostAndPortArray = ja.split(":");
			if (hostAndPortArray.length != 2) {
				throw new IllegalStateException("无效的jettyAddress[" + jettyAddress + "]");
			}
			final String host = hostAndPortArray[0];
			final int port = Integer.valueOf(hostAndPortArray[1]).intValue();

			// 创建连接器
			SelectChannelConnector connector = new SelectChannelConnector();
			if (StringUtils.isNotBlank(host)) {
				connector.setHost(host);
			}
			connector.setPort(port);

			// 添加连接器
			jettyServer.addConnector(connector);
		}
	}

	/**
	 * {@link ApplicationListener#onApplicationEvent(ApplicationEvent)}
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			if (config.isAutoStart() && !acceptor.isActive()) {
				start();
			}
		} else if (event instanceof ContextClosedEvent) {
			close();
		}
	}
	
	/**
	 * 开启服务器
	 */
	public void start() {
		if (logger.isErrorEnabled()) {
			logger.debug("开启服务器");
		}
		
		try {
			// socket
			acceptor.setReuseAddress(true);

			for (InetSocketAddress address : config.getAddresses()) {
				acceptor.bind(address);
				logger.info("绑定服务器地址和端口到 {}:{}", address.getHostName(), address.getPort());
			}
						
			// jetty
			if (jettyServer != null) {
				jettyServer.start();

				if (logger.isInfoEnabled()) {
					Connector[] connectors = jettyServer.getConnectors();
					for (Connector connector : connectors) {
						final String host = (connector.getHost() == null) ? "0.0.0.0" : connector.getHost();
						final int port = connector.getPort();

						logger.info("绑定jetty服务器地址和端口到 {}:{}", host, port);
					}
				}
			}
		} catch (Exception e) {
			logger.error("启动服务器失败", e);
			throw new RuntimeException("启动服务器失败", e);
		}
	}

	/**
	 * 关闭服务器
	 */
	public void close() {
		// log
		logger.info("关闭服务器");

		// socket
		acceptor.unbind();
		acceptor.dispose(false);
		executorFilter.destroy();
		
		// jetty
		if (jettyServer != null) {
			try {
				jettyServer.stop();
			} catch (Exception e) {
				logger.error("关闭jetty服务器失败", e);
			}
		}
	}

	/**
	 * {@link ApplicationContextAware#setApplicationContext(ApplicationContext)}
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	// setters

	public void setConfig(ServerConfig config) {
		this.config = config;
	}

	public void setAcceptor(SocketAcceptor acceptor) {
		this.acceptor = acceptor;
	}

	public void setHandler(ServerHandler handler) {
		this.handler = handler;
	}

	public void setFilters(Map<String, IoFilter> filters) {
		this.filters = filters;
	}
	
}
