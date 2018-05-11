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

package org.treediagram.nina.network.filter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.treediagram.nina.network.model.Attribute;
import org.treediagram.nina.network.util.IpUtils;

/**
 * 后台管理器过滤器
 * 
 * @author kidal
 * 
 */
public class ManagerFilter extends IoFilterAdapter {
	/**
	 * 管理后台属性标识
	 */
	public static final String MANAGER = "manager";

	/**
	 * 管理后台属性
	 */
	private static final Attribute<String> managerAttribute = new Attribute<String>(MANAGER);

	/**
	 * 读写锁
	 */
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 允许的IP地址集合
	 */
	private final Map<Pattern, String> allowIps = new LinkedHashMap<Pattern, String>();

	/**
	 * 设置后台管理器IP地址集合
	 */
	public void setAllowIps(Map<String, String> ips) {
		WriteLock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			allowIps.clear();
			for (Entry<String, String> e : ips.entrySet()) {
				String ip = e.getKey();
				String regex = ip.replace(".", "[.]").replace("*", "[0-9]*");
				Pattern pattern = Pattern.compile(regex);
				allowIps.put(pattern, e.getValue());
			}
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * 设置后台管理器IP地址
	 */
	public void setAllowIpConfig(String config) {
		String[] ips = config.split(",");
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (String ip : ips) {
			String[] s = ip.split("=", 2);
			result.put(s[0], s[1]);
		}
		setAllowIps(result);
	}

	/**
	 * 添加许可IP地址
	 */
	public void addAllowIp(String ip, String name) {
		WriteLock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			String reg = ip.replace(".", "[.]").replace("*", "[0-9]*");
			Pattern pattern = Pattern.compile(reg);
			allowIps.put(pattern, name);
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * {@link IoFilterAdapter#sessionOpened(org.apache.mina.core.filterchain.IoFilter.NextFilter, IoSession)}
	 */
	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
		String ip = IpUtils.getIp(session);
		for (Entry<Pattern, String> e : allowIps.entrySet()) {
			Matcher matcher = e.getKey().matcher(ip);
			if (matcher.matches()) {
				managerAttribute.setValue(session, e.getValue());
				break;
			}
		}
		super.sessionOpened(nextFilter, session);
	}
}
