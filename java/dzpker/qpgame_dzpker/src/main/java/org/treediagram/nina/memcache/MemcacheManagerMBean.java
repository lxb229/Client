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

package org.treediagram.nina.memcache;

import java.util.Map;

import org.treediagram.nina.memcache.model.MemcachedConfiguration;

/**
 * 内存缓存管理器 MBean
 * 
 * @author kidalsama
 * 
 */
public interface MemcacheManagerMBean {
	/**
	 * 获取全部写入器信息
	 */
	Map<String, Map<String, String>> getAllWriterInfo();

	/**
	 * 获取写入器信息
	 */
	Map<String, String> getWriterInfo(String name);

	/**
	 * 获取全部的缓存实体配置信息
	 */
	Map<String, MemcachedConfiguration> getAllMemcachedConfiguration();

	/**
	 * 获取缓存的实体个数
	 */
	Map<String, Integer> getCachedEntityCount();

	/**
	 * 获取缓存的索引实体个数
	 */
	Map<String, Map<String, Integer>> getCachedIndexEntityCount();
}
