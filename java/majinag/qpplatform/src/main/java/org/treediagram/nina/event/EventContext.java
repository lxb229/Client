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

package org.treediagram.nina.event;

/**
 * 事件上下文
 * 
 * @author kidal
 * 
 */
public class EventContext<T> {
	/**
	 * 事件名
	 */
	private final String name;

	/**
	 * 事件
	 */
	private final T event;

	/**
	 * 创建事件
	 */
	public EventContext(T event) {
		this.name = event.getClass().getName();
		this.event = event;
	}

	/**
	 * 创建事件
	 */
	public EventContext(String name, T event) {
		this.name = name;
		this.event = event;
	}

	/**
	 * 事件名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 事件
	 */
	public T getEvent() {
		return event;
	}
}
