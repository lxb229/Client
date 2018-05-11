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

package org.treediagram.nina.core.lang;

import java.sql.Date;

/**
 * 数字工具类
 * 
 * @author kidal
 * 
 */
public class NumberUtils {
	/**
	 * 创建
	 */
	@SuppressWarnings("unchecked")
	public static <T> T valueOf(Class<T> resultType, Number value) {
		if (resultType == null) {
			String msg = value.getClass().getSimpleName() + " -> NULL";
			throw new NullPointerException(msg);
		}
		if (resultType == Date.class) {
			return (T) new Date(value.longValue());
		} else if (resultType == int.class || resultType == Integer.class) {
			return (T) Integer.valueOf(value.intValue());
		} else if (resultType == double.class || resultType == Double.class) {
			return (T) value;
		} else if (resultType == boolean.class || resultType == Boolean.class) {
			return (T) Boolean.valueOf(value.intValue() == 0);
		} else if (resultType == byte.class || resultType == Byte.class) {
			return (T) Byte.valueOf(value.byteValue());
		} else if (resultType == long.class || resultType == Long.class) {
			return (T) Long.valueOf(value.longValue());
		} else if (resultType == short.class || resultType == Short.class) {
			return (T) Short.valueOf(value.shortValue());
		} else if (resultType == float.class || resultType == Float.class) {
			return (T) Float.valueOf(value.floatValue());
		} else if (resultType == Number.class) {
			return (T) value;
		} else {
			String msg = value.getClass().getSimpleName() + " -> " + resultType.getSimpleName();
			throw new IllegalArgumentException(new ClassCastException(msg));
		}
	}
}
