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

import java.util.Date;

import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Response;

/**
 * 客户端
 * 
 * @author kidal
 * 
 */
public interface Client {
	/**
	 * 发送请求
	 */
	<T> Response<T> send(Request<?> request);

	/**
	 * 发送请求
	 */
	<T> Response<T> send(Request<?> request, Class<T> responseClass);

	/**
	 * 发送请求
	 */
	<T> Response<T> send(Request<?> request, Class<T> responseClass, int timeoutMillis);

	/**
	 * 关闭与服务器的连接
	 */
	void close();

	/**
	 * 连接服务器
	 */
	void connect();

	/**
	 * 检查是否需要保持连接
	 */
	boolean isKeepAlive();

	/**
	 * 取消保持连接状态
	 */
	void disableKeepAlive();

	/**
	 * 获取客户端的最后操作时间戳
	 */
	Date getTimestamp();

	/**
	 * 检查会话是否处于连接状态
	 */
	boolean isConnected();

	/**
	 * 连接是否有效
	 */
	boolean isDisposed();

	/**
	 * 获取连接的地址
	 */
	String getHost();

	/**
	 * 获取连接的端口
	 * 
	 * @return
	 */
	int getPort();

	/**
	 * 获取连接的地址
	 */
	String getAddress();
}
