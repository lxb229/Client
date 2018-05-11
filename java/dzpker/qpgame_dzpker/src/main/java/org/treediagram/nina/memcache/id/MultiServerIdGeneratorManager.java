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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.orm.Querier;

/**
 * 多游戏服主键生成器管理器
 * 
 * @author kidal
 * 
 */
@SuppressWarnings("rawtypes")
public class MultiServerIdGeneratorManager {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(MultiServerIdGeneratorManager.class);

	/**
	 * 查询器
	 */
	@Autowired
	private Querier querier;

	/**
	 * 编号生成器
	 */
	private ConcurrentMap<Class, MultiServerIdGenerator> generators = new ConcurrentHashMap<Class, MultiServerIdGenerator>();

	/**
	 * 查询名
	 */
	private ConcurrentMap<Class, String> queryNames = new ConcurrentHashMap<Class, String>();

	public MultiServerIdGeneratorManager (){
	}
	/**
	 * 初始化
	 */
	public void init(Class<? extends IEntity> entityClass, String queryName) {
		if (generators.containsKey(entityClass)) {
			throw new IllegalStateException("实体" + entityClass.getName() + "的主键生成器已经存在");
		}

		generators.putIfAbsent(entityClass, new MultiServerIdGenerator());
		queryNames.putIfAbsent(entityClass, queryName);
	}

	/**
	 * 获取下一个主键
	 */
	public long getNext(Class<? extends IEntity> entityClass, short channel, short zone) {
		return getIdGenerator(entityClass, channel, zone).getNext(channel, zone);
	}

	/**
	 * 获取主键生成器
	 */
	private MultiServerIdGenerator getIdGenerator(Class<? extends IEntity> entityClass, short channel, short zone) {
		MultiServerIdGenerator generator = generators.get(entityClass);

		if (generator == null) {
			throw new IllegalStateException("实体" + entityClass.getName() + "主键生成器未初始化");
		}

		if (!generator.isInit(channel, zone)) {
			synchronized (generator) {
				if (!generator.isInit(channel, zone)) {
					long[] limits = IdGenerator.getLimits(channel, zone);
					Long max = querier.unique(entityClass, Long.class, queryNames.get(entityClass), limits[0],
							limits[1]);
					try {
						generator.init(channel, zone, max);
					} catch (IllegalStateException e) {
						logger.error("重复初始化", e);
					}
				}
			}
		}

		return generator;
	}
}
