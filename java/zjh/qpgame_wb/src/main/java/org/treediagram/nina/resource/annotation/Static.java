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

package org.treediagram.nina.resource.annotation;

import java.beans.PropertyEditor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 静态资源注入注释
 * 
 * @author kidal
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Static {
	/**
	 * 标识值
	 */
	String value() default "";

	/**
	 * 唯一标识转换器声明
	 */
	Class<? extends PropertyEditor> converter() default PropertyEditor.class;

	/**
	 * 注入值是否必须
	 */
	boolean required() default true;

	/**
	 * 标识值是否唯一值
	 */
	boolean unique() default false;
}
