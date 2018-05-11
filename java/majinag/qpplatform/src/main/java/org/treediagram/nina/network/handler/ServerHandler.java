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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.treediagram.nina.core.exception.ManagedException;
import org.treediagram.nina.core.zip.ZipUtils;
import org.treediagram.nina.network.codec.CoderManager;
import org.treediagram.nina.network.codec.Message;
import org.treediagram.nina.network.command.Commands;
import org.treediagram.nina.network.command.FacadeMethod;
import org.treediagram.nina.network.command.FacadeMethodInvokeContext;
import org.treediagram.nina.network.command.FacadeMethodSyncCaller;
import org.treediagram.nina.network.command.MethodDefinition;
import org.treediagram.nina.network.command.parameter.ParameterPostProcessor;
import org.treediagram.nina.network.exception.CompressException;
import org.treediagram.nina.network.exception.ProcessingException;
import org.treediagram.nina.network.exception.SessionParameterException;
import org.treediagram.nina.network.model.Code;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Response;
import org.treediagram.nina.network.model.Result;

/**
 * 服务器IO处理器适配器
 * 
 * @author kidal
 * 
 */
public class ServerHandler extends IoHandlerAdapter {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	/**
	 * 转换服务
	 */
	@Autowired(required = false)
	private ConversionService conversionService;

	/**
	 * 参数后期处理器
	 */
	@Autowired(required = false)
	private ParameterPostProcessor parameterPostProcessor;

	/**
	 * 消息处理器
	 */
	@Autowired(required = false)
	private MessageProcessor messageProcessor;

	/**
	 * 前端方法调用上下文
	 */
	private FacadeMethodInvokeContext facadeMethodInvokeContext;

	/**
	 * 命令集合
	 */
	private final Commands commands;

	/**
	 * 编码解码器
	 */
	private final CoderManager coders;

	/**
	 * 前端方法同步调用器
	 */
	private final FacadeMethodSyncCaller facadeMethodSyncCaller = new FacadeMethodSyncCaller();

	/**
	 * 侦听器
	 */
	private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

	/**
	 * 创建服务器IO处理器适配器
	 */
	public ServerHandler(Commands commands, CoderManager coders) {
		this.commands = commands;
		this.coders = coders;
	}

	/**
	 * 初始化
	 */
	@PostConstruct
	protected void init() {
		// 收集参数
		facadeMethodInvokeContext = new FacadeMethodInvokeContext(conversionService, parameterPostProcessor);

		// 注册统计
		commands.registerStat();
	}

	/**
	 * 添加侦听器
	 */
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	/**
	 * 移除侦听器
	 */
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	/**
	 * 发送对象
	 */
	@Deprecated
	public void send(boolean compress, int code, int timestamp, int id, Object body, 
			String attachment, IoSession... sessions) {
		byte[] bytes = coders.encode(code, body);
		byte[] attachmentBytes = attachment != null ? attachment.getBytes() : null;

		if (compress){
			try {
				bytes = ZipUtils.compress(bytes);
			} 
			catch (Exception e)
			{
				throw new CompressException(e);
			}
		}
		
		Message message = new Message(code, 0, id, timestamp, bytes, attachmentBytes);

		for (IoSession session : sessions){
			if (!session.isConnected() || session.isClosing()){
				continue;
			}
			session.write(message);
		}
	}

	/**
	 * 发送请求
	 */
	public void send(Request<?> request, IoSession... sessions) {
		Message message = null;
		if (messageProcessor != null) {
			message = messageProcessor.requestToMessage(request, coders, sessions);
		}
		if (message == null) {
			boolean compress = request.isCompress();
			int code = request.getCode();
			int timestamp = request.getTimestamp();
			int id = request.getId();
			Object body = request.getBody();
			String attachment = request.getAttachment();

			byte[] bytes = coders.encode(code, body);
			byte[] attachmentBytes = attachment != null ? attachment.getBytes() : null;

			if (compress) {
				try {
					bytes = ZipUtils.compress(bytes);
				} catch (Exception e) {
					throw new CompressException(e);
				}
			}
			message = new Message(code, 0, id, timestamp, bytes, attachmentBytes);
		}
		for (IoSession session : sessions) {
			if (!session.isConnected() || session.isClosing()) {
				continue;
			}
			session.write(message);
		}
	}

	/**
	 * 发送答复
	 */
	private void send(boolean compress, Response<?> response, IoSession... sessions) {
		Message message = null;
		if (messageProcessor != null) {
			message = messageProcessor.responseToMessage(response, compress, coders, sessions);
		}
		if (message == null) {
			int code = response.getCode();
			int timestamp = response.getTimestamp();
			int id = response.getId();
			Object body = response.getBody();
			String attachment = response.getAttachment();

			byte[] bytes = coders.encode(code, body);
			byte[] attachmentBytes = attachment != null ? attachment.getBytes() : null;

			if (compress) {
				try {
					bytes = ZipUtils.compress(bytes);
				} catch (Exception e) {
					throw new CompressException(e);
				}
			}
			message = new Message(code, 0, id, timestamp, bytes, attachmentBytes);
		}
		for (IoSession session : sessions) {
			if (!session.isConnected() || session.isClosing()) {
				continue;
			}
			session.write(message);
		}
	}

	/**
	 * 处理消息
	 */
	@SuppressWarnings("rawtypes")
	public Response receive(final Message message, final IoSession session, boolean allowSyncCall) {
		// 获取前端方法
		FacadeMethod facadeMethod = commands.getFacadeMethod(message.getId());
		if (facadeMethod == null) {
			logger.warn("从会话 {} 收到了未注册的消息 {}", session, message.getId());
			return null;
		}

		// 转换消息为请求
		Request<?> request = null;
		if (messageProcessor != null) {
			request = messageProcessor.messageToRequest(message, facadeMethod.getDefinition(), coders, session);
		}
		if (request == null) {
			// 解压数据
			byte[] bytes = message.getBody();
			if (facadeMethod.getDefinition().isCompressRequest()) {
				try {
					bytes = ZipUtils.decompress(bytes);
				} catch (Exception e) {
					logger.warn("会话 {} 发送的消息 {} 解压失败", new Object[] { session, message.getId(), e });
					return null;
				}
			}

			// 创建请求
			try {
				Object body = coders.decode(message.getCode(), bytes);
				request = Request.valueOf(message.getId(), body);
				request.setCode(message.getCode());
				request.setTimestamp(message.getTimestamp());
				request.setAttachment(message.getAttachment() != null ? new String(message.getAttachment()) : null);
				request.setCompress(facadeMethod.getDefinition().isCompressResponse());
			} catch (Exception e) {
				logger.warn("会话 {} 发送的消息 {} 转换失败", new Object[] { session, message.getId(), e });
				return null;
			}
		}

		// 执行前端方法
		return receive(facadeMethod, request, session, allowSyncCall);
	}

	// privates

	/**
	 * 执行前端方法
	 */
	@SuppressWarnings("rawtypes")
	private Response receive(final FacadeMethod facadeMethod, final Request<?> request, final IoSession session,
			boolean allowSyncCall) {
		MethodDefinition definition = facadeMethod.getDefinition();
		String syncKey = definition.getSyncKey();

		if (StringUtils.isBlank(syncKey)) {
			return doReceive(facadeMethod, request, session);
		} else {
			if (allowSyncCall) {
				facadeMethodSyncCaller.execute(syncKey, new Runnable() {
					@Override
					public void run() {
						doReceive(facadeMethod, request, session);
					}
				});
				return null;
			} else {
				throw new ManagedException(Code.PROCESSING_ERROR, "前端方法被注解需要同步执行");
			}
		}
	}

	/**
	 * 执行接受
	 */
	@SuppressWarnings("rawtypes")
	private Response doReceive(FacadeMethod facadeMethod, Request<?> request, IoSession session) {
		try {
			return doReceiveThrowable(facadeMethod, request, session);
		} catch (SessionParameterException e) {
			logger.warn("会话 {} SESSION参数异常，链接 {} 将被强制关闭", session.getId(), session.getRemoteAddress());
			fireCaughtException(session, request, Code.PARAMETER_ERROR, e);
			session.close(false);
		} catch (ProcessingException e) {
			if (logger.isInfoEnabled()) {
				logger.info("消息处理逻辑异常 - {}", e.getMessage());
			}
			fireCaughtException(session, request, Code.PROCESSING_ERROR, e);
		} catch (ManagedException e) {
			fireCaughtException(session, request, e.getCode(), e);
		} catch (Throwable e) {
			if (logger.isInfoEnabled()) {
				logger.info("消息处理未知异常 - ", e.getMessage());
			}
			fireCaughtException(session, request, Code.UNKNOWN_ERROR, e);
		}
		return null;
	}

	/**
	 * 收到消息
	 */
	@SuppressWarnings("rawtypes")
	private Response doReceiveThrowable(FacadeMethod facadeMethod, Request<?> request, IoSession session)
			throws Throwable {
		// 事件
		Object value = fireBeforeInvokeFacadeMethod(session, facadeMethod, request);

		// 处理消息
		if (value == null) {
			value = facadeMethod.invoke(request, session, facadeMethodInvokeContext);
		}

		// 事件
		value = fireAfterInvokeFacadeMethod(session, facadeMethod, request, value);

		// 获取答复
		Response response = null;
		response = reverse(facadeMethod, request, session, value, response);

		// 返回答复
		return response;
	}

	@SuppressWarnings("rawtypes")
	private Response reverse(FacadeMethod facadeMethod, Request<?> request, IoSession session, Object value, Response response) 
	{
		if (value != null) {
			if (value instanceof Response) {
				response = (Response) value;
				response.setCode(request.getCode());
			} else if (value instanceof Result) {
				Result<?> result = (Result) value;
				response = Response.valueOf(result.toMap());
				response.setAttachment(result.getAttachment());
			} else {
				response = Response.valueOf(value);
				response.setCode(request.getCode());
			}
		} else {
			throw new ManagedException(Code.RESPONSE_PROCESSING_ERROR);
		}

		//设置
		response.setTimestamp(request.getTimestamp());
		response.setId(request.getId());

		// 即将答复事件
		fireBeforeResponse(session);

		// 发送答复
		if (session != null) {
			send(facadeMethod.getDefinition().isCompressResponse(), response, session);
		}
		return response;
	}

	// events

	private Object fireBeforeInvokeFacadeMethod(IoSession session, FacadeMethod facadeMethod, Request<?> request) {
		Object value = null;
		Object target = facadeMethod.getTarget();
		Method method = facadeMethod.getDefinition().getMethod();

		for (Listener listener : listeners) {
			value = listener.beforeInvokeFacdeMethod(session, target, method, request, value);
		}

		return value;
	}

	private Object fireAfterInvokeFacadeMethod(IoSession session, FacadeMethod facadeMethod, Request<?> request,
			Object value) {
		Object target = facadeMethod.getTarget();
		Method method = facadeMethod.getDefinition().getMethod();

		for (Listener listener : listeners) {
			value = listener.afterInvokeFacdeMethod(session, target, method, request, value);
		}

		return value;
	}

	private void fireBeforeResponse(IoSession session) {
		for (Listener listener : listeners) {
			listener.beforeResponse(session);
		}
	}

	private void fireCaughtException(IoSession session, Request<?> request, int errorCode, Throwable e) {
		for (Listener listener : listeners) {
			listener.caughtException(session, request, errorCode, e);
		}
	}

	// IoHandlerAdapter

	/**
	 * {@link IoHandlerAdapter#messageReceived(IoSession, Object)}
	 */
	@Override
	public void messageReceived(IoSession session, Object in) throws Exception {
		// 检查消息与会话状态
		if (in == null) {
			logger.warn("收到的消息为null");
			return;
		}
		if (!(in instanceof Message)) {
			logger.warn("无效的消息类型[{}]", in.getClass().getName());
			return;
		}
		if (session.isClosing() || !session.isConnected()) {
			if (logger.isDebugEnabled()) {
				logger.debug("会话 {} 已经关闭或正在关闭中，忽略消息", session.getId());
			}
			return;
		}

		// 获取消息
		Message message = (Message) in;

		// 处理消息
		receive(message, session, true);
	}

	/**
	 * {@link IoHandlerAdapter#exceptionCaught(IoSession, Throwable)}
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		fireCaughtException(session, null, Code.UNKNOWN_ERROR, cause);
	}

	// getters

	public CoderManager getCoders() {
		return coders;
	}
}
