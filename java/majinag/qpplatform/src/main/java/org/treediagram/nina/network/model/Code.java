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

package org.treediagram.nina.network.model;

/**
 * 代号
 * 
 * @author kidal
 * 
 */
public interface Code {
	/**
	 * 成功
	 */
	public static int OK = 0;

	/**
	 * 失败
	 */
	public static int FAILED = -1;

	/**
	 * 未知
	 */
	public static int UNKNOWN_ERROR = -2;

	/**
	 * 参数错误
	 */
	public static int PARAMETER_ERROR = -3;

	/**
	 * 处理错误
	 */
	public static int PROCESSING_ERROR = -4;

	/**
	 * 答复处理错误
	 */
	public static int RESPONSE_PROCESSING_ERROR = -5;

	/**
	 * 正在处理
	 */
	public static int PROCESSING = -6;
	
	/**
	 * 
	 */
}
