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

package org.treediagram.nina.memcache.exception;

import org.treediagram.nina.memcache.IEntity;

/**
 * 实体增强异常
 * 
 * @author kidal
 * 
 */
public class EnhanceException extends MemcacheException {

	private static final long serialVersionUID = 5864958477911383572L;

	/**
	 * 尝试被增强的实体
	 */
	private final IEntity<?> entity;

	public EnhanceException(IEntity<?> entity) {
		super();
		this.entity = entity;
	}

	public EnhanceException(IEntity<?> entity, String message, Throwable cause) {
		super(message, cause);
		this.entity = entity;
	}

	public EnhanceException(IEntity<?> entity, String message) {
		super(message);
		this.entity = entity;
	}

	public EnhanceException(IEntity<?> entity, Throwable cause) {
		super(cause);
		this.entity = entity;
	}

	public IEntity<?> getEntity() {
		return entity;
	}
}
