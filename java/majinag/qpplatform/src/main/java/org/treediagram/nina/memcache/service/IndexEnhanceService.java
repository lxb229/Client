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
import org.treediagram.nina.memcache.model.MemcachedConfiguration;

/**
 * 索引增强服务接口
 * 
 * @author kidal
 * 
 */
public interface IndexEnhanceService<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> extends
		EnhanceService<PK, T> {
	/**
	 * 修改索引值的方法
	 * 
	 * @param name 索引属性名
	 * @param entity 修改的实体
	 * @param prev 之前的值
	 */
	void changeIndexValue(String name, T entity, Object prev);

	/**
	 * 获取实体配置
	 */
	MemcachedConfiguration getEntityConfig();
}
