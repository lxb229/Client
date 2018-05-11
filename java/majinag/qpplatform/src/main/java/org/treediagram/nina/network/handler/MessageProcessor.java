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

import org.apache.mina.core.session.IoSession;
import org.treediagram.nina.network.codec.CoderManager;
import org.treediagram.nina.network.codec.Message;
import org.treediagram.nina.network.command.MethodDefinition;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Response;

/**
 * 消息处理器
 * 
 * @author Kidal
 *
 */
public interface MessageProcessor {
	/**
	 * 将消息转换为请求
	 * 
	 * @param message 需要转换的消息
	 * @param methodDefinition 消息调用的方法定义
	 * @param coders 编码解码器
	 * @param session 会话
	 * @return 转换后的请求(返回null表示使用默认算法)
	 */
	Request<?> messageToRequest(Message message, MethodDefinition methodDefinition, CoderManager coders,
			IoSession session);

	/**
	 * 将请求转换为消息
	 * 
	 * @param request 需要转换的请求
	 * @param coders 编码解码器
	 * @param sessions 会话们
	 * @return 转换后的消息(返回null表示使用默认算法)
	 */
	Message requestToMessage(Request<?> request, CoderManager coders, IoSession... sessions);

	/**
	 * 将答复转换为消息
	 * 
	 * @param response 需要转换的答复
	 * @param compress 是否压缩消息体
	 * @param coders 编码解码器
	 * @param sessions 会话们
	 * @return 转换后的消息(返回null表示使用默认算法)
	 */
	Message responseToMessage(Response<?> response, boolean compress, CoderManager coders, IoSession... sessions);
}
