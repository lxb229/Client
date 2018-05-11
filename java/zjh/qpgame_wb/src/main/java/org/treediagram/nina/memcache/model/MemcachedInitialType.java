/*
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

package org.treediagram.nina.memcache.model;

/**
 * 实体内存缓存初始化类型
 * 
 * @author kidalsama
 * 
 */
public enum MemcachedInitialType {
	/**
	 * 载入全部
	 */
	LOAD_ALL,

	/**
	 * 使用查询
	 */
	USE_QUERY,
}
