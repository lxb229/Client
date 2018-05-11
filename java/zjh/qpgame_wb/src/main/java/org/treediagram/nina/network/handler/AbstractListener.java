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

package org.treediagram.nina.network.handler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 侦听器
 * 
 * @author kidal
 * 
 */
public abstract class AbstractListener implements Listener {
	/**
	 * 服务器处理器
	 */
	@Autowired
	private ServerHandler serverHandler;

	/**
	 * 初始化
	 */
	@PostConstruct
	protected void init() {
		serverHandler.addListener(this);
	}
}
