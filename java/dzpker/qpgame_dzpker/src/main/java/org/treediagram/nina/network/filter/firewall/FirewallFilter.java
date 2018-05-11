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

package org.treediagram.nina.network.filter.firewall;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treediagram.nina.core.concurrent.DelayedElement;
import org.treediagram.nina.network.model.Attribute;
import org.treediagram.nina.network.util.IpUtils;

/**
 * 防火墙过滤器
 * 
 * @author kidal
 * 
 */
public class FirewallFilter extends IoFilterAdapter implements FirewallManager {
	/**
	 * 分隔符
	 */
	private static final String SPLIT_REGEX = ",";

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(FirewallFilter.class);

	/**
	 * 允许属性
	 */
	private static final Attribute<Boolean> attributeAllow = new Attribute<Boolean>("firewall:allow");

	/**
	 * 记录属性
	 */
	private static final Attribute<FirewallRecord> attributeRecord = new Attribute<FirewallRecord>("firewall:record");

	/**
	 * 白名单 IP 集合
	 */
	private final ConcurrentMap<String, Pattern> allows = new ConcurrentHashMap<String, Pattern>();

	/**
	 * 黑名单 IP 集合
	 */
	private final ConcurrentMap<String, Pattern> blocks = new ConcurrentHashMap<String, Pattern>();

	/**
	 * 黑名单移除队列
	 */
	private final DelayQueue<DelayedElement<String>> blockRemoveQueue = new DelayQueue<DelayedElement<String>>();

	/**
	 * 是否阻塞全部
	 */
	private final AtomicBoolean blockAll = new AtomicBoolean(false);

	/**
	 * 当前连接数量
	 */
	private final AtomicInteger currentConnections = new AtomicInteger(0);

	/**
	 * 默认的阻塞时间（秒）
	 */
	private int defaultBlockSeconds = 10 * 60;

	/**
	 * 最大客户端连接数
	 */
	private int maxClientCount = 5000;

	// public methods

	/**
	 * 设置最大违规次数
	 */
	public void setMaxViolateTimes(int times) {
		FirewallRecord.setMaxViolateTimes(times);
	}

	/**
	 * 设置每秒收到的字节数限制
	 */
	public void setBytesInSecondLimit(int size) {
		FirewallRecord.setBytesInSecondLimit(size);
	}

	/**
	 * 设置每秒收到的数据包次数限制
	 */
	public void setTimesInSecondLimit(int size) {
		FirewallRecord.setTimesInSecondLimit(size);
	}

	/**
	 * 设置是否阻塞全部
	 */
	public void setBlockAllState(boolean state) {
		blockAll.set(state);
	}

	/**
	 * 设置白名单IP集合
	 * 
	 * @param allows 白名单集合
	 */
	public void setAllows(String allows) {
		if (StringUtils.isBlank(allows)) {
			return;
		} else {
			String[] ips = allows.split(SPLIT_REGEX);

			for (String ip : ips) {
				this.allows.put(ip, ipToPattern(ip));
			}
		}
	}

	/**
	 * 设置永久黑名单
	 * 
	 * @param blocks 黑名单集合
	 */
	public void setBlocks(String blocks) {
		if (StringUtils.isBlank(blocks)) {
			return;
		} else {
			String[] ips = blocks.split(SPLIT_REGEX);

			for (String ip : ips) {
				this.blocks.put(ip, ipToPattern(ip));
			}
		}
	}

	/**
	 * 设置永久黑名单
	 */
	public void setBlock(String ip) {
		this.blocks.put(ip, ipToPattern(ip));
	}

	/**
	 * 设置默认阻塞时间秒
	 */
	public void setDefaultBlockSeconds(int defaultBlockSeconds) {
		if (defaultBlockSeconds <= 0) {
			logger.warn("默认阻塞时间不能小于或等于 0 秒，继续使用内核默认时间 {} 秒", this.defaultBlockSeconds);
			return;
		} else {
			this.defaultBlockSeconds = defaultBlockSeconds;
		}
	}

	/**
	 * 设置最大客户端连接数
	 */
	public void setMaxClientCount(int maxClientCount) {
		if (defaultBlockSeconds <= 10) {
			logger.warn("最大客户端连接数不能小于或等于 10，继续使用内核默认最大客户端连接数 {}", this.maxClientCount);
			return;
		} else {
			this.maxClientCount = maxClientCount;
		}
	}

	// private methods

	/**
	 * 将 IP 转换为正则表达式形式
	 */
	private Pattern ipToPattern(String ip) {
		String reg = ip.replace(".", "[.]").replace("*", "[0-9]*");
		Pattern pattern = Pattern.compile(reg);
		return pattern;
	}

	/**
	 * 检查会话是否被允许
	 */
	private boolean isAllow(IoSession session) {
		return attributeAllow.getValue(session, false);
	}

	/**
	 * 检查会话
	 */
	private boolean isViolate(IoSession session, Object message) {
		// 检查类型
		if (!(message instanceof IoBuffer)) {
			if (logger.isDebugEnabled()) {
				logger.debug("接收数据类型 {} 不是 IoBuffer", message.getClass());
			}
			return false;
		}

		// 获取记录
		FirewallRecord firewallRecord = null;

		if (attributeRecord.contains(session)) {
			firewallRecord = attributeRecord.getValue(session);
		} else {
			firewallRecord = new FirewallRecord();
			attributeRecord.setValue(session, firewallRecord);
		}

		// 检查本次访问
		int bytes = ((IoBuffer) message).remaining();

		if (!firewallRecord.check(bytes)) {
			return false;
		}

		// 调试日志
		if (logger.isDebugEnabled()) {
			String ip = IpUtils.getIp(session);
			logger.debug("会话[{}]发生违规，违规状态[总违规次数:{} 长度:{} 次数:{}]", new Object[] { ip, firewallRecord.getViolateTime(),
					firewallRecord.getBytesInSecond(), firewallRecord.getTimesInSecond() });
		}

		// 检查是否超出许可
		if (firewallRecord.isBlock()) {
			return true;
		} else {
			return false;
		}
	}

	// IoHandlerAdapter

	/**
	 * {@link IoFilterAdapter#sessionOpened(NextFilter, IoSession)

	 */
	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
		// 增加当前客户端连接数
		int clientCount = currentConnections.incrementAndGet();

		// 获取 IP 地址
		String ip = IpUtils.getIp(session);

		// 检查是否在白名单
		for (Pattern pattern : allows.values()) {
			if (pattern.matcher(ip).matches()) {
				// 调试日志
				if (logger.isDebugEnabled()) {
					logger.debug("白名单用户 {} 登录服务器", ip);
				}

				// 调用父类方法
				super.sessionOpened(nextFilter, session);
				return;
			}
		}

		// 禁止全部连接
		if (blockAll.get()) {
			// 调试日志
			if (logger.isDebugEnabled()) {
				logger.debug("由于阻止全部连接状态，阻止用户 {} 登录服务器", ip);
			}

			// 断开连接
			session.close(true);
			return;
		}

		// 最大连接数限制
		if (clientCount >= maxClientCount) {
			// 调试日志
			if (logger.isDebugEnabled()) {
				logger.debug("由于达到最大连接数 {}/{}，阻止非白名单用户 {} 登录服务器", new Object[] { clientCount, maxClientCount, ip });
			}

			// 断开连接
			session.close(true);
			return;
		}

		// 过期黑名单清理
		for (;;) {
			DelayedElement<String> delayedElement = blockRemoveQueue.poll();

			if (delayedElement == null) {
				break;
			}

			blockRemoveQueue.remove(delayedElement.getContent());
		}

		// 检查是否在黑名单
		for (Pattern pattern : blocks.values()) {
			if (pattern.matcher(ip).matches()) {
				// 调试日志
				if (logger.isDebugEnabled()) {
					logger.debug("阻止黑名单用户 {} 登录服务器", ip);
				}

				// 断开连接
				session.close(true);
				return;
			}
		}

		// 调用父类方法
		super.sessionOpened(nextFilter, session);
	}

	/**
	 * {@link IoFilterAdapter#sessionClosed(NextFilter, IoSession)}
	 */
	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
		currentConnections.decrementAndGet();
		super.sessionClosed(nextFilter, session);
	}

	/**
	 * {@link IoFilterAdapter#messageReceived(NextFilter, IoSession, Object)}
	 */
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		if (!isAllow(session)) {
			if (isViolate(session, message)) {
				blockByIoSession(session);
				session.close(true);
				return;
			}
		}
		super.messageReceived(nextFilter, session, message);
	}

	// FirewallManager

	/**
	 * {@link FirewallManager#getBlockList()}
	 */
	@Override
	public Collection<String> getBlockList() {
		return Collections.unmodifiableCollection(blocks.keySet());
	}

	/**
	 * {@link FirewallManager#getAllowList()}
	 */
	@Override
	public Collection<String> getAllowList() {
		return Collections.unmodifiableCollection(allows.keySet());
	}

	/**
	 * {@link FirewallManager#block(String, int)}
	 */
	@Override
	public void block(String ip, int seconds) {
		// 不重复阻塞
		if (blocks.containsKey(ip)) {
			return;
		} else {
			// 校正时间
			if (seconds <= 0) {
				seconds = defaultBlockSeconds;
			}

			// 添加到阻塞列表
			blocks.put(ip, ipToPattern(ip));

			// 设置延迟移除
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, seconds);
			DelayedElement<String> delayedElement = DelayedElement.valueOf(ip, calendar.getTime());
			blockRemoveQueue.add(delayedElement);
		}
	}

	/**
	 * {@link FirewallManager#block(String)}
	 */
	@Override
	public void block(String ip) {
		block(ip, 0);
	}

	/**
	 * {@link FirewallManager#blockByIoSession(IoSession)}
	 */
	@Override
	public String blockByIoSession(IoSession session) {
		String ip = IpUtils.getIp(session);
		block(ip);
		return ip;
	}

	/**
	 * {@link FirewallManager#unblock(String)}
	 */
	@Override
	public void unblock(String ip) {
		blocks.remove(ip);
	}

	/**
	 * {@link FirewallManager#isblockAll()}
	 */
	@Override
	public boolean isblockAll() {
		return blockAll.get();
	}

	/**
	 * {@link FirewallManager#blockAll()}
	 */
	@Override
	public void blockAll() {
		blockAll.set(true);
	}

	/**
	 * {@link FirewallManager#unblockAll()}
	 */
	@Override
	public void unblockAll() {
		blockAll.set(false);
	}

	/**
	 * {@link FirewallManager#allow(String)}
	 */
	@Override
	public void allow(String ip) {
		if (allows.containsKey(ip)) {
			return;
		} else {
			allows.put(ip, ipToPattern(ip));
		}
	}

	/**
	 * {@link FirewallManager#disallow(String)}
	 */
	@Override
	public void disallow(String ip) {
		allows.remove(ip);
	}

	/**
	 * {@link FirewallManager#getCurrentConnections()}
	 */
	@Override
	public int getCurrentConnections() {
		return currentConnections.get();
	}
}
