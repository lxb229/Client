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

package org.treediagram.nina.network.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.treediagram.nina.network.model.Request;

/**
 * 提{@link Request}的属性作为参数
 * 
 * <pre>
 * 不能和{@link InBody}与{@link InSession}同时声明在同一个参数上，
 * 如出现同时声明的情况，将以:
 * {@link InBody} > {@link InRequest} > {@link InSession} 的次序生效
 * </pre>
 * 
 * @author kidal
 * 
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface InRequest {
	/**
	 * 值来源类型
	 * 
	 * @author kidal
	 * 
	 */
	public static enum Type {
		/**
		 * 时间戳
		 */
		TIMESTAMP,

		/**
		 * 消息标识
		 */
		ID,
	}

	/**
	 * 值来源
	 */
	Type value();
}
