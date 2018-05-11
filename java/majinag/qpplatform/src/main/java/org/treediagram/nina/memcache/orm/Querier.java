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

import java.util.List;

/**
 * 物理存储层的数据查询器接口<br/>
 * 用于给不同的ORM底层实现
 * 
 * @author kidal
 */
public interface Querier {

	/**
	 * 获取全部实体
	 * 
	 * @param clz 实体类型
	 * @return
	 */
	<T> List<T> all(Class<T> clz);

	/**
	 * 获取指定命名查询的查询结果(返回实体列表)
	 * 
	 * @param clz 实体类型
	 * @param queryname 查询名
	 * @param params 查询参数
	 * @return
	 */
	<T> List<T> list(Class<T> clz, String queryname, Object... params);

	/**
	 * 获取指定命名查询的查询结果(返回投影内容列表)
	 * 
	 * @param entityClz 实体类型
	 * @param retClz 返回结果的元素类型
	 * @param queryname 查询名
	 * @param params 查询参数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	<E> List<E> list(Class entityClz, Class<E> retClz, String queryname, Object... params);

	/**
	 * 获取指定命名查询的查询结果(返回单一的实体对象实例)
	 * 
	 * @param clz 实体类型
	 * @param queryname 查询名
	 * @param params 查询参数
	 * @return 实体对象或null
	 * @throws NonUniqueResultException 查询结果超过一个时抛出
	 */
	<T> T unique(Class<T> clz, String queryname, Object... params);

	/**
	 * 获取指定命名查询的查询结果(返回单一的投影内容)
	 * 
	 * @param entityClz 实体类型
	 * @param retClz 返回结果的类型
	 * @param queryname 查询名
	 * @param params 查询参数
	 * @return
	 * @throws NonUniqueResultException 查询结果超过一个时抛出
	 */
	@SuppressWarnings("rawtypes")
	<E> E unique(Class entityClz, Class<E> retClz, String queryname, Object... params);

	/**
	 * 分页查询(返回实体列表)
	 * 
	 * @param clz 实体类型
	 * @param queryname 查询名
	 * @param paging 分页参数
	 * @param params 查询参数
	 * @return
	 */
	<T> List<T> paging(Class<T> clz, String queryname, Paging paging, Object... params);

	/**
	 * 分页查询(返回投影内容列表)
	 * 
	 * @param entityClz 实体类型
	 * @param retClz 返回结果的类型
	 * @param queryname 查询名
	 * @param paging 分页参数
	 * @param params 查询参数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	<E> List<E> paging(Class entityClz, Class<E> retClz, String queryname, Paging paging, Object... params);
	
	@SuppressWarnings("rawtypes")
	<E> List<E> listByLimit(Class entityClz, Class<E> retClz, String queryname, int first, int count,  Object... params);
	@SuppressWarnings("rawtypes")
	<E> List<E> listBySqlLimit(Class entityClz, Class<E> retClz, String querySql, int first, int count);
	
	/**
	 * 查询执行
	 * 
	 * @param entityClz 实体类型
	 * @param queryname 查询名
	 * @param params 查询参数
	 */
	@SuppressWarnings("rawtypes")
	int execute(Class entityClz, String queryname, Object... params);
}
