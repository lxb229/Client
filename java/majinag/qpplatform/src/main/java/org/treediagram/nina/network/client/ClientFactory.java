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

import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.treediagram.nina.core.concurrent.DelayedElement;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.codec.Message;
import org.treediagram.nina.network.codec.MessageDecoder;
import org.treediagram.nina.network.codec.MessageEncoder;
import org.treediagram.nina.network.exception.SocketException;
import org.treediagram.nina.network.exception.SocketTimeoutException;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Response;

/**
 * 客户端工厂
 * 
 * @author kidal
 * 
 */
public class ClientFactory {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(ClientFactory.class);

	/**
	 * 可用的客户端
	 */
	private final ConcurrentMap<String, SimpleClient> clients = new ConcurrentHashMap<String, SimpleClient>();

	/**
	 * 客户端操作
	 */
	private final ConcurrentMap<String, Lock> locks = new ConcurrentHashMap<String, Lock>();

	/**
	 * 过期客户端删除队列
	 */
	private final DelayQueue<DelayedElement<? extends Client>> removeQueue = new DelayQueue<DelayedElement<? extends Client>>();

	/**
	 * 客户端配置
	 */
	private ClientConfig config;

	/**
	 * 初始化
	 */
	@PostConstruct
	protected void init() {
		// 启动客户端清理线程
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						DelayedElement<? extends Client> e = removeQueue.take();
						Client client = e.getContent();
						if (client == null || client.isDisposed()) {
							continue; // 客户端已经不存在
						}
						String key = client.getAddress();
						if (!client.isKeepAlive()) {
							Lock lock = loadClientLock(key);
							lock.lock();
							try {
								// 检查是否超时没有活动
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(client.getTimestamp());
								calendar.add(Calendar.MILLISECOND, config.getRemoveTimeMillis());
								if (calendar.getTime().before(new Date())) {
									clients.remove(key);
									client.close();
									continue;
								}
							} finally {
								lock.unlock();
							}
						}

						// 客户端还处于活跃状态，延时再进行检查
						Calendar calendar = Calendar.getInstance();
						calendar.add(Calendar.MILLISECOND, config.getRemoveTimeMillis());
						DelayedElement<Client> element = DelayedElement.valueOf(client, calendar.getTime());
						removeQueue.put(element);
					} catch (InterruptedException e) {
						logger.error("过期客户端清理线程被打断", e);
					} catch (Exception e) {
						logger.error("过期客户端清理线程出现未知异常", e);
					}
				}
			}
		}, "过期客户端清理");
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * 销毁
	 */
	@PreDestroy
	protected void destroy() {
		for (Client client : clients.values()) {
			client.close();
		}
	}

	/**
	 * 创建客户端
	 */
	public Client createClient(String host, int port, boolean keepAlive) {
		// 创建客户端
		InetSocketAddress address = new InetSocketAddress(host, port);
		SimpleClient client = new SimpleClient(address);

		// 保持连接
		if (keepAlive) {
			client.keepAlive = keepAlive;
		}

		// 完成
		return client;
	}

	/**
	 * 获取客户端
	 */
	public Client getClient(String host, int port, boolean keepAlive) {
		InetSocketAddress address = new InetSocketAddress(host, port);
		return getClient(address, keepAlive);
	}

	/**
	 * 获取客户端
	 */
	public Client getClient(InetSocketAddress address, boolean keepAlive) {
		// 锁定
		String key = address.getAddress().getHostAddress() + ":" + address.getPort();
		Lock lock = loadClientLock(key);
		lock.lock();

		try {
			// 获取客户端
			SimpleClient client = clients.get(key);

			// 如果客户端无效则重置客户端，否则创建新的客户端
			if (client != null && !client.isDisposed()) {
				client.reset();
			} else {
				// 创建新的客户端
				client = new SimpleClient(address);

				// 放入字典
				SimpleClient prevClient = clients.putIfAbsent(key, client);
				if (prevClient != null) {
					// 已经存在客户端
					// 客户端超过最大充实次数则替换，否则使用存在的客户端
					if (prevClient.retry > config.getMaxRetry()) {
						clients.replace(key, prevClient, client);
					} else {
						client = prevClient;
					}
				} else {
					// 不存在客户端，设置客户端移除时间
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.MILLISECOND, config.getRemoveTimeMillis());
					removeQueue.put(DelayedElement.valueOf(client, calendar.getTime()));
				}
			}

			// 保持连接
			if (keepAlive) {
				client.keepAlive = keepAlive;
			}

			// 完成
			return client;
		} catch (Exception e) {
			// 移除锁
			removeClientLock(key);

			// 报错
			FormattingTuple message = MessageFormatter.format("无法获取指定服务器地址的客户端对象[{}]", new Object[] { address, e });
			logger.error(message.getMessage());
			throw new SocketException(message.getMessage(), e);
		} finally {
			// 解锁
			lock.unlock();
		}
	}

	/**
	 * 载入客户端操作锁
	 */
	private Lock loadClientLock(String key) {
		Lock lock = locks.get(key);
		if (lock == null) {
			lock = new ReentrantLock();
			Lock prevLock = locks.putIfAbsent(key, lock);
			lock = prevLock != null ? prevLock : lock;
		}
		return lock;
	}

	/**
	 * 移除客户端操作锁
	 */
	private void removeClientLock(String key) {
		locks.remove(key);
	}

	/**
	 * 简单的客户端
	 * 
	 * @author kidal
	 * 
	 */
	private class SimpleClient implements Client {
		/**
		 * 远程服务器地址
		 */
		private final InetSocketAddress address;

		/**
		 * 连接器
		 */
		private final NioSocketConnector connector;

		/**
		 * 当前与服务器连接的会话
		 */
		private IoSession session;

		/**
		 * 时间戳
		 */
		private Date timestamp = new Date();

		/**
		 * 当前重试次数
		 */
		private int retry = 0;

		/**
		 * 保持连接
		 */
		private boolean keepAlive = false;

		/**
		 * 创建简单的客户端
		 */
		public SimpleClient(InetSocketAddress address) {
			// 保存和创建参数
			this.address = address;
			this.connector = new NioSocketConnector();

			// 设置连接超时
			this.connector.setConnectTimeoutMillis(config.getConnectTimeoutMillis());

			// 设置编码解码器
			ProtocolCodecFilter codec = new ProtocolCodecFilter(new MessageEncoder(), new MessageDecoder());
			this.connector.getFilterChain().addLast("codec", codec);

			// 设置配置
			SocketSessionConfig sessionConfig = this.connector.getSessionConfig();
			sessionConfig.setUseReadOperation(true);
			sessionConfig.setReadBufferSize(config.getBufferRead());
			sessionConfig.setWriteTimeout(config.getBufferWrite());
		}

		/**
		 * 重置
		 */
		public void reset() {
			timestamp = new Date();
		}

		/**
		 * {@link Client#send(Request)}
		 */
		@Override
		public <T> Response<T> send(Request<?> request) {
			return send(request, null, 0);
		}

		/**
		 * {@link Client#send(Request, Class)}
		 */
		@Override
		public <T> Response<T> send(Request<?> request, Class<T> responseClass) {
			return send(request, responseClass, 0);
		}

		/**
		 * {@link Client#send(Request, Class, int)}
		 */
		@Override
		@SuppressWarnings("unchecked")
		public <T> Response<T> send(Request<?> request, Class<T> responseClass, int timeoutMillis) {
			// 连接
			if (!isConnected()) {
				connect();
			}

			// 获取信息
			request.setTimestamp((int) getTimestamp().getTime());

			int id = request.getId();
			int ts = request.getTimestamp();
			Object body = request.getBody();

			// 转换消息体
			String bodyJson = null;
			if (body == null) {
				bodyJson = "{}";
			} else {
				bodyJson = JsonUtils.object2String(body);
			}
			byte[] bodyBytes = bodyJson.getBytes();
			Message message = new Message(request.getCode(), 0, id, ts, bodyBytes, null);

			// 发送信息
			WriteFuture writeFuture = session.write(message);
			writeFuture.awaitUninterruptibly();

			// 修正超时
			timeoutMillis = timeoutMillis <= 0 ? config.getResponseTimeoutMillis() : timeoutMillis;

			// 读取信息
			int readRetry = 0;
			ReadFuture readFuture = session.read();
			while (readFuture.awaitUninterruptibly(timeoutMillis, TimeUnit.MILLISECONDS)) {
				// 获取答复字节
				Object repObj = readFuture.getMessage();

				// 空答复视为超时
				if (repObj == null) {
					String responseClassName = responseClass == null ? HashMap.class.getName() : responseClass
							.getName();
					throw new SocketTimeoutException("消息[" + id + "]等待答复[" + responseClassName + "]超时");
				}

				// 验证编号和时间戳
				Message repMsg = (Message) repObj;
				int repts = repMsg.getTimestamp();
				int repid = repMsg.getId();
				if (ts != repts || id != repid) {
					// 重试10次
					if (readRetry > 10) {
						break;
					}

					// 再次重试
					readRetry++;
					readFuture = session.read();
					continue;
				}

				// 解析答复
				byte[] repCompressedBody = repMsg.getBody();
				byte[] repBodyBytes = repCompressedBody;// ZipUtils.decompress(repCompressedBody);
				String repBodyString = new String(repBodyBytes);
				T repBody = null;
				try {
					if (responseClass == null) {
						repBody = (T) JsonUtils.string2Map(repBodyString);
					} else {
						repBody = JsonUtils.string2Object(repBodyString, responseClass);
					}
				} catch (Exception e) {
					throw new SocketException("将[" + repBodyString + "[转换为答复[" + responseClass.getName() + "]错误", e);
				}
				Response<T> response = new Response<T>();
				response.setTimestamp(repts);
				response.setId(repid);
				response.setBody(repBody);
				return response;
			}

			String responseClassName = responseClass == null ? Map.class.getName() : responseClass.getName();
			throw new SocketTimeoutException("消息[" + id + "]等待答复[" + responseClassName + "]超时");
		}

		/**
		 * {@link Client#close()}
		 */
		@Override
		public synchronized void close() {
			// 不重复关闭客户端
			if (session == null || session.isClosing() || !session.isConnected()) {
				// 超过最大尝试次数后释放资源
				if (retry > config.getMaxRetry()) {
					connector.dispose();

					// 日志
					if (logger.isDebugEnabled()) {
						logger.debug("与服务器[{}]连接重试过多[{}]", address, retry);
					}
				}
				return;
			}

			// 日志
			if (logger.isDebugEnabled()) {
				logger.debug("开始关闭与服务器[{}]的连接", address);
			}

			// 优雅关闭客户端
			CloseFuture close = session.close(false);
			close.awaitUninterruptibly();

			// 释放资源
			connector.dispose();

			// 日志
			if (logger.isDebugEnabled()) {
				logger.debug("已经关闭与服务器[{}]的连接", address);
			}
		}

		/**
		 * {@link Client#connect()}
		 */
		@Override
		public synchronized void connect() {
			// 已经连接则不重复连接
			if (session != null && session.isConnected()) {
				return;
			}

			// 日志
			if (logger.isDebugEnabled()) {
				logger.debug("开始连接服务器[{}]", address);
			}

			// 尝试次数增加
			retry++;

			try {
				// 连接
				ConnectFuture connectFuture = connector.connect(address);
				connectFuture.awaitUninterruptibly();

				// 获取会话
				session = connectFuture.getSession();

				// 日志
				if (logger.isDebugEnabled()) {
					logger.debug("与服务器[{}]连接成功", address);
				}

				// 清除尝试次数
				retry = 0;
			} catch (Exception e) {
				// 超过最大尝试数后断开连接
				if (retry > config.getMaxRetry()) {
					close();
				}

				// 抛出异常
				throw new SocketException(e);
			}
		}

		/**
		 * {@link Client#isKeepAlive()}
		 */
		@Override
		public boolean isKeepAlive() {
			return keepAlive;
		}

		/**
		 * {@link Client#disableKeepAlive()}
		 */
		@Override
		public void disableKeepAlive() {
			keepAlive = false;
		}

		/**
		 * {@link Client#getTimestamp()}
		 */
		@Override
		public Date getTimestamp() {
			if (isKeepAlive()) {
				timestamp = new Date();
			}
			return timestamp;
		}

		/**
		 * {@link Client#isConnected()}
		 */
		@Override
		public boolean isConnected() {
			if (session == null) {
				return false;
			}
			if (session.isConnected()) {
				return true;
			}
			return false;
		}

		/**
		 * {@link Client#isDispose()}
		 */
		@Override
		public boolean isDisposed() {
			return connector.isDisposed() || connector.isDisposing();
		}

		/**
		 * {@link Client#getHost()}
		 */
		@Override
		public String getHost() {
			return address.getHostName();
		}

		/**
		 * {@link Client#getPort()}
		 */
		@Override
		public int getPort() {
			return address.getPort();
		}

		/**
		 * {@link Client#getAddress()}
		 */
		@Override
		public String getAddress() {
			return address.getAddress().getHostAddress() + ":" + address.getPort();
		}
	}

	// IOC
	public void setConfig(ClientConfig config) {
		this.config = config;
	}
}
