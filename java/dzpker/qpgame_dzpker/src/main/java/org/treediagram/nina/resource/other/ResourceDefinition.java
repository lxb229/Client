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

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import org.treediagram.nina.core.reflect.ReflectionUtils;
import org.treediagram.nina.resource.Validate;
import org.treediagram.nina.resource.annotation.InjectBean;
import org.treediagram.nina.resource.annotation.ResourceType;

/**
 * 资源定义信息对象
 * 
 * @author kidal
 */
public class ResourceDefinition {

	public final static String FILE_SPLIT = ".";
	public final static String FILE_PATH = File.separator;

	/**
	 * 注入属性域过滤器
	 */
	private final static FieldFilter INJECT_FILTER = new FieldFilter() {
		@Override
		public boolean matches(Field field) {
			if (field.isAnnotationPresent(InjectBean.class)) {
				return true;
			}
			return false;
		}
	};

	/**
	 * 资源类
	 */
	private final Class<?> clz;

	/**
	 * 资源路径
	 */
	private final String location;

	/**
	 * 开始标记
	 */
	private final String startMark;

	/**
	 * 资源格式
	 */
	private final String format;

	/**
	 * 资源的注入信息
	 */
	private final Set<InjectDefinition> injects = new HashSet<InjectDefinition>();

	/**
	 * 构造方法
	 */
	public ResourceDefinition(Class<?> clz, FormatDefinition format) {
		ResourceType anno = clz.getAnnotation(ResourceType.class);

		this.clz = clz;
		this.format = format.getType();
		this.startMark = anno.startMark();

		StringBuilder builder = new StringBuilder();
		builder.append(format.getLocation()).append(FILE_PATH);
		if (StringUtils.isNotBlank(anno.value())) {
			String dir = anno.value();
			int start = 0;
			int end = dir.length();
			if (StringUtils.startsWith(dir, FILE_PATH)) {
				start++;
			}
			if (StringUtils.endsWith(dir, FILE_PATH)) {
				end--;
			}
			builder.append(dir.substring(start, end)).append(FILE_PATH);
		}
		if (StringUtils.isNotBlank(anno.name())) {
			builder.append(anno.name()).append(FILE_SPLIT).append(format.getSuffix());
		} else {
			builder.append(clz.getSimpleName()).append(FILE_SPLIT).append(format.getSuffix());
		}
		this.location = builder.toString();

		ReflectionUtils.doWithDeclaredFields(clz, new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				InjectDefinition definition = new InjectDefinition(field);
				injects.add(definition);
			}
		}, INJECT_FILTER);
	}

	/**
	 * 获取静态属性注入定义
	 */
	public Set<InjectDefinition> getStaticInjects() {
		HashSet<InjectDefinition> result = new HashSet<InjectDefinition>();
		for (InjectDefinition definition : this.injects) {
			Field field = definition.getField();
			if (Modifier.isStatic(field.getModifiers())) {
				result.add(definition);
			}
		}
		return result;
	}

	/**
	 * 获取非静态属性注入定义
	 */
	public Set<InjectDefinition> getInjects() {
		HashSet<InjectDefinition> result = new HashSet<InjectDefinition>();
		for (InjectDefinition definition : this.injects) {
			Field field = definition.getField();
			if (!Modifier.isStatic(field.getModifiers())) {
				result.add(definition);
			}
		}
		return result;
	}

	/**
	 * 资源是否需要校验
	 */
	public boolean isNeedValidate() {
		if (Validate.class.isAssignableFrom(clz)) {
			return true;
		}
		return false;
	}

	// Getter and Setter ...

	public Class<?> getClz() {
		return clz;
	}

	public String getLocation() {
		return location;
	}

	public String getStartMark() {
		return startMark;
	}

	public String getFormat() {
		return format;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
