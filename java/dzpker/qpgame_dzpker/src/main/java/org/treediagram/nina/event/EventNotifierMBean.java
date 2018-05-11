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

import java.util.List;

import javax.management.MXBean;

/**
 * 时间通知器JMX接口
 * 
 * @author kidal
 * 
 */
@MXBean
public interface EventNotifierMBean {
	/**
	 * 获取队列长度
	 */
	int getQueueSize();

	/**
	 * 获取事件池长度
	 */
	int getPollQueueSize();

	/**
	 * 获取执行线程数
	 */
	int getPoolActiveCount();

	/**
	 * 获取队列中的事件名
	 */
	List<String> getEvents();
}
