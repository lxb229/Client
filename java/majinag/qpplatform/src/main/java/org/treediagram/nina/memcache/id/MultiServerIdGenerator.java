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

package org.treediagram.nina.memcache.id;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 多服的主键生成器
 * 
 * @author kidal
 */
public final class MultiServerIdGenerator {
	/**
	 * 主键生成器映射
	 */
	private ConcurrentMap<Integer, IdGenerator> generators = new ConcurrentHashMap<Integer, IdGenerator>();

	/**
	 * 主键生成器映射键值
	 */
	private int getKey(short channel, short zone) {
		return (channel << 12) + zone;
	}

	/**
	 * 是否初始化
	 */
	public boolean isInit(short channel, short zone) {
		int key = getKey(channel, zone);

		if (generators.containsKey(key)) {
			return true;
		}

		return false;
	}

	/**
	 * 添加指定分区标识的主键生成器
	 * 
	 * @param zone 分区标识
	 * @param max 当前的主键最大值
	 */
	public void init(short channel, short zone, Long max) {
		int key = getKey(channel, zone);

		if (generators.containsKey(key)) {
			throw new IllegalStateException("渠道[" + channel + "]分区[" + zone + "]的主键生成器已存在");
		}

		IdGenerator generator = new IdGenerator(channel, zone, max);
		generators.putIfAbsent(key, generator);
	}

	/**
	 * 获取下一个自增主键
	 * 
	 * @param zone 分区标识
	 */
	public long getNext(short channel, short zone) {
		int key = getKey(channel, zone);
		IdGenerator generator = generators.get(key);

		if (generator == null) {
			throw new IllegalStateException("渠道[" + channel + "]分区[" + zone + "]的主键生成器不存在");
		}

		return generator.getNext();
	}
}
