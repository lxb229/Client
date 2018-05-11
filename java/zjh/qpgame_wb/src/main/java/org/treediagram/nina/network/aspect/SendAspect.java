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

package org.treediagram.nina.network.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treediagram.nina.network.annotation.Recv;
import org.treediagram.nina.network.annotation.Send;
import org.treediagram.nina.network.annotation.SendCommand;
import org.treediagram.nina.network.client.Client;
import org.treediagram.nina.network.client.ClientFactory;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Response;

/**
 * 请求拦截器
 * 
 * @author kidal
 * 
 */
@Aspect
public class SendAspect {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(SendAspect.class);

	/**
	 * 请求器
	 */
	@Resource
	private ClientFactory clientFactory;

	/**
	 * 执行拦截方法
	 */
	@SuppressWarnings("rawtypes")
	@Around("execution(* *(java.net.InetSocketAddress,..)) && @annotation(sendCommand)")
	public Object executeInetSocketAddress(ProceedingJoinPoint pjp, SendCommand sendCommand) throws Throwable {
		// 获取参数和签名
		Object[] args = pjp.getArgs();
		Signature signature = pjp.getSignature();

		// 必须支持拦截切面才能执行
		if (signature instanceof MethodSignature) {
			Method method = ((MethodSignature) signature).getMethod();
			InetSocketAddress address = (InetSocketAddress) args[0];

			// 创建请求
			Request<Map<String, Object>> request = buildRequest(sendCommand.value(), method, args);

			// 发送请求
			Client client = clientFactory.getClient(address, true);
			Response<Map> response = client.send(request, Map.class, sendCommand.timeoutMillis());

			// 处理答复
			processResponse(response, method, args);

			// 完成
			return pjp.proceed(args);
		} else {
			logger.error("不支持的拦截切面:{}", signature);
			return pjp.proceed(args);
		}
	}

	/**
	 * 构造请求
	 */
	private Request<Map<String, Object>> buildRequest(int id, Method method, Object[] args) {
		Map<String, Object> body = new LinkedHashMap<String, Object>();
		Annotation[][] annotations = method.getParameterAnnotations();
		for (int i = 1; i < annotations.length; i++) {
			Send send = getSend(annotations[i]);
			if (send == null) {
				continue;
			}
			String name = send.value();
			body.put(name, args[i]);
		}
		return Request.valueOf(id, body);
	}

	/**
	 * 获取发送
	 */
	private Send getSend(Annotation[] annotations) {
		for (int i = 0; i < annotations.length; i++) {
			if (annotations[i] instanceof Send) {
				return (Send) annotations[i];
			}
		}
		return null;
	}

	/**
	 * 处理答复
	 */
	@SuppressWarnings("rawtypes")
	private void processResponse(Response<Map> response, Method method, Object[] args) {
		Annotation[][] annotations = method.getParameterAnnotations();
		for (int i = 1; i < annotations.length; i++) {
			Recv recv = getRecv(annotations[i]);
			if (recv == null) {
				continue;
			}
			String name = recv.value();
			args[i] = response.getBody().get(name);
		}
	}

	/**
	 * 获取接收
	 */
	private Recv getRecv(Annotation[] annotations) {
		for (int i = 0; i < annotations.length; i++) {
			if (annotations[i] instanceof Recv) {
				return (Recv) annotations[i];
			}
		}
		return null;
	}
}
