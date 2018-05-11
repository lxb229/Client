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

/**
 * 配置名定义
 * 
 * @author kidal
 */
public interface SchemaNames {
	/**
	 * 配置元素
	 */
	String CONFIG_ELEMENT = "config";

	/**
	 * 包声明元素
	 */
	String PACKAGE_ELEMENT = "package";

	/**
	 * 类声明元素
	 */
	String CLASS_ELEMENT = "class";

	/**
	 * 资源格式声明元素
	 */
	String FORMAT_ELEMENT = "format";

	/**
	 * 名称属性
	 */
	String PACKAGE_ATTRIBUTE_NAME = "name";

	/**
	 * 名称属性
	 */
	String CLASS_ATTRIBUTE_NAME = "name";

	/**
	 * 资源位置
	 */
	String FORMAT_ATTRIBUTE_LOCATION = "location";

	/**
	 * 资源类型
	 */
	String FORMAT_ATTRIBUTE_TYPE = "type";

	/**
	 * 资源后缀
	 */
	String FORMAT_ATTRIBUTE_SUFFIX = "suffix";

	/**
	 * 资源配置信息
	 */
	String FORMAT_ATTRIBUTE_CONFIG = "config";
}
