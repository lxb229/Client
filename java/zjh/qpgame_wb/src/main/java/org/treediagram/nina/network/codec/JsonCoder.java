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

package org.treediagram.nina.network.codec;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.core.convert.ConversionService;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.command.FacadeMethodInvokeContext;
import org.treediagram.nina.network.command.parameter.InBodyParameter;
import org.treediagram.nina.network.exception.CoderDecodeException;
import org.treediagram.nina.network.exception.CoderEncodeException;
import org.treediagram.nina.network.exception.ParameterException;

/**
 * json编码解码器
 * 
 * @author kidal
 * 
 */
public class JsonCoder implements Coder {
	/**
	 * 日志
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * {@link Coder#decode(byte[])}
	 */
	@Override
	public Object decode(byte[] bytes) {
		try 
		{
			return JsonUtils.string2Map(new String(bytes, "UTF-8").replace("[]", "null"));
		} catch (Exception e) {
			throw new CoderDecodeException(e);
		}
	}

	/**
	 * {@link Coder#encode(Object)}
	 */
	@Override
	public byte[] encode(Object object) {
		try {
			return JsonUtils.object2String(object).replace("[]", "null").getBytes("UTF-8");
		} catch (Exception e) {
			throw new CoderEncodeException(e);
		}
	}

	/**
	 * {@link Coder#getInBody(Object, InBodyParameter, FacadeMethodInvokeContext)

	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object getInBody(Object body, InBodyParameter parameter, FacadeMethodInvokeContext context) {
		// 准备参数
		final Map<String, Object> _body = (Map<String, Object>) body;
		final InBody annotation = parameter.getAnnotation();
		final String parameterName = parameter.getParameterName();
		final Class<?> parameterClass = parameter.getParameterClass();

		// 尝试直接读取
		if (body != null && _body.containsKey(parameterName)) {
			return _body.get(parameterName);
		}

		// 使用默认参数
		final String defaultValue = annotation.defaultValue();
		if (!StringUtils.isBlank(defaultValue)) {
			ConversionService conversionService = context.getConversionService();
			if (conversionService != null) {
				return conversionService.convert(defaultValue, parameterClass);
			}
		}

		// 是否必须
		if (annotation.required()) {
			FormattingTuple message = MessageFormatter.format("请求参数 {} 不存在", annotation.value());
			if (logger.isInfoEnabled()) {
				logger.info(message.getMessage());
			}
			throw new ParameterException(message.getMessage());
		}

		// 不是必须
		return null;
	}
}
