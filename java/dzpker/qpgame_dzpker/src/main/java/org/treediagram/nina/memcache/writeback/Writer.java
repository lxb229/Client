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

import java.util.Map;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.orm.Accessor;

/**
 * 数据写入器
 * 
 * @author kidalsama
 * 
 */
@SuppressWarnings("rawtypes")
public interface Writer {
	/**
	 * 初始化
	 * 
	 * @param name 写入器名
	 * @param accessor 访问器
	 * @param configuration 配置
	 */
	public void initialize(String name, Accessor accessor, String configuration);

	/**
	 * 将元素放入队列
	 * 
	 * @param element 元素
	 */
	public void put(Element element);

	/**
	 * 添加事件侦听器
	 * 
	 * @param entityClass 实体类型
	 * @param listener 事件侦听器
	 */
	public void addEventListener(Class<? extends IEntity> entityClass, EventListener listener);

	/**
	 * 获取事件侦听器
	 * 
	 * @param entityClass 实体类型
	 * @return 事件侦听器
	 */
	public EventListener getEventListener(Class<? extends IEntity> entityClass);

	/**
	 * 停止并立即会写
	 */
	public void stopAndWriteback();

	/**
	 * 当前状态信息
	 */
	public Map<String, String> getInformation();
}
