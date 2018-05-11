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
import java.util.Comparator;
import java.util.List;

import org.treediagram.nina.memcache.IEntity;

/**
 * 缓存内容的查找器
 * 
 * @author kidal
 * 
 */
public interface CacheFinder<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> {
	/**
	 * 获取符合条件的缓存实体集合
	 * 
	 * @param filter 实体过滤器
	 * @return 不会返回null
	 */
	public List<T> find(Filter<T> filter);

	/**
	 * 将全部实体按指定排序规则排序并返回
	 * 
	 * @param comparator 排序器
	 * @return 不会返回null
	 */
	public List<T> sort(Comparator<T> comparator);

	/**
	 * 获取符合条件的缓存实体的有序集合
	 * 
	 * @param filter 实体过滤器
	 * @param comparator 排序器
	 * @return 不会返回null
	 */
	public List<T> find(Filter<T> filter, Comparator<T> comparator);

	/**
	 * 获取缓存中的全部实体集合
	 * 
	 * @return
	 */
	public List<T> all();
}
