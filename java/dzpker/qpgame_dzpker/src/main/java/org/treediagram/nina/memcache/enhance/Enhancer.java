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

package org.treediagram.nina.memcache.enhance;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.exception.EnhanceException;

/**
 * 实体增强器
 * 
 * @author kidal
 * 
 */
@SuppressWarnings("rawtypes")
public interface Enhancer {
	/**
	 * 将实体的实例转换为增强实例
	 * 
	 * @param <T> 实体类型
	 * @param entity 实体实例
	 * @return 增强实例
	 * @exception EnhanceException 实体增强异常
	 */
	public <T extends IEntity> T transform(T entity) throws EnhanceException;
}
