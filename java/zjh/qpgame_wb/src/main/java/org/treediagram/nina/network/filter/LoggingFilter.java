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

package org.treediagram.nina.network.filter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 会话日志过滤器
 * 
 * @author kidal
 * 
 */
public class LoggingFilter extends IoFilterAdapter {
	private final String name;
	private final Logger logger;

	public LoggingFilter() {
		this(LoggingFilter.class.getName());
	}

	public LoggingFilter(Class<?> clazz) {
		this(clazz.getName());
	}

	public LoggingFilter(String name) {
		if (name == null) {
			this.name = LoggingFilter.class.getName();
		} else {
			this.name = name;
		}
		logger = LoggerFactory.getLogger(this.name);
	}

	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
		//logger.info("OPENED {}", session.getRemoteAddress());
		super.sessionOpened(nextFilter, session);
	}

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
		//logger.info("CLOSED {}", session.getRemoteAddress());
		super.sessionClosed(nextFilter, session);
	}

	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
		//int idletime = session.getConfig().getIdleTime(status) * session.getIdleCount(status);
		//logger.info("IDLE {} seconds, {}", idletime, session.getRemoteAddress());
		super.sessionIdle(nextFilter, session, status);
	}

	@Override
	public void exceptionCaught(NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(nextFilter, session, cause);
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		super.messageReceived(nextFilter, session, message);
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		super.messageSent(nextFilter, session, writeRequest);
	}
}
