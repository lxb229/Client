package com.palmjoys.yf1b.act.framework.socket;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.core.convert.ConversionService;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.codec.Coder;
import org.treediagram.nina.network.codec.JsonCoder;
import org.treediagram.nina.network.command.FacadeMethodInvokeContext;
import org.treediagram.nina.network.command.parameter.InBodyParameter;
import org.treediagram.nina.network.exception.CoderDecodeException;
import org.treediagram.nina.network.exception.CoderEncodeException;
import org.treediagram.nina.network.exception.ParameterException;

/**
 * 
 * @author kidal
 *
 */
public class JsonBase64Coder implements Coder {
	/**
	 * 日志
	 */
	private final Logger logger = LoggerFactory.getLogger(JsonCoder.class);

	/**
	 * {@link Coder#decode(byte[])}
	 */
	@Override
	public Object decode(byte[] bytes) {
		try {
			final byte[] jsonBytes = Base64.decodeBase64(bytes);
			final String jsonString = new String(jsonBytes, "UTF-8");
			return JsonUtils.string2Map(jsonString.replace("[]", "null"));
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
			final byte[] bodyBytes = JsonUtils.object2String(object).replace("[]", "null").getBytes("UTF-8");
			final byte[] base64Bytes = Base64.encodeBase64(bodyBytes);
			return base64Bytes;
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
