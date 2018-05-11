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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;

/**
 * 通信会话通信接口
 * 
 * @author kidal
 * 
 */
public class Session implements ConcurrentMap<String, Object> {
	/**
	 * 主键
	 */
	public static final String MAIN_KEY = "org.treediagram.nina";

	/**
	 * 标识
	 */
	private final long id;

	/**
	 * 属性
	 */
	private final ConcurrentMap<String, Object> attributes;

	/**
	 * 创建通信会话通信接口
	 */
	public Session(long id, ConcurrentMap<String, Object> attributes) {
		this.id = id;
		this.attributes = attributes;
	}

	/**
	 * 创建通信会话通信接口748087
	 */
	@SuppressWarnings("unchecked")
	public static Session valueOf(IoSession session) {
		long id = session.getId();
		ConcurrentMap<String, Object> attributes = (ConcurrentMap<String, Object>) session.getAttribute(MAIN_KEY);
		if (attributes == null) {
			attributes = new ConcurrentHashMap<String, Object>();
			Object prevAttributes = session.setAttributeIfAbsent(MAIN_KEY, attributes);
			if (prevAttributes != null) {
				attributes = (ConcurrentMap<String, Object>) prevAttributes;
			}
		}
		return new Session(id, attributes);
	}

	// getters and setters

	public long getId() {
		return id;
	}

	// Object

	@Override
	public boolean equals(Object obj) {
		return attributes.equals(obj);
	}

	@Override
	public int hashCode() {
		return attributes.hashCode();
	}

	// ConcurrentMap<String, Object>

	@Override
	public int size() {
		return attributes.size();
	}

	@Override
	public boolean isEmpty() {
		return attributes.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return attributes.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return attributes.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return attributes.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return attributes.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return attributes.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		attributes.putAll(m);
	}

	@Override
	public void clear() {
		attributes.clear();
	}

	@Override
	public Set<String> keySet() {
		return attributes.keySet();
	}

	@Override
	public Collection<Object> values() {
		return attributes.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return attributes.entrySet();
	}

	@Override
	public Object putIfAbsent(String key, Object value) {
		return attributes.putIfAbsent(key, value);
	}

	@Override
	public boolean remove(Object key, Object value) {
		return attributes.remove(key, value);
	}

	@Override
	public boolean replace(String key, Object oldValue, Object newValue) {
		return attributes.replace(key, oldValue, newValue);
	}

	@Override
	public Object replace(String key, Object value) {
		return attributes.replace(key, value);
	}
}
