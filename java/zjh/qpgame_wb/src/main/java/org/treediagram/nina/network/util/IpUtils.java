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

package org.treediagram.nina.network.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;

/**
 * 获取IP地址的简单工具类
 * 
 * @author kidal
 */
public abstract class IpUtils {
	/**
	 * 获取会话的IP地址
	 */
	public static String getIp(IoSession session) {
		if (session == null || session.getRemoteAddress() == null) {
			return "UNKNOWN";
		} else {
			String ip = session.getRemoteAddress().toString();
			return StringUtils.substringBetween(ip, "/", ":");
		}
	}

	/***
	 * IP 地址正则配置开始
	 **/
	private final static String ipPatternConfig = "[0-9\\*]+\\.";

	/***
	 * IP 地址正则配置结束
	 **/
	private final static String ipPatternEndConfig = "[0-9\\*]+";

	/**
	 * IP 正则模式
	 */
	public final static Pattern IP_PATTERN = Pattern.compile(ipPatternConfig + ipPatternConfig + ipPatternConfig
			+ ipPatternEndConfig);

	/**
	 * 检查 IP 是否有效
	 */
	public static boolean isValidIp(String ip) {
		return IP_PATTERN.matcher(ip).matches();
	}
}
