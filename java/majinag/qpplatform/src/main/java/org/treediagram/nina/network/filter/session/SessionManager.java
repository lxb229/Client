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

package org.treediagram.nina.network.filter.session;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;
import org.treediagram.nina.network.model.Request;

/**
 * 会话管理器
 * 
 * @author kidal
 * 
 */
public interface SessionManager {
	/**
	 * 已经验证身份的会话
	 */
	String IDENTITY = "identity";

	/**
	 * 向会话发送请求
	 */
	void send(Request<?> request, Object... ids);
	
	/**
	 * 向会话发送请求
	 */
	void send(Request<?> request, IoSession... sessions);

	/**
	 * 向所有会话发送请求
	 */
	void sendAll(Request<?> request);

	/**
	 * 向所有已经验证身份的会话发送请求
	 */
	void sendAllIdentified(Request<?> request);

	/**
	 * 向所有匿名的会话发送请求
	 */
	void sendAllAnonymous(Request<?> request);

	/**
	 * 检查指定标识的会话是否在线
	 */
	boolean isOnline(Object... ids);

	/**
	 * 踢指定会话
	 */
	Collection<?> kick(int cause, Object... ids);

	/**
	 * 踢全部会话
	 */
	Collection<?> kickAll(int cause);

	/**
	 * 获取在线会话标识
	 */
	Collection<?> getOnlineIdentities();

	/**
	 * 获取标识
	 */
	Object getOnlineIdentity(IoSession session);

	/**
	 * 获取会话
	 */
	IoSession getSession(Object id);

	/**
	 * 替换会话
	 */
	void replace(IoSession src, IoSession dest);

	/**
	 * 绑定会话
	 */
	void bind(IoSession session, Object obj);
	
	/**
	 * 解除会话绑定
	 * */
	void unBind(IoSession session, Object obj);

	/**
	 * 统计当前的会话数量
	 */
	int count(boolean includeAnonymous);

	/**
	 * 添加事件侦听器
	 */
	public void addListener(SessionListener listener);
	
	/**
	 * 获取因空闲事件关闭的会话列表
	 * */
	public ConcurrentMap<Long, Long> getIdleClosedSessions(); 
}
