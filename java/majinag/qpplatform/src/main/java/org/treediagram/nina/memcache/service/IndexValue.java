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

package org.treediagram.nina.memcache.service;

/**
 * 索引值对象
 * 
 * @author kidal
 * 
 */
public class IndexValue {
	/**
	 * 索引名
	 */
	private final String name;

	/**
	 * 索引值
	 */
	private final Object value;

	/**
	 * 构造方法
	 */
	private IndexValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * 构造方法
	 */
	public static IndexValue valueOf(String name, Object value) {
		if (name == null) {
			throw new IllegalArgumentException("索引名不能为null");
		}
		return new IndexValue(name, value);
	}

	/**
	 * 获取索引名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取索引值
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 获取索引值
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(Class<T> clz) {
		return (T) value;
	}

	/**
	 * {@link Object#hashCode()}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * {@link Object#equals(Object)}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		IndexValue other = (IndexValue) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}
}
