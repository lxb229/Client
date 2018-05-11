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
 * 事件通知器
 * 
 * @author kidal
 * 
 */
public interface EventNotifier {
	/**
	 * 抛出事件
	 */
	void post(EventContext<?> eventContext);

	/**
	 * 同步抛出事件
	 */
	void postSync(EventContext<?> eventContext);

	/**
	 * 抛出事件
	 */
	<T> void post(T event);

	/**
	 * 同步抛出事件
	 */
	<T> void postSync(T event);

	/**
	 * 侦听事件
	 */
	void register(String name, EventListener<?> listener);

	/**
	 * 停止侦听事件
	 */
	void unregister(String name, EventListener<?> listener);

	/**
	 * 侦听事件
	 */
	void register(Class<?> name, EventListener<?> listener);

	/**
	 * 停止侦听事件
	 */
	void unregister(Class<?> name, EventListener<?> listener);
}
