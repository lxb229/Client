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

package org.treediagram.nina.network.handler;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;

/**
 * jetty请求处理器
 * 
 * @author kidal
 *
 */
public interface JettyRequestHandler {
	/**
	 * 获取请求路径
	 */
	String getPath();

	/**
	 * 处理请求
	 * 
	 * @param target 请求路径
	 * @param baseRequest 请求
	 * @param request 请求
	 * @return 应答数据
	 */
	byte[] handleRequest(String target, Request baseRequest, HttpServletRequest request);
}
