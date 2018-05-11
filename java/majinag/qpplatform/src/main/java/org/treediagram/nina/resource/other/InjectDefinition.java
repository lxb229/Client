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

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.treediagram.nina.resource.annotation.InjectBean;

/**
 * 注入信息定义
 * 
 * @author kidal
 */
public class InjectDefinition {
	/**
	 * 被注入的属性
	 */
	private final Field field;

	/**
	 * 注入配置
	 */
	private final InjectBean inject;

	/**
	 * 注入类型
	 */
	private final InjectType type;

	public InjectDefinition(Field field) {
		if (field == null) {
			throw new IllegalArgumentException("被注入属性域不能为null");
		}
		if (!field.isAnnotationPresent(InjectBean.class)) {
			throw new IllegalArgumentException("被注入属性域" + field.getName() + "的注释配置缺失");
		}
		field.setAccessible(true);

		this.field = field;
		this.inject = field.getAnnotation(InjectBean.class);
		if (StringUtils.isEmpty(this.inject.value())) {
			this.type = InjectType.CLASS;
		} else {
			this.type = InjectType.NAME;
		}
	}

	/**
	 * 获取注入值
	 */
	public Object getValue(ApplicationContext applicationContext) {
		if (InjectType.NAME.equals(type)) {
			return applicationContext.getBean(inject.value());
		} else {
			return applicationContext.getBean(field.getType());
		}
	}

	// Getter and Setter ...

	public InjectType getType() {
		return type;
	}

	public Field getField() {
		return field;
	}

	public InjectBean getInject() {
		return inject;
	}
}
