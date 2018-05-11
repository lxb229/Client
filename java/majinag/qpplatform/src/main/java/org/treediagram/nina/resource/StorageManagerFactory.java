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

package org.treediagram.nina.resource;

import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.treediagram.nina.resource.other.ResourceDefinition;

/**
 * 资源管理器工厂
 * 
 * @author kidal
 */
public class StorageManagerFactory implements FactoryBean<StorageManager>, ApplicationContextAware {
	/**
	 * 资源定义列表
	 */
	private List<ResourceDefinition> definitions;

	public void setDefinitions(List<ResourceDefinition> definitions) {
		this.definitions = definitions;
	}

	private StorageManager storageManager;

     public StorageManagerFactory()
     {
    	 
	 }
     
	@PostConstruct
	protected void initialize()
	{
		storageManager = this.applicationContext.getAutowireCapableBeanFactory().createBean(StorageManager.class);
		for (ResourceDefinition definition : definitions) {
			storageManager.initialize(definition);
		}
		for (ResourceDefinition definition : definitions) {
			storageManager.validate(definition);
		}
	}

	@Override
	public StorageManager getObject() throws Exception {
		return storageManager;
	}

	// 实现接口的方法

	@Override
	public Class<StorageManager> getObjectType() {
		return StorageManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
