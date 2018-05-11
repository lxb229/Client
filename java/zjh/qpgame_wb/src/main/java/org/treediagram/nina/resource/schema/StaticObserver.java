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

package org.treediagram.nina.resource.schema;

import java.lang.reflect.Field;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

/**
 * 静态资源变更观察者
 * 
 * @author kidal
 */
@SuppressWarnings("rawtypes")
public class StaticObserver implements Observer {
	/**
	 * 日志
	 */
	private final static Logger logger = LoggerFactory.getLogger(StaticObserver.class);

	/**
	 * 接收更新通知
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (!(o instanceof Storage)) {
			if (logger.isDebugEnabled()) {
				FormattingTuple message = MessageFormatter.format("被观察对象[{}]不是指定类型", o.getClass());
				logger.debug(message.getMessage());
			}
			return;
		}

		inject((Storage) o);
	}

	/**
	 * 注入资源实例
	 */
	private void inject(Storage o) {
		@SuppressWarnings("unchecked")
		Object value = o.get(key, false);
		if (anno.required() && value == null) {
			FormattingTuple message = MessageFormatter.format("被注入属性[{}]不存在[key:{}]", field, key);
			logger.error(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}

		try {
			field.set(target, value);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("无法设置被注入属性[{}]", field);
			logger.error(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}
	}

	/**
	 * 注入目标
	 */
	private final Object target;

	/**
	 * 注入属性
	 */
	private final Field field;

	/**
	 * 注入属性
	 */
	private final Static anno;

	/**
	 * 资源键值
	 */
	private final Object key;

	public StaticObserver(Object target, Field field, Static anno, Object key) {
		this.target = target;
		this.field = field;
		this.anno = anno;
		this.key = key;
	}

	// Getter and Setter ...

	public Object getTarget() {
		return target;
	}

	public Field getField() {
		return field;
	}

	public Static getAnno() {
		return anno;
	}

	public Object getKey() {
		return key;
	}
}
