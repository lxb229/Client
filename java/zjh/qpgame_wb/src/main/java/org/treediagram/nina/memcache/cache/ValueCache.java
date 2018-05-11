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

package org.treediagram.nina.memcache.cache;

/**
 * 值缓存
 * 
 * @author kidal
 * 
 */
public interface ValueCache {
	/**
	 * 获取值
	 * 
	 * @param key 键
	 * @return 值
	 */
	Object get(Object key);

	/**
	 * 修改值
	 * 
	 * @param key 键
	 * @param value 值
	 */
	void put(Object key, Object value);

	/**
	 * 移除值
	 * 
	 * @param key 键
	 */
	void remove(Object key);
}
