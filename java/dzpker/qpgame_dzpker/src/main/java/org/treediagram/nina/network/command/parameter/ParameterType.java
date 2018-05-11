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

package org.treediagram.nina.network.command.parameter;

/**
 * 命令参数类型
 * 
 * @author kidal
 * 
 */
public enum ParameterType {
	/**
	 * 请求信息体
	 */
	BODY,

	/**
	 * 请求对象
	 */
	REQUEST,

	/**
	 * 抽象会话对象
	 */
	SESSION,

	/**
	 * 原生回话对象
	 */
	IO_SESSION,

	/**
	 * 在信息体中
	 */
	IN_BODY,

	/**
	 * 在请求对象中
	 */
	IN_REQUEST,

	/**
	 * 在会话对象中
	 */
	IN_SESSION,
}
