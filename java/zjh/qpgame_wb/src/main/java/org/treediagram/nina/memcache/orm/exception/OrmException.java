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

package org.treediagram.nina.memcache.orm.exception;

/**
 * 存储层异常的抽象父类，用于统一管理存储层异常
 * 
 * @author kidal
 */
public abstract class OrmException extends RuntimeException {

	private static final long serialVersionUID = -5480573723994246089L;

	public OrmException() {
		super();
	}

	public OrmException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrmException(String message) {
		super(message);
	}

	public OrmException(Throwable cause) {
		super(cause);
	}

}
