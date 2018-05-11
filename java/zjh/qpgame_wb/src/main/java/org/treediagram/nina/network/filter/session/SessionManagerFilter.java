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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;

import javax.annotation.PostConstruct;
import javax.swing.text.AbstractDocument.Content;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.treediagram.nina.core.concurrent.DelayedElement;
import org.treediagram.nina.network.handler.ServerHandler;
import org.treediagram.nina.network.model.Attribute;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Session;

/**
 * 会话管理器过滤器
 * 
 * @author kidal
 * 
 */
public class SessionManagerFilter extends IoFilterAdapter implements SessionManager, ApplicationContextAware
{
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(SessionManagerFilter.class);

	/**
	 * 验证身份会话属性
	 */
	private static Attribute<Object> identityAttribute = new Attribute<Object>("identity");

	/**
	 * 处理完成会话属性
	 */
	private static Attribute<Boolean> proceedAttribute = new Attribute<Boolean>("proceed");

	/**
	 * 踢出会话属性
	 */
	private static Attribute<Boolean> kickedAttribute = new Attribute<Boolean>("kicked");

	/**
	 * 原因会话属性
	 */
	private static Attribute<Integer> causeAttribute = new Attribute<Integer>("cause");

	/**
	 * 无视事件会话属性
	 */
	private static Attribute<Boolean> ignoreEventAttribute = new Attribute<Boolean>("ignore_event");
	
	/**
	 * 应用程序上下文
	 */
	private ApplicationContext applicationContext;

	/**
	 * 验证身份会话
	 */
	private ConcurrentMap<Object, IoSession> identities = new ConcurrentHashMap<Object, IoSession>();
    
	 
	/**
	 * 匿名会话
	 */
	private ConcurrentMap<Long, IoSession> anonymous = new ConcurrentHashMap<Long, IoSession>();

	/**
	 * 关闭会话
	 */
	private ConcurrentMap<Object, IoSession> closeds = new ConcurrentHashMap<Object, IoSession>();

	/**
	 * 延迟删除队列
	 */
	private DelayQueue<DelayedElement<Object>> removeQueue;

	/**
	 * 延迟删除时间
	 */
	private int delayTimes;

	/**
	 * 通信控制器
	 */
	private ServerHandler handler;

	/**
	 * 事件监听器
	 */
	private Map<SessionEventType, SessionListener> listeners = new HashMap<SessionEventType, SessionListener>();

	/**
	 * 因空闲事件关闭的队列
	 * */
	private ConcurrentMap<Long, Long> idleClosedSessions = new ConcurrentHashMap<Long, Long>();
	
	/**
	 * 延迟删除运行器
	 */
	private class DelayRemoveRunnable implements Runnable {
		@Override
		public void run() {
			for (;;) {
				try {
					DelayedElement<Object> e = removeQueue.take();
					Object identity = e.getContent();
					IoSession closed = closeds.remove(identity);
					if (closed != null) {
						Integer cause = causeAttribute.getValue(closed, SessionEventCause.NORMAL);
						fireClosedEvent(identity, cause, closed);
					}
				} catch (InterruptedException e) {
					logger.error("会话延迟移除被打断", e);
				}
			}
		}
	}

	/**
	 * 初始化
	 */
	@PostConstruct
	protected void initialize() {
		// 通信控制器
		handler = applicationContext.getBean(ServerHandler.class);
		Assert.notNull(handler, "通信控制器不能为null");
		
		// 延迟移除
		if (hasDelayTimes()) {
			removeQueue = new DelayQueue<DelayedElement<Object>>();

			Thread thread = new Thread(new DelayRemoveRunnable(), "会话延迟移除处理");
			thread.setDaemon(true);
			thread.start();
		}
	}

	/**
	 * 检查是否有延迟时间
	 */
	private boolean hasDelayTimes() {
		return delayTimes > 0;
	}

	/**
	 * 会话替换事件
	 */
	private void fireReplacedEvent(Object identity, IoSession current, IoSession replaced) {
		SessionListener listener = listeners.get(SessionEventType.REPLACED);
		if (listener == null) {
			return;
		}
		if (!ignoreEventAttribute.getValue(current, false)) {
			listener.onEvent(new SessionReplacedEvent(SessionEventCause.ENFORCE_LOGOUT, identity, current, replaced));
		}
	}

	/**
	 * 发出完成身份验证事件
	 */
	private void fireIdentifiedEvent(Object identity, IoSession session) {
		SessionListener listener = listeners.get(SessionEventType.IDENTIFIED);
		if (listener == null) {
			return;
		}
		if (!ignoreEventAttribute.getValue(session, false)) {
			listener.onEvent(new SessionEvent(SessionEventType.IDENTIFIED, SessionEventCause.NORMAL, identity, session));
		}
	}

	/**
	 * 发出关闭事件
	 */
	private void fireClosedEvent(Object identity, int cause, IoSession session) {
		SessionListener listener = listeners.get(SessionEventType.CLOSED);
		if (listener == null) {
			return;
		}
		if (!ignoreEventAttribute.getValue(session, false)) {
			listener.onEvent(new SessionEvent(SessionEventType.CLOSED, cause, identity, session));
		}
	}

	/**
	 * 添加会话
	 */
	private void add(IoSession session) {
		anonymous.put(session.getId(), session);
	}

	/**
	 * 移除会话
	 */
	private void remove(IoSession session) {
		anonymous.remove(session.getId());

		Object identity = getIdentity(session);
		if (identity != null) {
			identities.remove(identity);
		}
	}

	/**
	 * 获取身份
	 */
	private Object getIdentity(IoSession session) {
		return identityAttribute.getValue(session);
	}		
	
	/**
	 * 当会话开启时
	 */
	private void onSessionOpened(IoSession session) {
		add(session);
	}

	/**
	 * 当会话关闭时
	 */
	private void onSessionClosed(IoSession session) {
		remove(session);

		Object identity = getIdentity(session);
		if (identity == null) {
			return;
		}
		
		if (hasDelayTimes()) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, delayTimes);

			DelayedElement<Object> e = DelayedElement.valueOf(identity, calendar.getTime());
			removeQueue.put(e);
			closeds.put(identity, session);
		} else {
			Integer cause = causeAttribute.getValue(session, SessionEventCause.NORMAL);
			fireClosedEvent(identity, cause, session);
		}
	}

	/**
	 * 当验证用户时
	 */
	private void onIdentified(IoSession session) {
		Object identity = getIdentity(session);
		if (identity == null) {
			return;
		}

		anonymous.remove(session.getId());
		identities.put(identity, session);

		if (hasDelayTimes()) {
			IoSession prev = closeds.remove(identity);
			if (prev != null && prev != session) {
				fireReplacedEvent(identity, session, prev);
				return;
			}
		}

		fireIdentifiedEvent(identity, session);
	}

	// IoFilterAdapter

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		if (identityAttribute.contains(session) && !proceedAttribute.getValue(session, false)) {
			if (logger.isDebugEnabled()) {
				logger.debug("*** 发现未处理的用户身份标识 {} ***", identityAttribute.getValue(session));
			}
			onIdentified(session);
			proceedAttribute.setValue(session, true);
		}
		super.messageSent(nextFilter, session, writeRequest);
	}

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
		onSessionClosed(session);

		if (logger.isDebugEnabled()) {
			/*if (identityAttribute.contains(session)) {
				logger.debug("*** 用户 {} 会话 {} 关闭移除 ***", identityAttribute.getValue(session), session.getId());
			} else {
				logger.debug("*** 会话 {} 关闭移除 ***", session.getId());
			}*/
		}
		super.sessionClosed(nextFilter, session);
	}

	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
		onSessionOpened(session);

		/*if (logger.isDebugEnabled()) {
			logger.debug("会话 {} 创建添加", session.getId());
		}*/
		super.sessionOpened(nextFilter, session);
	}

	@Override
	public void send(Request<?> request, Object... ids) {
		// 检查参数
		if (ids == null || ids.length == 0) {
			return;
		}

		// 获取会话
		List<IoSession> sessions = new ArrayList<IoSession>(ids.length);
		for (Object id : ids) {
			IoSession session = identities.get(id);
			if (session != null) {
				sessions.add(session);
			}
		}

		// 检查会话长度
		if (sessions.isEmpty()) {
			return;
		}

		// 发送
		send(request, sessions.toArray(new IoSession[0]));
	}

	@Override
	public void send(Request<?> request, IoSession... sessions) {
		if (sessions != null) {
			handler.send(request, sessions);
		}
	}	

	@Override
	public void sendAll(Request<?> request) {
		sendAllIdentified(request);
		sendAllAnonymous(request);
	}

	@Override
	public void sendAllIdentified(Request<?> request) {
		IoSession[] sessions = identities.values().toArray(new IoSession[0]);
		send(request, sessions);
	}

	@Override
	public void sendAllAnonymous(Request<?> request) {
		IoSession[] sessions = anonymous.values().toArray(new IoSession[0]);
		send(request, sessions);
	}

	@Override
	public boolean isOnline(Object... ids) {
		for (Object id : ids) {
			if (!identities.containsKey(id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Collection<?> kick(int cause, Object... ids) {
		Set<Object> kicks = new HashSet<Object>();
		for (Object id : ids) {
			// 获取会话
			IoSession session = identities.get(id);
			if (session == null) {
				continue;
			}

			// 设置属性
			kickedAttribute.setValue(session, true);
			causeAttribute.setValue(session, cause);

			if (cause < 0) {
				ignoreEventAttribute.setValue(session, true);
			}

			// 先移除会话
			remove(session);

			// 关闭会话
			session.close(false);

			if (cause >= 0) {
				fireClosedEvent(id, cause, session);
			}

			// 添加到集合
			kicks.add(id);
		}
		return kicks;
	}

	@Override
	public Collection<?> kickAll(int cause) {
		Object[] ids = identities.keySet().toArray();
		return kick(cause, ids);
	}

	@Override
	public Collection<?> getOnlineIdentities() {
		return new HashSet<Object>(identities.keySet());
	}

	@Override
	public Object getOnlineIdentity(IoSession session) {
		Object identity = identityAttribute.getValue(session);
		if (identity != null) {
			if (!identities.containsKey(identity)) {
				identity = null;
			}
		}
		return identity;
	}

	@Override
	public IoSession getSession(Object id) {
		IoSession session = identities.get(id);
		if (session == null) {
			session = closeds.get(id);
		}
		return session;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void replace(IoSession src, IoSession dest) {
		// 检查参数
		if (src == null || dest == null) {
			throw new IllegalArgumentException("替换源和替换目标不能为null");
		}

		// 获取属性
		ConcurrentMap<String, Content> attributes = (ConcurrentHashMap<String, Content>) src
				.getAttribute(Session.MAIN_KEY);
		if (attributes == null) {
			return;
		}

		// 拷贝属性
		attributes = new ConcurrentHashMap<String, Content>(attributes);
		attributes.remove(kickedAttribute.getKey());
		attributes.remove(causeAttribute.getKey());
		dest.setAttribute(Session.MAIN_KEY, attributes);

		// 身份认证
		Object id = identityAttribute.getValue(dest);
		if (logger.isInfoEnabled()) {
			logger.info("*** SESSION {} 替代 SESSION {}，绑定用户身份 {} ***", new Object[] { dest.getId(), src.getId(), id });
		}
		onIdentified(dest);
		proceedAttribute.setValue(dest, true);

		// 清空源属性
		src.removeAttribute(Session.MAIN_KEY);
	}

	@Override
	public void bind(IoSession session, Object obj ) {		
		if(obj == null)
			return;
		Long playerId = (Long)obj;
		/*if (logger.isInfoEnabled()){
			logger.info("*** SESSION {} 绑定用户身份 {} ***", session.getId(), playerId);
		}*/
		idleClosedSessions.remove(playerId);
		
		identityAttribute.setValue(session, playerId);
		onIdentified(session);
		proceedAttribute.setValue(session, true);
	}

	@Override
	public void unBind(IoSession session, Object obj) {
		if(obj == null)
			return;
		/*Long playerId = (Long)obj;
		if (logger.isInfoEnabled()){
			logger.info("*** SESSION {} 解除绑定用户身份 {} ***", session.getId(), playerId);
		}*/		
		this.remove(session);
		session.removeAttribute(Session.MAIN_KEY);
	}

	@Override
	public int count(boolean includeAnonymous) {
		if (includeAnonymous) {
			return identities.size() + anonymous.size();
		} else {
			return identities.size();
		}
	}

	@Override
	public void addListener(SessionListener listener) {
		synchronized (this) {
			SessionEventType type = listener.getType();
			if (listeners.containsKey(type)) {
				throw new IllegalStateException("事件类型 " + type + " 对应的监听器已经存在");
			}
			listeners.put(listener.getType(), listener);
		}
	}

	// ApplicationContextAware

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	// IoC
	public void setDelayTimes(int delayTimes) {
		this.delayTimes = delayTimes;
	}

	@Override
	public ConcurrentMap<Long, Long> getIdleClosedSessions() {
		return idleClosedSessions;
	}
}
