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

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.treediagram.nina.network.annotation.InRequest;
import org.treediagram.nina.network.command.FacadeMethodInvokeContext;
import org.treediagram.nina.network.model.Request;

/**
 * {@link InRequest}注解的参数
 * 
 * @author kidal
 * 
 */
public class InRequestParameter implements Parameter {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(InRequestParameter.class);

	/**
	 * 注解
	 */
	private final InRequest annotation;

	/**
	 * 创建{@link InRequest}注解的参数
	 */
	public InRequestParameter(InRequest annotation) {
		this.annotation = annotation;
	}

	/**
	 * {@link Parameter#getType()}
	 */
	@Override
	public ParameterType getType() {
		return ParameterType.IN_REQUEST;
	}

	/**
	 * {@link Parameter#getParameterClass()}
	 */
	@Override
	public Class<?> getParameterClass() {
		return null;
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
	public Object getValue(Request<?> request, IoSession session, FacadeMethodInvokeContext context) {
		switch (annotation.value()) {
		case ID:
			return request.getId();

		case TIMESTAMP:
			return request.getTimestamp();

		default:
			FormattingTuple message = MessageFormatter.format("无法处理的 InRequest 类型 {}", annotation.value());
			logger.error(message.getMessage());
			throw new IllegalStateException(message.getMessage());
		}
	}
}
