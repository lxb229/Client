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

import org.apache.mina.core.filterchain.IoFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.treediagram.nina.network.codec.Coder;
import org.treediagram.nina.network.codec.CoderManager;
import org.treediagram.nina.network.handler.ServerHandler;
import org.treediagram.nina.network.server.SocketServer;
import org.w3c.dom.Element;

/**
 * 服务器豆子定义解析器
 * 
 * @author kidal
 * 
 */
public class ServerBeanDefinitionParser extends AbstractBeanDefinitionParser {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(ServerBeanDefinitionParser.class);

	/**
	 * {@link AbstractBeanDefinitionParser#parseInternal(Element, ParserContext)}
	 */
	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		// 编码解码器
		RuntimeBeanReference coders = parseCoders(element, parserContext);

		// 服务器
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SocketServer.class);
		builder.addPropertyValue("config", parseConfig(element, parserContext));
		builder.addPropertyValue("acceptor", parseAcceptor(element, parserContext));
		builder.addPropertyValue("handler", parseHandler(element, parserContext, coders));
		builder.addPropertyValue("filters", parseFilters(element, parserContext));
		return builder.getBeanDefinition();
	}

	/**
	 * 解析编码解码器
	 */
	private RuntimeBeanReference parseCoders(Element element, ParserContext parserContext) {
		String id = element.getAttribute("id");
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(CoderManager.class);
		builder.addConstructorArgValue(getCoders(element, parserContext));
		String name = id + "-coderManager";
		registry.registerBeanDefinition(name, builder.getBeanDefinition());
		return new RuntimeBeanReference(name);
	}

	/**
	 * 获取编码解码器
	 */
	private ManagedMap<Integer, Object> getCoders(Element element, ParserContext parserContext) {
		element = getUniqueChildElementByTagName(element, "coders");

		ManagedMap<Integer, Object> managedMap = new ManagedMap<Integer, Object>();
		managedMap.setValueTypeName(Coder.class.getName());
		managedMap.setSource(parserContext.extractSource(element));

		List<Element> elements = DomUtils.getChildElementsByTagName(element, "coder");
		for (Element e : elements) {
			String name = e.getAttribute("name");
			Integer format = Integer.parseInt(name);

			if (e.hasAttribute("ref")) {
				String beanName = e.getAttribute("ref");
				managedMap.put(format, new RuntimeBeanReference(beanName));
			} else {
				String className = e.getAttribute("class");
				try {
					Class<?> cls = Class.forName(className);
					managedMap.put(format, cls.newInstance());
				} catch (Exception exception) {
					FormattingTuple message = MessageFormatter.format("无法创建编码解码器 {} 的实例", className);
					logger.error(message.getMessage());
					throw new RuntimeException(message.getMessage(), exception);
				}
			}
		}

		return managedMap;
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
	 * 解析接收器
	 */
	private Object parseAcceptor(Element element, ParserContext parserContext) {
		element = getUniqueChildElementByTagName(element, "acceptor");

		if (element.hasAttribute("ref")) {
			String beanName = element.getAttribute("ref");
			return new RuntimeBeanReference(beanName);
		} else {
			String className = element.getAttribute("class");
			try {
				Class<?> cls = Class.forName(className);
				return cls.newInstance();
			} catch (Exception e) {
				FormattingTuple message = MessageFormatter.format("无法创建接收器 {} 的实例", className);
				logger.error(message.getMessage());
				throw new RuntimeException(message.getMessage(), e);
			}
		}
	}

	/**
	 * 解析处理器
	 */
	private RuntimeBeanReference parseHandler(Element element, ParserContext parserContext, RuntimeBeanReference coders) {
		RuntimeBeanReference commandsFactory = parseCommandsFactory(element, parserContext, coders);

		String id = element.getAttribute("id");
		String beanName = id + "-handler";

		BeanDefinitionRegistry registry = parserContext.getRegistry();
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ServerHandler.class);
		builder.addConstructorArgValue(commandsFactory);
		builder.addConstructorArgValue(coders);
		registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
		return new RuntimeBeanReference(beanName);
	}

	/**
	 * 解析命令集合工厂
	 */
	private RuntimeBeanReference parseCommandsFactory(Element element, ParserContext parserContext,
			RuntimeBeanReference coders) {
		String id = element.getAttribute("id");
		String beanName = id + "-register";

		BeanDefinitionRegistry registry = parserContext.getRegistry();
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SocketCommandsFacotry.class);
		builder.addConstructorArgValue(coders);
		registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
		return new RuntimeBeanReference(beanName);
	}

	/**
	 * 解析过滤器
	 */
	private ManagedMap<String, Object> parseFilters(Element element, ParserContext parserContext) {
		if (!hasChildElementsByTagName(element, "filters")) {
			return null;
		} else {
			element = getUniqueChildElementByTagName(element, "filters");

			ManagedMap<String, Object> managedMap = new ManagedMap<String, Object>();
			managedMap.setValueTypeName(IoFilter.class.getName());
			managedMap.setSource(parserContext.extractSource(element));

			List<Element> elements = DomUtils.getChildElementsByTagName(element, "filter");
			for (Element e : elements) {
				String name = e.getAttribute("name");

				if (e.hasAttribute("ref")) {
					String beanName = e.getAttribute("ref");
					managedMap.put(name, new RuntimeBeanReference(beanName));
				} else {
					String className = e.getAttribute("class");
					try {
						Class<?> cls = Class.forName(className);
						managedMap.put(name, cls.newInstance());
					} catch (Exception exception) {
						FormattingTuple message = MessageFormatter.format("无法创建过滤器 {} 的实例", className);
						logger.error(message.getMessage());
						throw new RuntimeException(message.getMessage(), exception);
					}
				}
			}
			return managedMap;
		}
	}

	/**
	 * 检查是否有指定标签的子元素
	 */
	private boolean hasChildElementsByTagName(Element parent, String tagName) {
		List<Element> elements = DomUtils.getChildElementsByTagName(parent, tagName);
		if (elements.size() > 0) {
			return true;
		} else {
			return false;
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
