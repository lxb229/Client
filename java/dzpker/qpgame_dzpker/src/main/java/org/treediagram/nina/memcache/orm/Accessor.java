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

import java.util.Collection;
import java.util.Map;

import org.treediagram.nina.memcache.IEntity;

/**
 * 物理存储层数据访问器接口<br/>
 * 用于给不同的ORM底层实现
 * 
 * @author kidal
 */
public interface Accessor {

	// CRUD 方法部分

	/**
	 * 从存储层加载指定的实体对象实例
	 * 
	 * @param clz 实体类型
	 * @param id 实体主键
	 * @return 实体实例,不存在应该返回null
	 */
	<PK, T extends IEntity<PK>> T load(Class<T> clazz, PK id);

	/**
	 * 持久化指定的实体实例,并返回实体的主键值对象
	 * 
	 * @param clz 实体类型
	 * @param entity 被持久化的实体实例(当持久化成功时,该实体的主键必须被设置为正确的主键值)
	 * @return 持久化实体的主键值对象
	 * @throws EntityExistsException 实体已经存在时抛出
	 * @throws DataException 实体数据不合法时抛出
	 */
	<PK, T extends IEntity<PK>> PK save(Class<T> clazz, T entity);

	/**
	 * 从存储层移除指定实体
	 * 
	 * @param clz 实体类型
	 * @param id 实体主键
	 */
	<PK, T extends IEntity<PK>> void remove(Class<T> clazz, PK id);

	/**
	 * 从存储层移除指定实体
	 * 
	 * @param clz 实体类型
	 * @param id 实体主键
	 */
	<PK, T extends IEntity<PK>> void remove(Class<T> clazz, T entity);

	/**
	 * 移除全部
	 */
	<PK, T extends IEntity<PK>> void removeAll(Class<T> clazz);

	/**
	 * 更新存储层的实体数据(不允许更新实体的主键值)
	 * 
	 * @param entity 被更新实体对象实例
	 * @param clz 实体类型
	 * @throws EntityNotFoundException 被更新实体在存储层不存在时抛出
	 */
	<PK, T extends IEntity<PK>> T update(Class<T> clazz, T entity);

	/**
	 * 查询存储层的实体数据
	 * 
	 * @param <PK>
	 * @param <T>
	 * @param clz 实体类型
	 * @param entities 填充的数据集合
	 * @param offset 查询偏移量
	 * @param size 查询数量
	 * @return 实体总数
	 */
	<PK, T extends IEntity<PK>> void listAll(Class<T> clazz, Collection<T> entities, Integer offset, Integer size);

	/**
	 * 按照指定条件交集(与查询)的方式查询存储层的实体数据
	 * 
	 * @param <PK>
	 * @param <T>
	 * @param clz 实体类型
	 * @param entities 填充的数据集合
	 * @param keyValue 查询条件
	 * @param offset 查询偏移量
	 * @param size 总数大小
	 * @return 实体总数
	 */
	<PK, T extends IEntity<PK>> void listIntersection(Class<T> clazz, Collection<T> entities,
			Map<String, Object> keyValue, Integer offset, Integer size);

	/**
	 * 按照指定条件并集(或查询)的方式查询存储层的实体数据
	 * 
	 * @param <PK>
	 * @param <T>
	 * @param clz 实体类型
	 * @param entities 填充的数据集合
	 * @param keyValue 查询条件
	 * @param offset 查询偏移量
	 * @param size 总数大小
	 * @return
	 */
	<PK, T extends IEntity<PK>> void listUnion(Class<T> clazz, Collection<T> entities, Map<String, Object> keyValue,
			Integer offset, Integer size);

	/**
	 * 获取实体记录总数(具体实现扩展方法)
	 * 
	 * @param clz
	 * @return
	 */
	public <PK, T extends IEntity<PK>> long countAll(Class<T> clz);

	/**
	 * 获取实体记录交集总数
	 * 
	 * @param clz
	 * @return
	 */
	public <PK, T extends IEntity<PK>> long countIntersection(Class<T> clz, Map<String, Object> keyValue);

	/**
	 * 获取实体记录并集总数
	 * 
	 * @param clz
	 * @return
	 */
	public <PK, T extends IEntity<PK>> long countUnion(Class<T> clz, Map<String, Object> keyValue);

	/**
	 * 元信息方法
	 * 
	 * @return
	 */
	Collection<EntityMetadata> getAllMetadata();
}
