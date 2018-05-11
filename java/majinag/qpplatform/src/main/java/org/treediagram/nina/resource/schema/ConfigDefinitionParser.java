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

package org.treediagram.nina.resource.schema;

import static org.treediagram.nina.resource.schema.SchemaNames.CLASS_ATTRIBUTE_NAME;
import static org.treediagram.nina.resource.schema.SchemaNames.PACKAGE_ATTRIBUTE_NAME;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.treediagram.nina.resource.StorageManagerFactory;
import org.treediagram.nina.resource.other.FormatDefinition;
import org.treediagram.nina.resource.other.ResourceDefinition;
import org.treediagram.nina.resource.reader.ExcelReader;
import org.treediagram.nina.resource.reader.JsonReader;
import org.treediagram.nina.resource.reader.ReaderHolder;
import org.treediagram.nina.resource.reader.ResourceReader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 配置定义处理器
 * 
 * @author kidal
 */
public class ConfigDefinitionParser extends AbstractBeanDefinitionParser {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(ConfigDefinitionParser.class);

	/**
	 * 默认资源匹配符
	 */
	protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	/**
	 * 资源搜索分析器，由它来负责检索EAO接口
	 */
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	/**
	 * 类的元数据读取器，由它来负责读取类上的注释信息
	 */
	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		FormatDefinition format = parseFormat(element);

		register(parserContext, format);

		// 要创建的对象信息
		BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(StorageManagerFactory.class);
		ManagedList<BeanDefinition> resources = new ManagedList<BeanDefinition>();

		// 检查XML内容
		NodeList child = element.getChildNodes();
		for (int i = 0; i < child.getLength(); i++) {
			Node node = child.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String name = node.getLocalName();

			if (name.equals(SchemaNames.PACKAGE_ELEMENT)) {
				// 自动包扫描处理
				String packageName = ((Element) node).getAttribute(PACKAGE_ATTRIBUTE_NAME);
				String[] names = getResources(packageName);
				for (String resource : names) {
					Class<?> clz = null;
					try {
						clz = Class.forName(resource);
					} catch (ClassNotFoundException e) {
						FormattingTuple message = MessageFormatter.format("无法获取的资源类[{}]", resource);
						logger.error(message.getMessage());
						throw new RuntimeException(message.getMessage(), e);
					}
					BeanDefinition definition = parseResource(clz, format);
					resources.add(definition);
				}
			}

			if (name.equals(SchemaNames.CLASS_ELEMENT)) {
				// 自动类加载处理
				String className = ((Element) node).getAttribute(CLASS_ATTRIBUTE_NAME);
				Class<?> clz = null;
				try {
					clz = Class.forName(className);
				} catch (ClassNotFoundException e) {
					FormattingTuple message = MessageFormatter.format("无法获取的资源类[{}]", className);
					logger.error(message.getMessage());
					throw new RuntimeException(message.getMessage(), e);
				}
				BeanDefinition definition = parseResource(clz, format);
				resources.add(definition);
			}
		}

		factory.addPropertyValue("definitions", resources);
		AbstractBeanDefinition definition = factory.getBeanDefinition();

		
		return definition;
	}

	/**
	 * 注册必须的环境资源
	 */
	private void register(ParserContext parserContext, FormatDefinition format) {
		registerDefaultReader(parserContext);
		registerReaderHolder(parserContext, format);
		registerStaticInject(parserContext);
	}

	/**
	 * 解析 XML 中的 format 元素
	 */
	private FormatDefinition parseFormat(Element element) {
		NodeList child = element.getChildNodes();
		for (int i = 0; i < child.getLength(); i++) {
			Node node = child.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String name = node.getLocalName();
			if (!name.equals(SchemaNames.FORMAT_ELEMENT)) {
				continue;
			}

			Element e = (Element) node;
			String type = e.getAttribute(SchemaNames.FORMAT_ATTRIBUTE_TYPE);
			String suffix = e.getAttribute(SchemaNames.FORMAT_ATTRIBUTE_SUFFIX);
			String location = e.getAttribute(SchemaNames.FORMAT_ATTRIBUTE_LOCATION);
			if (StringUtils.endsWith(location, File.pathSeparator)) {
				location = StringUtils.substringAfterLast(location, File.pathSeparator);
			}
			String config = e.hasAttribute(SchemaNames.FORMAT_ATTRIBUTE_CONFIG) ? e
					.getAttribute(SchemaNames.FORMAT_ATTRIBUTE_CONFIG) : null;
			return new FormatDefinition(location, type, suffix, config);
		}

		throw new RuntimeException("XML文件缺少[format]元素定义");
	}

	/**
	 * 注册 {@link StaticInjectProcessor}
	 */
	private void registerStaticInject(ParserContext parserContext) {
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		String name = StringUtils.uncapitalize(StaticInjectProcessor.class.getSimpleName());
		BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(StaticInjectProcessor.class);
		registry.registerBeanDefinition(name, factory.getBeanDefinition());
	}

	/**
	 * 注册 {@link ReaderHolder}
	 */
	private void registerReaderHolder(ParserContext parserContext, FormatDefinition format) {
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		String name = StringUtils.uncapitalize(ReaderHolder.class.getSimpleName());
		BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(ReaderHolder.class);
		factory.addPropertyValue(ReaderHolder.FORMAT_SETTER, format);
		registry.registerBeanDefinition(name, factory.getBeanDefinition());
	}

	/**
	 * 注册默认的 {@link ResourceReader}
	 */
	private void registerDefaultReader(ParserContext parserContext) {
		BeanDefinitionRegistry registry = parserContext.getRegistry();

		// 注册 ExcelReader
		String name = StringUtils.uncapitalize(ExcelReader.class.getSimpleName());
		BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(ExcelReader.class);
		registry.registerBeanDefinition(name, factory.getBeanDefinition());

		// 注册 JsonReader
		name = StringUtils.uncapitalize(JsonReader.class.getSimpleName());
		factory = BeanDefinitionBuilder.rootBeanDefinition(JsonReader.class);
		registry.registerBeanDefinition(name, factory.getBeanDefinition());
	}

	/**
	 * 解析资源定义
	 * 
	 * @param clz 资源类
	 * @param format 格式定义
	 */
	private BeanDefinition parseResource(Class<?> clz, FormatDefinition format) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ResourceDefinition.class);
		builder.addConstructorArgValue(clz);
		builder.addConstructorArgValue(format);
		return builder.getBeanDefinition();
	}

	/**
	 * 获取指定包下的静态资源对象
	 */
	private String[] getResources(String packageName) {
		try {
			// 搜索资源
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ resolveBasePackage(packageName) + "/" + DEFAULT_RESOURCE_PATTERN;
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			// 提取资源
			Set<String> result = new HashSet<String>();
			String name = org.treediagram.nina.resource.annotation.ResourceType.class.getName();
			for (Resource resource : resources) {
				if (!resource.isReadable()) {
					continue;
				}
				// 判断是否静态资源
				MetadataReader metaReader = this.metadataReaderFactory.getMetadataReader(resource);
				AnnotationMetadata annoMeta = metaReader.getAnnotationMetadata();
				if (!annoMeta.hasAnnotation(name)) {
					continue;
				}
				ClassMetadata clzMeta = metaReader.getClassMetadata();
				result.add(clzMeta.getClassName());
			}

			return result.toArray(new String[0]);
		} catch (IOException e) {
			String message = "无法读取资源信息";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}
}
