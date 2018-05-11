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

package org.treediagram.nina.console;

import org.apache.commons.lang3.StringUtils;

/**
 * 控制台指令帮助类
 * 
 * @author kidal
 */
public class CommandHelper {
	/**
	 * 命令或参数的分隔符
	 */
	public static final String SPLIT = " ";

	/**
	 * 获取控制台指令名
	 * 
	 * @param line 控制台输入内容
	 * @return 如果无法获取将返回null
	 */
	public static String[] getNames(String line) {
		if (StringUtils.isBlank(line)) {
			return null;
		}

		String[] arguments = StringUtils.split(line);

		if (arguments.length == 0) {
			return null;
		} else if (arguments.length == 1) {
			return new String[] { arguments[0] };
		} else {
			return new String[] { arguments[0], arguments[1] };
		}
	}

	/**
	 * 获取控制台指令参数
	 * 
	 * @param line 控制台输入内容
	 * @return 如果无法获取将返回0长数组，不会返回null
	 */
	public static String[] getArguments(String line, int namesLength) {
		line = line.trim();

		if (StringUtils.isBlank(line)) {
			return new String[0];
		}

		String[] arguments = StringUtils.split(line);
		if (arguments.length <= 1) {
			return new String[0];
		}

		String[] result = new String[arguments.length - namesLength];
		for (int i = 0; i < result.length; i++) {
			result[i] = arguments[i + namesLength].replace('`', ' ');
		}

		return result;
	}
}
