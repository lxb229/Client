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

import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.core.convert.ConversionService;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.command.FacadeMethodInvokeContext;
import org.treediagram.nina.network.exception.SessionParameterException;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Session;

/**
 * {@link InSession}注解的参数
 * 
 * @author kidal
 * 
 */
public class InSessionParameter implements Parameter {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(InRequestParameter.class);

	/**
	 * 注解
	 */
	private final InSession annotation;

	/**
	 * 类型
	 */
	private final Class<?> parameterClass;

	/**
	 * 创建{@link InSession}注解的参数
	 */
	public InSessionParameter(InSession annotation, Method method, int i) {
		this.annotation = annotation;
		this.parameterClass = method.getParameterTypes()[i];
	}

	/**
	 * {@link Parameter#getType()}
	 */
	@Override
	public ParameterType getType() {
		return ParameterType.IN_SESSION;
	}

	/**
	 * {@link Parameter#getParameterClass()}
	 */
	@Override
	public Class<?> getParameterClass() {
		return parameterClass;
	}

	/**
	 * {@link Parameter#isSupportConvert()}
	 */
	@Override
	public boolean isSupportConvert() {
		return true;
	}

	/**
	 * {@link Parameter#getValue(Request, IoSession, FacadeMethodInvokeContext)}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object getValue(Request<?> request, IoSession session, FacadeMethodInvokeContext context) {
		// 获取会话属性
		ConcurrentMap<String, Object> attributes = (ConcurrentMap<String, Object>) session.getAttribute(Session.MAIN_KEY);

		// 值
		Object value = null;

		// 从会话属性获取值
		if (attributes != null) {
			String key = annotation.value();
			if (StringUtils.isBlank(key)) {
				value = attributes.get(SessionManager.IDENTITY);
			} else {
				value = attributes.get(key);
			}
		}

		// 未获取值
		if (value == null) {
			// 尝试使用默认值
			String defaultValue = annotation.defaultValue();
			if (!StringUtils.isBlank(defaultValue)) {
				ConversionService conversionService = context.getConversionService();
				if (conversionService != null) {
					value = conversionService.convert(defaultValue, parameterClass);
				}
			}
            
			// 是否必须
			if (annotation.required()) {
				int id = request.getId();
				SocketAddress address = session.getRemoteAddress();
				FormattingTuple message = MessageFormatter.format("指令 {} 的注释 {} 要求的 SESSION 参数不存在, 连接 {}",
						new Object[] { id, annotation, address });
				logger.warn(message.getMessage());
				throw new SessionParameterException(message.getMessage());
			}
		}

		// 完成
		return value;
	}
}
