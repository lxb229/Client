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

package org.treediagram.nina.memcache.schema;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedMap;
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
import org.springframework.util.xml.DomUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Memcached;
import org.treediagram.nina.memcache.exception.MemcacheConfigurationException;
import org.treediagram.nina.memcache.writeback.WriterConfig;
import org.treediagram.nina.memcache.writeback.WriterType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 内存缓存 bean 定义解析器
 * 
 * @author kidalsama
 * 
 */
public class MemcacheBeanDefinitionParser extends AbstractBeanDefinitionParser
{
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(MemcacheBeanDefinitionParser.class);

	/**
	 * 默认资源匹配符
	 */
	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	/**
	 * 资源搜索分析器，由它来负责检索EAO接口
	 */
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	/**
	 * 类的元数据读取器，由它来负责读取类上的注释信息
	 */
	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

	/**
	 * {@link AbstractBeanDefinitionParser#parseInternal(Element, ParserContext)}
	 */
	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		// 注册镜像缓存注入处理器
		registerMemcacheInjectProcessor(parserContext);

		// 创建工程定义
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(MemcacheManagerFactory.class);
		builder.addPropertyReference(ElementNames.ACCESSOR, getAccessorBeanName(element));
		builder.addPropertyReference(ElementNames.QUERIER, getQuerierBeanName(element));
		addConstants(element, builder);
		addWritebackPolicyValue(element, builder);
		addEntityPropertyValue(element, builder);

		// 返回 bean 定义
		return builder.getBeanDefinition();
	}

	/**
	 * 添加常量
	 */
	private void addConstants(Element element, BeanDefinitionBuilder builder) {
		// 获取元素
		element = DomUtils.getChildElementByTagName(element, ElementNames.CONSTANTS);

		// 尝试注册引用
		String ref = element.getAttribute(AttributeNames.REF);
		if (StringUtils.isNotBlank(ref)) {
			builder.addPropertyReference(ElementNames.CONSTANTS, ref);
			return;
		}

		// 指定设置
		ManagedMap<String, Integer> constants = new ManagedMap<String, Integer>();
		for (Element e : DomUtils.getChildElementsByTagName(element, ElementNames.CONSTANT)) {
			String name = e.getAttribute(AttributeNames.NAME);
			int size = Integer.parseInt(e.getAttribute(AttributeNames.SIZE));

			constants.put(name, size);
		}
		builder.addPropertyValue(ElementNames.CONSTANTS, constants);
	}

	/**
	 * 添加回写策略属性值
	 */
	private void addWritebackPolicyValue(Element element, BeanDefinitionBuilder builder) 
	{
		Map<String, WriterConfig> writerConfigs = new HashMap<String, WriterConfig>();
		Element writerElement = DomUtils.getChildElementByTagName(element, ElementNames.WRITEBACK_POLICY);
		WriterType type = WriterType.valueOf(writerElement.getAttribute(AttributeNames.TYPE));
		String value = writerElement.getAttribute(AttributeNames.CONFIG);
		writerConfigs.put("default", new WriterConfig(type, value));

		for (Element e : DomUtils.getChildElementsByTagName(writerElement, ElementNames.WRITER)) {
			String name = e.getAttribute(AttributeNames.NAME);
			type = WriterType.valueOf(e.getAttribute(AttributeNames.TYPE));
			value = e.getAttribute(AttributeNames.CONFIG);
			writerConfigs.put(name, new WriterConfig(type, value));
		}

		builder.addPropertyValue("writerConfigs", writerConfigs);
	}

	/**
	 * 添加实体属性值
	 */
	private void addEntityPropertyValue(Element element, BeanDefinitionBuilder builder) {
		Element entityElement = DomUtils.getChildElementByTagName(element, ElementNames.ENTITY);
		if (entityElement != null) {
			NodeList childNodes = entityElement.getChildNodes();
			Set<Class<? extends IEntity<?>>> entityClasses = new HashSet<Class<? extends IEntity<?>>>();

			for (int i = 0; i < childNodes.getLength(); i++) {
				Node node = childNodes.item(i);

				if (node.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}

				String name = node.getLocalName();

				if (name.equals(ElementNames.PACKAGE)) {
					String packageName = ((Element) node).getAttribute(AttributeNames.NAME);
					String[] names = getResources(packageName);

					for (String resource : names) {
						Class<? extends IEntity<?>> entityClass = forEntityClass(resource);

						entityClasses.add(entityClass);
					}
				} else if (name.equals(ElementNames.CLASS)) {
					String className = ((Element) node).getAttribute(AttributeNames.NAME);
					Class<? extends IEntity<?>> entityClass = forEntityClass(className);

					entityClasses.add(entityClass);
				}
			}

			builder.addPropertyValue(MemcacheManagerFactory.ENTITY_CLASSES_NAME, entityClasses);

			if (logger.isDebugEnabled()) {
				for (Class<? extends IEntity<?>> entityClass : entityClasses) {
					logger.debug("向内存缓存注册实体:{}", entityClass);
				}
			}
		}
	}

	/**
	 * 载入实体类型
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends IEntity<?>> forEntityClass(String className) {
		try {
			return (Class<? extends IEntity<?>>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			FormattingTuple message = MessageFormatter.format("无法获取的资源类[{}]", className);
			logger.error(message.getMessage());
			throw new MemcacheConfigurationException(message.getMessage(), e);
		}
	}

	/**
	 * 注册镜像缓存注入处理器
	 */
	private void registerMemcacheInjectProcessor(ParserContext parserContext) {
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		String name = StringUtils.uncapitalize(MemcacheInjectProcessor.class.getSimpleName());
		BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(MemcacheInjectProcessor.class);

		registry.registerBeanDefinition(name, factory.getBeanDefinition());
	}

	/**
	 * 获取查询器名称
	 */
	private String getAccessorBeanName(Element element) {
		element = MemcacheParserHelper.getUniqueChildElementByTagName(element, ElementNames.ACCESSOR);

		if (element.hasAttribute(AttributeNames.REF)) {
			return element.getAttribute(AttributeNames.REF);
		}

		throw new MemcacheConfigurationException("访问器配置缺失");
	}

	/**
	 * 获取访问器名称
	 */
	private String getQuerierBeanName(Element element) {
		element = MemcacheParserHelper.getUniqueChildElementByTagName(element, ElementNames.QUERIER);

		if (element.hasAttribute(AttributeNames.REF)) {
			return element.getAttribute(AttributeNames.REF);
		}

		throw new MemcacheConfigurationException("查询器配置缺失");
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
			String name = Memcached.class.getName();
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
			throw new MemcacheConfigurationException(message, e);
		}
	}

	/**
	 * 解析基础包
	 */
	private String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}
}
