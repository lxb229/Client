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
import java.util.Collection;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.exception.InvaildEntityException;
import org.treediagram.nina.memcache.model.MemcachedConfiguration;
import org.treediagram.nina.memcache.writeback.Writer;

/**
 * 实体索引缓存
 * 
 * @author kidal
 * 
 */
public interface EntityIndexMemcache<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> {
	/**
	 * 加载指定区域的实体集合
	 * 
	 * @param idx 索引值
	 * @return 不可修改的列表
	 */
	Collection<T> load(IndexValue idx);

	/**
	 * 加载指定主键的实体(半异步)
	 * 
	 * @param idx 索引值
	 * @param id 主键
	 * @param builder 实体不存在时的实体创建器，允许为null
	 * @return 不会返回null
	 * @throws InvaildEntityException 无法创建合法的实体时抛出
	 * @throws UniqueFieldException 实体的唯一属性域值重复时抛出
	 */
	T getOrCreate(IndexValue idx, PK id, EntityBuilder<PK, T> builder);

	/**
	 * 获取某一主键的实体
	 * 
	 * @param idx
	 * @param id
	 * @return 实体不存在会返回null
	 */
	T get(IndexValue idx, PK id);

	/**
	 * 创建新的实体
	 * 
	 * @param entity
	 * @return 被增强过的实体实例
	 */
	T create(T entity);

	/**
	 * 移除指定实体(异步)
	 * 
	 * @param entity
	 */
	void remove(T entity);

	/**
	 * 清除指定的区域缓存
	 * 
	 * @param idx
	 */
	void clear(IndexValue idx);

	/**
	 * 清理全部缓存数据
	 */
	void truncate();

	/**
	 * 将缓存中的指定实体回写到存储层(异步)
	 * 
	 * @param T 回写实体实例
	 */
	void writeBack(T entity);

	/**
	 * 获取缓存配置
	 */
	MemcachedConfiguration getConfiguration();

	/**
	 * 获取写入器
	 */
	Writer getWriter();
}
