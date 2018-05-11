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
import java.util.List;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.exception.InvaildEntityException;
import org.treediagram.nina.memcache.model.MemcachedConfiguration;
import org.treediagram.nina.memcache.writeback.Writer;

/**
 * 实体内存缓存
 * 
 * @author kidalsama
 * 
 */
public interface EntityMemcache<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> {
	/**
	 * 载入实体
	 * 
	 * @param id 组件
	 */
	T load(PK id);

	/**
	 * 加载指定主键的实体(半异步)
	 * 
	 * @param id 主键
	 * @param builder 实体不存在时的实体创建器，允许为null
	 * @return 不会返回null
	 * @throws InvaildEntityException 无法创建合法的实体时抛出
	 * @throws UniqueFieldException 实体的唯一属性域值重复时抛出
	 */
	T loadOrCreate(PK id, EntityBuilder<PK, T> builder);

	/**
	 * 使用命名查询载入
	 * 
	 * @param queryname 命名查询
	 * @param params 命名查询参数
	 * @return 载入的实体集合
	 */
	@Deprecated
	List<T> loadByQuery(String queryname, Object... params);

	/**
	 * 保存或者更新实体
	 * 
	 * @param entity 实体
	 */
	@Deprecated
	T saveOrUpdate(T entity);

	/**
	 * 将缓存中的指定实体回写到存储层(异步)
	 * 
	 * @param T 回写实体实例
	 */
	void writeBack(T entity);

	/**
	 * 移除并删除指定实体(异步)
	 * 
	 * @param id 主键
	 * @return 缓存中与实体主键相关联的旧实例；如果没有则返回 null。
	 */
	T remove(PK id);

	/**
	 * 清理指定主键的缓存数据
	 * 
	 * @param id 主键
	 */
	void clear(PK id);

	/**
	 * 清理全部缓存数据
	 */
	void truncate();

	/**
	 * 获取对应的缓存实体查询器
	 */
	CacheFinder<PK, T> getFinder();

	/**
	 * 获取缓存配置
	 */
	MemcachedConfiguration getConfiguration();

	/**
	 * 获取写入器
	 */
	Writer getWriter();
}
