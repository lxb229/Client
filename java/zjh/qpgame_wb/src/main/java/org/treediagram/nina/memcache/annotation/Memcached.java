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

package org.treediagram.nina.memcache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.treediagram.nina.memcache.model.MemcachedType;
import org.treediagram.nina.memcache.model.MemcachedUnit;

/**
 * 内存缓存实体声明
 * 
 * @author kidalsama
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Memcached {
	/**
	 * 缓存实体数量
	 */
	String size() default "default";

	/**
	 * 缓存初始化容量
	 */
	int initialCapacity() default 16;

	/**
	 * 预计并发更新线程数
	 */
	int concurrencyLevel() default 16;

	/**
	 * 缓存内存管理类型
	 */
	MemcachedType type() default MemcachedType.LRU;

	/**
	 * 回写策略
	 */
	String writebackPolicy() default "default";

	/**
	 * 是否增强
	 */
	boolean enhance() default true;

	/**
	 * 内存缓存单位
	 */
	MemcachedUnit unit() default MemcachedUnit.ENTITY;
}
