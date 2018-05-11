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

import java.lang.reflect.Field;
import java.util.Date;

import org.treediagram.nina.core.reflect.ReflectionUtils;
import org.treediagram.nina.core.time.DateUtils;

/**
 * 语言辅助
 * 
 * @author kidal
 *
 */
public abstract class LangUtils {
	/**
	 * 获取对象描述
	 */
	public static String getObjectDescription(Object object) {
		if (object == null) {
			return "null";
		}

		Class<? extends Object> objectClass = object.getClass();
		Field[] fields = objectClass.getDeclaredFields();

		StringBuilder builder = new StringBuilder();

		builder.append("(");

		for (Field field : fields) {
			ReflectionUtils.makeAccessible(field);
			Object value;

			try {
				value = field.get(object);
			} catch (Exception e) {
				value = e.getMessage();
			}

			builder.append(field.getName()).append("=");

			if (value instanceof Date) {
				builder.append(DateUtils.date2String((Date) value, DateUtils.PATTERN_DATE_TIME));
			} else {
				builder.append(value);
			}

			builder.append(", ");
		}

		if (builder.length() == 0) {
			return objectClass.getName() + "@" + object.hashCode();
		}

		String descripion = builder.substring(0, builder.length() - 2);
		return descripion + ")";
	}

	/**
	 * 静态
	 */
	private LangUtils() {
	}
}
