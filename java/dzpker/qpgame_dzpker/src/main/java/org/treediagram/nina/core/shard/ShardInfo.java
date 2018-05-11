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

package org.treediagram.nina.core.shard;

/**
 * 分片信息
 * 
 * @author kidal
 *
 */
public class ShardInfo<T> {
	/**
	 * 默认权重
	 */
	public static final int DEFAULT_WEIGHT = 1;

	/**
	 * 分片名
	 */
	private final String name;
	/**
	 * 权重
	 */
	private final int weight;
	/**
	 * 目标
	 */
	private final T target;

	/**
	 * 创建分片信息
	 * 
	 * @param target 分片目标
	 */
	public ShardInfo(T target) {
		this(null, DEFAULT_WEIGHT, target);
	}

	/**
	 * 创建分片信息
	 * 
	 * @param weight 权重
	 * @param target 分片目标
	 */
	public ShardInfo(int weight, T target) {
		this(null, weight, target);
	}

	/**
	 * 创建分片信息
	 * 
	 * @param name 分片名
	 * @param weight 权重
	 * @param target 分片目标
	 */
	public ShardInfo(String name, int weight, T target) {
		super();
		this.name = name;
		this.weight = weight;
		this.target = target;
	}

	/**
	 * 获取分片名
	 * 
	 * @return 分片名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取权重
	 * 
	 * @return 权重
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * 获取分片目标
	 * 
	 * @return 分片目标
	 */
	public T getTarget() {
		return target;
	}
}
