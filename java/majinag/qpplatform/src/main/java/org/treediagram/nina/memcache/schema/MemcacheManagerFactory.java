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

package org.treediagram.nina.memcache.schema;

import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.MemcacheManager;
import org.treediagram.nina.memcache.orm.Accessor;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.writeback.WriterConfig;

/**
 * 内存缓存服务管理器工厂
 * 
 * @author kidalsama
 * 
 */
public class MemcacheManagerFactory implements FactoryBean<MemcacheManager> {
	/**
	 * 实体类名
	 */
	public static final String ENTITY_CLASSES_NAME = "entityClasses";

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(MemcacheManagerFactory.class);

	/**
	 * 访问器
	 */
	private Accessor accessor;

	/**
	 * 查询器
	 */
	private Querier querier;

	/**
	 * 实体类
	 */
	private Set<Class<IEntity<?>>> entityClasses;

	/**
	 * 回写器配置
	 */
	private Map<String, WriterConfig> writerConfigs;

	/**
	 * 常量
	 */
	private Map<String, Integer> constants;

	/**
	 * 内存缓存服务管理器
	 */
	private MemcacheManager memcacheServiceManager;
	
	public MemcacheManagerFactory ()
	{
		
		//System.out.println("ppppppppppppppppppppppppppppppppppppppppppppppppppp");
	}

	@PreDestroy
	public void destory() {
		if (memcacheServiceManager != null) {
			// 日志
			logger.warn("开始回写实体缓存数据...");

			// 回写
			memcacheServiceManager.destory();

			// 等待
			Integer shutdownDelay = constants.get("__shutdown_delay__");
			if (shutdownDelay == null)
			{
				shutdownDelay = 5000;
			}
			try {
				Thread.sleep(shutdownDelay);
			} catch (InterruptedException e) {

			}

			// 日志
			logger.warn("回写实体缓存数据完成!");
		}
	}

	/**
	 * {@link FactoryBean#getObject()}
	 */
	@Override
	public MemcacheManager getObject() throws Exception {
		memcacheServiceManager = new MemcacheManager(accessor, querier, entityClasses, writerConfigs, constants);
		return memcacheServiceManager;
	}

	/**
	 * {@link FactoryBean#getObjectType()}
	 */
	@Override
	public Class<?> getObjectType() {
		return MemcacheManager.class;
	}

	/**
	 * {@link FactoryBean#isSingleton()}
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	// setters

	public void setAccessor(Accessor accessor) {
		this.accessor = accessor;
	}

	public void setQuerier(Querier querier) {
		this.querier = querier;
	}

	public void setEntityClasses(Set<Class<IEntity<?>>> entityClasses) {
		this.entityClasses = entityClasses;
	}

	public void setWriterConfigs(Map<String, WriterConfig> writerConfigs) {
		this.writerConfigs = writerConfigs;
	}

	public void setConstants(Map<String, Integer> constants) {
		this.constants = constants;
	}
}
