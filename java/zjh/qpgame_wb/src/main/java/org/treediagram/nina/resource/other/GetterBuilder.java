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

package org.treediagram.nina.resource.other;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.treediagram.nina.core.reflect.ReflectionUtils;
import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceIndex;

/**
 * 唯一标示获取实例创建器
 * 
 * @author kidal
 */
public class GetterBuilder {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(GetterBuilder.class);

	/**
	 * 属性识别器
	 */
	private static class FieldGetter implements Getter {

		private final Field field;

		public FieldGetter(Field field) {
			ReflectionUtils.makeAccessible(field);
			this.field = field;
		}

		@Override
		public Object getValue(Object object) {
			Object value = null;
			try {
				value = field.get(object);
			} catch (Exception e) {
				FormattingTuple message = MessageFormatter.format("标识符属性访问异常", e);
				logger.error(message.getMessage());
				throw new RuntimeException(message.getMessage());
			}
			return value;
		}
	}

	/**
	 * 方法识别器
	 */
	private static class MethodGetter implements Getter {

		private final Method method;

		public MethodGetter(Method method) {
			ReflectionUtils.makeAccessible(method);
			this.method = method;
		}

		@Override
		public Object getValue(Object object) {
			Object value = null;
			try {
				value = method.invoke(object);
			} catch (Exception e) {
				FormattingTuple message = MessageFormatter.format("标识方法访问异常", e);
				logger.error(message.getMessage());
				throw new RuntimeException(message.getMessage());
			}
			return value;
		}
	}

	/**
	 * 识别信息
	 * 
	 * @author kidal
	 */
	private static class IdentityInfo {

		public final Field field;
		public final Method method;

		public IdentityInfo(Class<?> clz) {
			Field[] fields = ReflectionUtils.getDeclaredFieldsWith(clz, ResourceId.class);
			if (fields.length > 1) {
				FormattingTuple message = MessageFormatter.format("类[{}]的属性唯一标识声明重复", clz);
				logger.error(message.getMessage());
				throw new RuntimeException(message.getMessage());
			}
			if (fields.length == 1) {
				this.field = fields[0];
				this.method = null;
				return;
			}
			Method[] methods = ReflectionUtils.getDeclaredGetMethodsWith(clz, ResourceId.class);
			if (methods.length > 1) {
				FormattingTuple message = MessageFormatter.format("类[{}]的方法唯一标识声明重复", clz);
				logger.error(message.getMessage());
				throw new RuntimeException(message.getMessage());
			}
			if (methods.length == 1) {
				this.method = methods[0];
				this.field = null;
				return;
			}
			FormattingTuple message = MessageFormatter.format("类[{}]缺少唯一标识声明", clz);
			logger.error(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}

		public boolean isField() {
			if (field != null) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 创建指定资源类的唯一标示获取实例
	 * 
	 * @param clz 资源类
	 */
	public static Getter createIdGetter(Class<?> clz) {
		IdentityInfo info = new IdentityInfo(clz);
		Getter identifier = null;
		if (info.isField()) {
			identifier = new FieldGetter(info.field);
		} else {
			identifier = new MethodGetter(info.method);
		}
		return identifier;
	}

	/**
	 * 属性域索引值获取器
	 * 
	 * @author kidal
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static class FieldIndexGetter extends FieldGetter implements IndexGetter {

		private final String name;
		private final boolean unique;
		private final Comparator comparator;

		public FieldIndexGetter(Field field) {
			super(field);
			ResourceIndex index = field.getAnnotation(ResourceIndex.class);
			this.name = index.name();
			this.unique = index.unique();

			Class<Comparator> clz = (Class<Comparator>) index.comparatorClz();
			if (!clz.equals(Comparator.class)) {
				try {
					this.comparator = clz.newInstance();
				} catch (Exception e) {
					throw new IllegalArgumentException("索引比较器[" + clz.getName() + "]无法被实例化");
				}
			} else {
				comparator = null;
			}
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isUnique() {
			return unique;
		}

		@Override
		public Comparator getComparator() {
			return comparator;
		}

		@Override
		public boolean hasComparator() {
			if (comparator != null) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 方法值索引值获取器
	 * 
	 * @author kidal
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static class MethodIndexGetter extends MethodGetter implements IndexGetter {

		private final String name;
		private final boolean unique;
		private final Comparator comparator;

		public MethodIndexGetter(Method method) {
			super(method);
			ResourceIndex index = method.getAnnotation(ResourceIndex.class);
			this.name = index.name();
			this.unique = index.unique();

			Class<Comparator> clz = (Class<Comparator>) index.comparatorClz();
			if (!clz.equals(Comparator.class)) {
				try {
					this.comparator = clz.newInstance();
				} catch (Exception e) {
					throw new IllegalArgumentException("索引比较器[" + clz.getName() + "]无法被实例化");
				}
			} else {
				comparator = null;
			}
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isUnique() {
			return unique;
		}

		@Override
		public Comparator getComparator() {
			return comparator;
		}

		@Override
		public boolean hasComparator() {
			if (comparator != null) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 创建资源索引
	 */
	public static Map<String, IndexGetter> createIndexGetters(Class<?> clz) {
		Field[] fields = ReflectionUtils.getDeclaredFieldsWith(clz, ResourceIndex.class);
		Method[] methods = ReflectionUtils.getDeclaredGetMethodsWith(clz, ResourceIndex.class);

		List<IndexGetter> getters = new ArrayList<IndexGetter>(fields.length + methods.length);
		for (Field field : fields) {
			IndexGetter getter = new FieldIndexGetter(field);
			getters.add(getter);
		}
		for (Method method : methods) {
			IndexGetter getter = new MethodIndexGetter(method);
			getters.add(getter);
		}

		Map<String, IndexGetter> result = new HashMap<String, IndexGetter>(getters.size());
		for (IndexGetter getter : getters) {
			String name = getter.getName();
			if (result.put(name, getter) != null) 
			{
				FormattingTuple message = MessageFormatter.format("资源类[{}]的索引名[{}]重复", clz, name);
				logger.error(message.getMessage());
				throw new RuntimeException(message.getMessage());
			}
		}
		return result;
	}
}
