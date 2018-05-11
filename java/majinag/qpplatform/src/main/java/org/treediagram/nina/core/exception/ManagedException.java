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

package org.treediagram.nina.core.exception;

/**
 * 受管理异常
 * 
 * @author kidal
 */
public class ManagedException extends RuntimeException {
	private static final long serialVersionUID = 4800557047062263356L;
	private final int code;

	public ManagedException(int code) {
		super();
		this.code = code;
	}

	public ManagedException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ManagedException(int code, String message) {
		super(message);
		this.code = code;
	}

	public ManagedException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
