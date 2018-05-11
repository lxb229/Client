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

import java.io.Serializable;

import org.treediagram.nina.memcache.IEntity;

/**
 * 增强服务接口
 * 
 * @author kidal
 * 
 */
public interface EnhanceService<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> {
	/**
	 * 将缓存中的指定实体回写到存储层(异步)
	 * 
	 * @param id 主键
	 * @param T 回写实体实例
	 */
	void writeBack(T entity);
}
