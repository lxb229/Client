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

import org.treediagram.nina.memcache.IEntity;

/**
 * 实体创建器
 * 
 * @author kidalsama
 * 
 */
public interface EntityBuilder<PK extends Comparable<PK>, T extends IEntity<PK>> {
	/**
	 * 创建实体
	 */
	public T createInstance(PK pk);
}
