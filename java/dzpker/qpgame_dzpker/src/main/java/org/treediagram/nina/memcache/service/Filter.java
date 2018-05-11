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

package org.treediagram.nina.memcache.service;

import org.treediagram.nina.memcache.IEntity;

/**
 * 查询结果过滤器
 * 
 * @author kidal
 * 
 * @param <T>
 */
public interface Filter<T extends IEntity<?>> {
	/**
	 * 检查是否排除该实体
	 * 
	 * @param entity 被检查的实体，不会为null
	 * @return true:排除被检查的实体(不会出现在返回结果中),false:不排除被检查的实体(会出现在返回结果中)
	 */
	public boolean isExclude(T entity);
}
