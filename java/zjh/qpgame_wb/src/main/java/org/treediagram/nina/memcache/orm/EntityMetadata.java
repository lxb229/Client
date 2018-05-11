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

package org.treediagram.nina.memcache.orm;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.treediagram.nina.memcache.IEntity;

/**
 * 实体元数据
 * 
 * @author kidal
 * 
 */
public interface EntityMetadata {
	/**
	 * 实体名
	 */
	public String getEntityName();

	/**
	 * 实体字段
	 */
	public Map<String, String> getFields();

	/**
	 * 表明
	 */
	public String getName();

	/**
	 * 主键
	 */
	public String getPrimaryKey();

	/**
	 * 索引字段
	 */
	public Collection<String> getIndexKeys();

	/**
	 * 版本字段
	 */
	public String getVersionKey();

	/**
	 * 主键类型
	 */
	public <PK extends Serializable> Class<PK> getPrimaryKeyClass();

	/**
	 * 实体类型
	 */
	@SuppressWarnings("rawtypes")
	public <T extends IEntity> Class<T> getEntityClass();
}
