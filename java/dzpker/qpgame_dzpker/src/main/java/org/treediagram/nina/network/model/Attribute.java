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

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

/**
 * 属性类，用于统一与简化对{@link IoSession}的属性操作<br/>
 * 所有属性都将放在以{@link Session#MAIN_KEY}作为KEY，对应的{@link ConcurrentHashMap}中
 * 
 * @author kidal
 */
@SuppressWarnings("unchecked")
public class Attribute<T> implements Serializable {
	/**
	 * 序列化标识
	 */
	private static final long serialVersionUID = -1292330951278268083L;

	/**
	 * 获取会话中的全部属性
	 */
	public static ConcurrentHashMap<String, Object> getAttributes(IoSession session) {
		ConcurrentHashMap<String, Object> attributes = (ConcurrentHashMap<String, Object>) session
				.getAttribute(Session.MAIN_KEY);
		if (attributes != null) {
			return attributes;
		} else {
			attributes = new ConcurrentHashMap<String, Object>();
			ConcurrentHashMap<String, Object> prev = (ConcurrentHashMap<String, Object>) session.setAttributeIfAbsent(
					Session.MAIN_KEY, attributes);
			return prev == null ? attributes : prev;
		}
	}

	/**
	 * 获取属性值
	 */
	public static <T> T getValue(IoSession session, String key) {
		ConcurrentHashMap<String, Object> attributes = getAttributes(session);
		return (T) attributes.get(key);
	}

	/**
	 * 获取属性值，不存在则以默认值返回
	 */
	public static <T> T getValue(IoSession session, String key, T defaultValue) {
		if (contains(session, key)) {
			return getValue(session, key);
		} else {
			return defaultValue;
		}
	}

	/**
	 * 设置属性值
	 */
	public static <T> T setValue(IoSession session, String key, T value) {
		ConcurrentHashMap<String, Object> attributes = getAttributes(session);
		return (T) attributes.put(key, value);
	}

	/**
	 * 检查是否存在对应的属性值
	 */
	public static boolean contains(IoSession session, String key) {
		ConcurrentHashMap<String, Object> attributes = getAttributes(session);
		return attributes.containsKey(key);
	}

	/**
	 * 属性键
	 */
	private final String key;

	/**
	 * 构建属性对象
	 * 
	 * @param key 属性键
	 */
	public Attribute(String key) {
		this.key = key;
	}

	/**
	 * 获取属性值
	 */
	public T getValue(IoSession session) {
		return getValue(session, key);
	}

	/**
	 * 获取属性值，不存在则以默认值返回
	 */
	public T getValue(IoSession session, T defaultValue) {
		return getValue(session, key, defaultValue);
	}

	/**
	 * 设置属性值
	 */
	public T setValue(IoSession session, T value) {
		return setValue(session, key, value);
	}

	/**
	 * 检查是否存在对应的属性值
	 */
	public boolean contains(IoSession session) {
		return contains(session, key);
	}

	/**
	 * 属性键
	 */
	public String getKey() {
		return key;
	}

	/**
	 * {@link Object#toString()}
	 */
	@Override
	public String toString() {
		return key;
	}
}
