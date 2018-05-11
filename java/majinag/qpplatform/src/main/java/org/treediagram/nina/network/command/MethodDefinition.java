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

package org.treediagram.nina.network.command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javassist.NotFoundException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ReflectionUtils;
import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InRequest;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkApiSync;
import org.treediagram.nina.network.codec.CoderManager;
import org.treediagram.nina.network.command.parameter.BodyParameter;
import org.treediagram.nina.network.command.parameter.InBodyParameter;
import org.treediagram.nina.network.command.parameter.InRequestParameter;
import org.treediagram.nina.network.command.parameter.InSessionParameter;
import org.treediagram.nina.network.command.parameter.IoSessionParameter;
import org.treediagram.nina.network.command.parameter.Parameter;
import org.treediagram.nina.network.command.parameter.ParameterPostProcessor;
import org.treediagram.nina.network.command.parameter.RequestParameter;
import org.treediagram.nina.network.command.parameter.SessionParameter;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Session;

/**
 * 命令方法定义
 * 
 * @author kidal
 * 
 */
public class MethodDefinition {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(MethodDefinition.class);

	/**
	 * 命令方法
	 */
	private final Method method;

	/**
	 * 编码解码器
	 */
	private final CoderManager coders;

	/**
	 * 参数列表
	 */
	private final Parameter[] parameters;

	/**
	 * 同步执行
	 */
	private final String syncKey;

	/**
	 * 压缩请求
	 */
	private final boolean compressRequest;

	/**
	 * 压缩答复
	 */
	private final boolean compressResponse;

	/**
	 * 创建命令方法定义
	 */
	public MethodDefinition(Method method, CoderManager coders) throws NotFoundException {
		ReflectionUtils.makeAccessible(method);

		final NetworkApi networkApi = method.getAnnotation(NetworkApi.class);
		final NetworkApiSync networkApiSync = method.getAnnotation(NetworkApiSync.class);

		this.method = method;
		this.coders = coders;
		this.parameters = buildParameters();
		this.syncKey = ((networkApiSync != null) ? networkApiSync.value() : null);
		this.compressRequest = networkApi.compress().request();
		this.compressResponse = networkApi.compress().response();
	}

	/**
	 * 构造参数
	 */
	public Object[] buildParameters(Method method, Request<?> request, IoSession session,
			FacadeMethodInvokeContext context) throws Exception {
		final ConversionService conversionService = context.getConversionService();
		final ParameterPostProcessor parameterPostProcessor = context.getParameterPostProcessor();
		final Object[] objects = new Object[parameters.length];

		// 获取参数
		for (int i = 0; i < parameters.length; i++) {
			objects[i] = parameters[i].getValue(request, session, context);
		}

		// 后期参数处理
		if (parameterPostProcessor != null) {
			for (int i = 0; i < parameters.length; i++) {
				objects[i] = parameterPostProcessor.process(method, parameters[i].getType(),
						parameters[i].getParameterClass(), objects[i]);
			}
		}

		// 转换参数
		if (conversionService != null) {
			for (int i = 0; i < parameters.length; i++) {
				// null不转换
				if (objects[i] == null) {
					continue;
				}

				// 不支持转换不转换
				if (!parameters[i].isSupportConvert()) {
					continue;
				}

				// 类型一致不转换
				if (objects[i].getClass().equals(parameters[i].getParameterClass())) {
					continue;
				}

				// 转换
				objects[i] = conversionService.convert(objects[i], parameters[i].getParameterClass());
			}
		}

		return objects;
	}

	/**
	 * 构造参数
	 */
	private Parameter[] buildParameters() throws NotFoundException {
		Type[] types = method.getGenericParameterTypes();
		Parameter[] parameters = new Parameter[types.length];
		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = buildParameter(i);
		}
		return parameters;
	}

	/**
	 * 构造参数
	 */
	private Parameter buildParameter(int i) throws NotFoundException {
		// 检查索引
		if (i >= method.getGenericParameterTypes().length) {
			FormattingTuple message = MessageFormatter.format("参数下标 {} 超过了方法 {} 的有效下标", i, method.getName());
			logger.error(message.getMessage());
			throw new IllegalArgumentException(message.getMessage());
		}

		// 解析注解
		Type type = method.getGenericParameterTypes()[i];
		Annotation[] annotations = method.getParameterAnnotations()[i];
		for (Annotation annotation : annotations) {
			if (annotation instanceof InBody) {
				return new InBodyParameter((InBody) annotation, method, coders, i);
			} else if (annotation instanceof InRequest) {
				return new InRequestParameter((InRequest) annotation);
			} else if (annotation instanceof InSession) {
				return new InSessionParameter((InSession) annotation, method, i);
			}
		}

		// 抽象会话
		if (type instanceof Class && Session.class.isAssignableFrom((Class<?>) type)) {
			return SessionParameter.INSTANCE;
		}

		// IO会话
		if (type instanceof Class && IoSession.class.isAssignableFrom((Class<?>) type)) {
			return IoSessionParameter.INSTANCE;
		}

		// 请求
		if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().equals(Request.class)) {
			return RequestParameter.INSTANCE;
		}

		// 请求体
		return BodyParameter.INSTANCE;
	}

	// getters and setters

	public Method getMethod() {
		return method;
	}

	public String getSyncKey() {
		return syncKey;
	}

	public boolean isCompressRequest() {
		return compressRequest;
	}

	public boolean isCompressResponse() {
		return compressResponse;
	}
}
