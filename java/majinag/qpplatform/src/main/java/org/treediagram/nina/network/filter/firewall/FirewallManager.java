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

package org.treediagram.nina.network.filter.firewall;

import java.util.Collection;

import org.apache.mina.core.session.IoSession;

/**
 * 防火墙管理器
 * 
 * @author kidal
 * 
 */
public interface FirewallManager {
	/**
	 * 当前的黑名单列表
	 */
	public Collection<String> getBlockList();

	/**
	 * 当前的白名单列表
	 */
	public Collection<String> getAllowList();

	/**
	 * 阻止指定IP的连接
	 * 
	 * @param ip IP 地址
	 * @param 持续时间 （秒）
	 */
	public void block(String ip, int seconds);

	/**
	 * 阻止指定IP的连接
	 * 
	 * @param ip IP 地址
	 */
	public void block(String ip);

	/**
	 * 阻止指定会话的IP连接
	 * 
	 * @param session 被阻止的会话
	 * @return 被阻止的 IP
	 */
	public String blockByIoSession(IoSession session);

	/**
	 * 解除对指定IP连接的阻止
	 * 
	 * @param ip IP 地址
	 */
	public void unblock(String ip);

	/**
	 * 是否阻止全部非白名单IP的连接的状态
	 */
	public boolean isblockAll();

	/**
	 * 开启阻止全部非白名单IP的连接的状态
	 */
	public void blockAll();

	/**
	 * 关闭阻止全部非白名单IP的连接的状态
	 */
	public void unblockAll();

	/**
	 * 添加指定的白名单IP
	 * 
	 * @param ip IP 地址
	 */
	public void allow(String ip);

	/**
	 * 移除指定的白名单IP
	 * 
	 * @param ip IP 地址
	 */
	public void disallow(String ip);

	/**
	 * 当前的连接数
	 */
	public int getCurrentConnections();
}
