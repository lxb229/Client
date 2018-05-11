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

package org.treediagram.nina.network.filter.session;

import org.apache.mina.core.session.IoSession;

/**
 * 会话替换事件
 * 
 * @author kidal
 * 
 */
public class SessionReplacedEvent extends SessionEvent {
	/**
	 * 被替换的会话
	 */
	private final IoSession replaced;

	/**
	 * 创建会话替换事件
	 */
	public SessionReplacedEvent(int cause, Object identity, IoSession session, IoSession replaced) {
		super(SessionEventType.REPLACED, cause, identity, session);
		this.replaced = replaced;
	}

	// getters and setters

	public IoSession getReplaced() {
		return replaced;
	}
}
