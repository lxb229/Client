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

/**
 * 内存缓存配置异常
 * 
 * @author kidalsama
 * 
 */
public class MemcacheConfigurationException extends MemcacheException {

	private static final long serialVersionUID = 1128704065672664881L;

	public MemcacheConfigurationException() {
		super();
	}

	public MemcacheConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public MemcacheConfigurationException(String message) {
		super(message);
	}

	public MemcacheConfigurationException(Throwable cause) {
		super(cause);
	}

}
