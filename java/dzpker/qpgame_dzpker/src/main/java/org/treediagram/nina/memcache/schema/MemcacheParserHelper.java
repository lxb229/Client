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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.xml.DomUtils;
import org.treediagram.nina.memcache.exception.MemcacheConfigurationException;
import org.w3c.dom.Element;

/**
 * 配置文件解析帮助类
 * 
 * @author kidalsama
 * 
 */
public abstract class MemcacheParserHelper {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(MemcacheConfigurationException.class);

	/**
	 * 检查是否有指定标签的子元素
	 * 
	 * @param parent 父元素节点
	 * @param tagName 标签名
	 */
	public static boolean hasChildElementsByTagName(Element parent, String tagName) {
		List<Element> elements = DomUtils.getChildElementsByTagName(parent, tagName);

		if (elements.size() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 获取唯一的子元素
	 * 
	 * @param parent 父元素节点
	 * @param tagName 标签名
	 */
	public static Element getUniqueChildElementByTagName(Element parent, String tagName) {
		List<Element> elements = DomUtils.getChildElementsByTagName(parent, tagName);

		if (elements.size() != 1) {
			FormattingTuple message = MessageFormatter.format("Tag Name[{}]的元素数量[{}]不唯一", tagName, elements.size());
			logger.error(message.getMessage());
			throw new MemcacheConfigurationException(message.getMessage());
		}

		return elements.get(0);
	}
}
