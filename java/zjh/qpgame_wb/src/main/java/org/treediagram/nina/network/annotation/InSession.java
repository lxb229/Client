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

import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;

/**
 * 提取{@link IoSession}的内容作为参数
 * 
 * <pre>
 * 不能和{@link InBody}与{@link InRequest}同时声明在同一个参数上，
 * 如出现同时声明的情况，将以:
 * {@link InBody} > {@link InRequest} > {@link InSession} 的次序生效
 * </pre>
 * 
 * @author kidal
 * 
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface InSession {
	/**
	 * 值来源，{@link AttributeKey}的字符串表示格式
	 * 
	 * <pre>
	 * 类名@键
	 * </pre>
	 */
	String value() default "";

	/**
	 * 是否必须
	 */
	boolean required() default true;

	/**
	 * 默认值
	 */
	String defaultValue() default "";
}
