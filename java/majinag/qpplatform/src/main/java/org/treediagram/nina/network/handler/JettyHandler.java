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

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treediagram.nina.core.exception.ManagedException;
import org.treediagram.nina.network.codec.Message;
import org.treediagram.nina.network.exception.JettyFriendlyException;
import org.treediagram.nina.network.model.Code;
import org.treediagram.nina.network.model.Response;

/**
 * Jetty处理器
 * 
 * @author kidal
 *
 */
public class JettyHandler extends AbstractHandler {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(JettyHandler.class);

	/**
	 * Socket服务器处理器
	 */
	private final ServerHandler serverHandler;

	/**
	 * 白名单IP
	 */
	private final Map<String, Pattern> whiteIps;

	/**
	 * jetty请求处理器
	 */
	private final Map<String, JettyRequestHandler> jettyRequestHandlers;

	/**
	 * 创建Http服务器处理器
	 */
	public JettyHandler(ServerHandler serverHandler, Map<String, Pattern> whiteIps,
			Map<String, JettyRequestHandler> jettyRequestHandlers) {
		this.serverHandler = serverHandler;
		this.whiteIps = whiteIps;
		this.jettyRequestHandlers = jettyRequestHandlers;
	}

	/**
	 * {@link Handler#handle(String, Request, HttpServletRequest, HttpServletResponse)}
	 */
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// 跳过已经应答或者已经处理的请求
		if (response.isCommitted() || baseRequest.isHandled()) {
			return;
		}

		// 标记请求已经处理
		baseRequest.setHandled(true);

		// 允许跨域
		response.addHeader("Access-Control-Allow-Origin", "*");

		// 过滤/favicon.ico请求
		if (request.getMethod().equals(HttpMethods.GET) && request.getRequestURI().equals("/favicon.ico")) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// 处理请求
		byte[] responseData = null;
		try {
			responseData = handleRequest(target, baseRequest, request);

			// 200 - OK
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (JettyFriendlyException e) {
			response.setStatus(e.getStatusCode());
			responseData = e.getResponseData();
		} catch (ManagedException e) {
			// 500 - INTERNAL SERVER ERROR
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseData = String.valueOf(e.getCode()).getBytes();

			// log
			if (logger.isDebugEnabled()) {
				logger.debug("", e);
			}
		} catch (Exception e) {
			logger.error("jetty侦听器在处理请求时出现异常", e);

			// 500 - INTERNAL SERVER ERROR
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseData = String.valueOf(Code.UNKNOWN_ERROR).getBytes();

			// log
			if (logger.isDebugEnabled()) {
				logger.debug("", e);
			}
		}

		// 应答
		if (responseData != null) {
			// text/plain;charset=UTF-8
			response.setContentType(MimeTypes.TEXT_PLAIN_UTF_8);

			// 写入应答流
			ServletOutputStream out = response.getOutputStream();
			out.write(responseData);
		}
	}

	/**
	 * 处理请求
	 */
	@SuppressWarnings("rawtypes")
	private byte[] handleRequest(String target, Request baseRequest, HttpServletRequest request) {
		// 检查IP
		checkIp(baseRequest);

		try {
			if (target.equals("/")) {
				// 获取查询参数
				final String codeString = baseRequest.getParameter("code");
				final String flagString = baseRequest.getParameter("flag");
				final String idString = baseRequest.getParameter("id");
				final String tsString = baseRequest.getParameter("ts");
				final String bodyString = baseRequest.getParameter("body");
				final String attachmentString = baseRequest.getParameter("attachment");

				// 检查必要的查询参数
				if (StringUtils.isBlank(codeString) || StringUtils.isBlank(flagString) || StringUtils.isBlank(idString)
						|| StringUtils.isBlank(tsString) || StringUtils.isBlank(bodyString)) {
					throw new ManagedException(HttpServletResponse.SC_BAD_REQUEST);
				}

				// 转换查询参数
				final int code = Integer.valueOf(codeString);
				final int flag = Integer.valueOf(flagString);
				final int id = Integer.valueOf(idString);
				final int ts = Integer.valueOf(tsString);
				final byte[] body = Base64.decodeBase64(bodyString);
				final byte[] attachment = ((StringUtils.isNotBlank(attachmentString)) ? Base64
						.decodeBase64(attachmentString) : null);

				// 构造消息
				final Message message = new Message(code, flag, id, ts, body, attachment);

				// 处理消息
				final Response response = serverHandler.receive(message, null, false);
				if (response == null) {
					throw new ManagedException(HttpServletResponse.SC_BAD_REQUEST);
				}

				// 获取答复消息体
				final Object respBody = response.getBody();
				if (respBody != null) {
					final byte[] respBodyBytes = serverHandler.getCoders().encode(code, respBody);
					final byte[] respBodyBase64 = Base64.encodeBase64(respBodyBytes);
					return respBodyBase64;
				}
			} else {
				JettyRequestHandler jettyRequestHandler = jettyRequestHandlers.get(target);
				if (jettyRequestHandler != null) {
					return jettyRequestHandler.handleRequest(target, baseRequest, request);
				} else {
					throw new ManagedException(HttpServletResponse.SC_NOT_FOUND);
				}
			}

			// 至少要返回一个空数组
			return ArrayUtils.EMPTY_BYTE_ARRAY;
		} catch (JettyFriendlyException e) {
			throw e;
		} catch (ManagedException e) {
			throw e;
		} catch (Exception e) {
			throw new ManagedException(HttpServletResponse.SC_BAD_REQUEST, e);
		}
	}

	/**
	 * 检查ip
	 */
	private void checkIp(Request baseRequest) {
		if (whiteIps != null && whiteIps.size() > 0) {
			// 获取ip
			String remoteAddr = baseRequest.getRemoteAddr();

			// 确认ip是否在白名单内
			boolean white = false;
			for (Entry<String, Pattern> entry : whiteIps.entrySet()) {
				if (entry.getValue().matcher(remoteAddr).matches()) {
					white = true;
					break;
				}
			}

			if (!white) {
				throw new ManagedException(HttpServletResponse.SC_FORBIDDEN);
			}
		}
	}
}
