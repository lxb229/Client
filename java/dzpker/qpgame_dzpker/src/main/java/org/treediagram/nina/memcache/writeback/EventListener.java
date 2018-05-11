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

package org.treediagram.nina.memcache.writeback;

import org.treediagram.nina.memcache.IEntity;

/**
 * 事件侦听器
 * 
 * @author kidalsama
 * 
 */
public interface EventListener {
	/**
	 * 通知
	 * 
	 * @param type 通知类型
	 * @param isSuccess 是否成功
	 * @param id 主键
	 * @param entity 实体
	 * @param exception 发生的异常
	 */
	public void onNotify(EventType type, boolean isSuccess, Object id, IEntity<?> entity, RuntimeException exception);
}
