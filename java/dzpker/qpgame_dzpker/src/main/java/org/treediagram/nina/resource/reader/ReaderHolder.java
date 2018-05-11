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

package org.treediagram.nina.resource.reader;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.treediagram.nina.resource.other.FormatDefinition;

/**
 * 资源读取器持有者
 * 
 * @author kidal
 */
public class ReaderHolder implements ApplicationContextAware {

	public final static String FORMAT_SETTER = "format";

	/**
	 * 格式定义信息
	 */
	private FormatDefinition format;
	private ConcurrentHashMap<String, ResourceReader> readers = new ConcurrentHashMap<String, ResourceReader>();

	public ReaderHolder ()
	{
		
	}
	@PostConstruct
	protected void initialize() {
		for (String name : this.applicationContext.getBeanNamesForType(ResourceReader.class)) {
			ResourceReader reader = this.applicationContext.getBean(name, ResourceReader.class);
			this.register(reader);
		}
	}

	/**
	 * 获取指定格式的 {@link ResourceReader}
	 */
	public ResourceReader getReader(String format) {
		return readers.get(format);
	}

	/**
	 * 注册指定的 {@link ResourceReader}
	 */
	public ResourceReader register(ResourceReader reader) {
		if (reader.getFormat().equals(format.getType()) && format.getConfig() != null) {
			reader.config(format.getConfig());
		}
		return readers.putIfAbsent(reader.getFormat(), reader);
	}

	// 实现 {@link ApplicationContextAware}

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setFormat(FormatDefinition format) {
		this.format = format;
	}
}
