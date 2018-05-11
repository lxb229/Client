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

import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.treediagram.nina.network.filter.session.SessionManager;

/**
 * 清理空闲会话过滤器
 * 
 * @author kidal
 * 
 */
public class CleanIdleSessionFilter extends IoFilterAdapter {
	/**
	 * 日志
	 */
	private final static Logger logger = LoggerFactory.getLogger(CleanIdleSessionFilter.class);

	@Autowired
	private SessionManager sessionManager;
	
	/**
	 * 空闲时间（秒）
	 */
	private int idleSeconds = 30;

	/**
	 * 设置空闲时间
	 */
	public void setIdleSeconds(int idleSeconds) {
		if (idleSeconds < 30) {
			logger.warn("会话空闲时间小于 30 秒，使用内核默认值 {} 秒", idleSeconds);
		} else {
			this.idleSeconds = idleSeconds;
		}
	}

	/**
	 * {@link IoFilterAdapter#sessionOpened(NextFilter, IoSession)}
	 */
	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
		// 获取会话配置
		IoSessionConfig config = session.getConfig();

		// 设置空闲时间
		if (config.getBothIdleTime() != idleSeconds) {
			config.setIdleTime(IdleStatus.WRITER_IDLE, idleSeconds);

			// 调试日志
			/*if (logger.isDebugEnabled()) {
				logger.debug("设置连接 {} 空闲时间 {}", session.getRemoteAddress(), idleSeconds);
			}*/
		}

		// 回到父类方法
		super.sessionOpened(nextFilter, session);
	}

	/**
	 * {@link IoFilterAdapter#sessionIdle(NextFilter, IoSession, IdleStatus)}
	 */
	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
		// 调试日志
		/*if (logger.isDebugEnabled()) {
			logger.debug("关闭空闲连接 {}...", session.getRemoteAddress());
		}*/
		Object playerObj = sessionManager.getOnlineIdentity(session);
		if(playerObj != null){
			Long playerId = (Long)playerObj;
			ConcurrentMap<Long, Long> theMap = sessionManager.getIdleClosedSessions();
			theMap.put(playerId, playerId);
		}
		
		// 关闭空闲连接
		session.close(true);
	}
}
