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

import java.lang.reflect.Method;

import org.apache.mina.core.session.IoSession;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Response;

/**
 * 处理器侦听器
 * 
 * @author kidal
 * 
 */
public interface Listener {
	/**
	 * 收到请求
	 */
	void received(Request<?> request, IoSession... sessions);

	/**
	 * 发送答复
	 */
	void sent(Response<?> response, IoSession... sessions);

	/**
	 * 捕获异常
	 */
	void caughtException(IoSession session, Request<?> request, int errorCode, Throwable e);

	/**
	 * 即将调用前端方法
	 */
	Object beforeInvokeFacdeMethod(IoSession session, Object target, Method method, Request<?> request, Object value);

	/**
	 * 调用完毕前端方法
	 * 
	 * @param value 答复
	 * @return 修正后的答复
	 */
	Object afterInvokeFacdeMethod(IoSession session, Object target, Method method, Request<?> request, Object value);

	/**
	 * 即将答复
	 */
	void beforeResponse(IoSession session);
}
