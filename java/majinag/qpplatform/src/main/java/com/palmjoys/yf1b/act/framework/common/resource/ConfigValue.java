package com.palmjoys.yf1b.act.framework.common.resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.core.convert.ConversionService;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.resource.annotation.InjectBean;
import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

import com.palmjoys.yf1b.act.framework.common.model.ConfigValueType;

/**
 * 值配置对象<br/>
 * 用于配置系统中的一些配置值
 * 
 * @author frank
 */
@ResourceType("common")
@SuppressWarnings("rawtypes")
public class ConfigValue<T> {

	private static final Logger logger = LoggerFactory.getLogger(ConfigValue.class);

	@InjectBean
	private static ConversionService conversionService;

	/** 标识 */
	@ResourceId
	private String id;
	/** 配置值 */
	private String content;
	/** 配置值格式类型 */
	private ConfigValueType type;
	/** 值类型参数 */
	private Class clz;

	@SuppressWarnings("unused")
	private String explain;

	/** 值 */
	private transient volatile T value;

	/**
	 * 获取配置值
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getValue() {
		if (value != null) {
			return value;
		}
		synchronized (this) {
			if (value == null) {
				try {
					switch (type) {
						case NORMAL :
							value = (T) conversionService.convert(content, clz);
							break;
						case JSON :
							if (StringUtils.startsWith(content, "[")) {
								if (clz.isArray()) {
									value = (T) JsonUtils.string2Object(content, clz);
								} else {
									value = (T) JsonUtils.string2Array(content, clz);
								}
							} else {
								value = (T) JsonUtils.string2Object(content, clz);
							}
					}
				} catch (Exception e) {
					FormattingTuple message = MessageFormatter.format("值配置对象[{}]的配置值[{}]配置有误", id, content);
					logger.error(message.getMessage(), e);
					throw new IllegalArgumentException(message.getMessage(), e);
				}
			}
		}
		return value;
	}

	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public Class getClz() {
		return clz;
	}

}
