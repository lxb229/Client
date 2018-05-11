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

/**
 * Schema 元素名
 * 
 * @author kidalsama
 * 
 */
public interface ElementNames {
	/**
	 * 配置
	 */
	public String CONFIG = "config";

	/**
	 * 实体定义元素
	 */
	String ENTITY = "entity";

	/**
	 * 包定义元素
	 */
	String PACKAGE = "package";

	/**
	 * 定义实体元素
	 */
	String CLASS = "class";

	/**
	 * 访问器
	 */
	String ACCESSOR = "accessor";

	/**
	 * 查询器
	 */
	String QUERIER = "querier";

	/**
	 * 常量
	 */
	String CONSTANT = "constant";

	/**
	 * 常量
	 */
	String CONSTANTS = "constants";

	/**
	 * 回写策略
	 */
	String WRITEBACK_POLICY = "writebackPolicy";

	/**
	 * 回写器
	 */
	String WRITER = "writer";
}
