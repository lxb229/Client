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

package org.treediagram.nina.network.schema;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.treediagram.nina.network.aspect.SendAspect;
import org.treediagram.nina.network.client.ClientFactory;
import org.w3c.dom.Element;

/**
 * 客户端豆子定义解析器
 * 
 * @author kidal
 * 
 */
public class ClientBeanDefinitionParser extends AbstractBeanDefinitionParser {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(ClientBeanDefinitionParser.class);

	/**
	 * {@link AbstractBeanDefinitionParser#parse(Element, ParserContext)}
	 */
	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		// 注册拦截切面
		if (Boolean.valueOf(element.getAttribute("sendAspect"))) {
			registerSendAspect(parserContext);
		}

		// 注册客户端工厂
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ClientFactory.class);
		// 配置
		builder.addPropertyValue("config", parseConfig(element, parserContext));
		// 完成
		return builder.getBeanDefinition();
	}

	/**
	 * 注册拦截切面
	 */
	private void registerSendAspect(ParserContext parserContext) {
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		String name = StringUtils.uncapitalize(SendAspect.class.getSimpleName());
		BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SendAspect.class);

		registry.registerBeanDefinition(name, factory.getBeanDefinition());
	}

	/**
	 * 解析配置
	 */
	private Object parseConfig(Element element, ParserContext parserContext) {
		element = getUniqueChildElementByTagName(element, "config");

		if (element.hasAttribute("ref")) {
			String beanName = element.getAttribute("ref");
			return new RuntimeBeanReference(beanName);
		} else {
			String className = element.getAttribute("class");
			try {
				Class<?> cls = Class.forName(className);
				return cls.newInstance();
			} catch (Exception e) {
				FormattingTuple message = MessageFormatter.format("无法创建配置 {} 的实例", className);
				logger.error(message.getMessage());
				throw new RuntimeException(message.getMessage(), e);
			}
		}
	}

	/**
	 * 获取唯一的子元素
	 */
	private Element getUniqueChildElementByTagName(Element parent, String tagName) {
		List<Element> elements = DomUtils.getChildElementsByTagName(parent, tagName);
		if (elements.size() != 1) {
			FormattingTuple message = MessageFormatter.format("Tag Name {} 的元素数量 {} 不唯一", tagName, elements.size());
			logger.error(message.getMessage());
			throw new RuntimeException(message.getMessage());
		} else {
			return elements.get(0);
		}
	}
}
