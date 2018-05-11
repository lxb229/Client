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

package org.treediagram.nina.network.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treediagram.nina.network.util.IpUtils;

/**
 * 消息解码器
 * 
 * @author kidal
 * 
 */
public class MessageDecoder extends CumulativeProtocolDecoder {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

	/**
	 * 会话上下文属性键
	 */
	private static final AttributeKey CONTEXT = new AttributeKey(MessageDecoder.class, "context");

	/**
	 * 数据包解码状态
	 * 
	 * @author kidal
	 * 
	 */
	enum DecodeState {
		/**
		 * 解码数据头
		 */
		HEADER,

		/**
		 * 解码数据体
		 */
		BODY,
	}

	/**
	 * 数据包解码上下文
	 * 
	 * @author kidal
	 * 
	 */
	public static class DecodeContext {
		/**
		 * 数据长度
		 */
		private int length = 0;

		/**
		 * 数据包解码状态
		 */
		private DecodeState decodeState = DecodeState.HEADER;

		/**
		 * 重置上下文状态
		 */
		public void reset() {
			this.length = 0;
			this.decodeState = DecodeState.HEADER;
		}

		// getters and setters

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public DecodeState getDecodeState() {
			return decodeState;
		}

		public void setDecodeState(DecodeState decodeState) {
			this.decodeState = decodeState;
		}
	}

	/**
	 * 解码
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		// 获取数据包解码上下文
		DecodeContext decodeContext = getDecodeContext(session);

		// 开始解码
		int length;
		while (in.hasRemaining()) {
			switch (decodeContext.getDecodeState()) {
			case HEADER:
				// 必须接受到消息头的长度才能开始解码
				if (in.remaining() < Message.HEADER_LENGTH) {
					return false;
				}

				// 标记
				in.mark();

				// 设置数据包解码上下文
				length = Message.getFullLength(in);
				decodeContext.setDecodeState(DecodeState.BODY);
				decodeContext.setLength(length);

			case BODY:
				// 获取足够的数据
				length = decodeContext.getLength();
				if (in.remaining() < length) {
					return false;
				}

				try {
					// 读取数据
					byte[] data = new byte[length];
					in.get(data);

					// 构造消息
					Message message = new Message(data);

					// 校验消息头
					int headerChecksum = message.checkHeader();
					if (headerChecksum != message.getHeaderCheck()) {
						throw new Exception("消息头校验码错误[client=" + message.getHeaderCheck() + ",server=" + headerChecksum
								+ "]");
					}

					// 校验消息体
					int bodyChecksum = message.checkBody();
					if (bodyChecksum != message.getBodyCheck()) {
						throw new Exception("消息体校验码错误[client=" + message.getBodyCheck() + ",server=" + bodyChecksum
								+ "]");
					}

					// 写入消息
					out.write(message);
				} catch (Exception e) {
					String ip = IpUtils.getIp(session);
					session.close(true);
					logger.error("断开连接[{}](解码错误)", ip, e);
				} finally {
					decodeContext.reset();
				}
				break;

			default:
				logger.error("无法处理的数据包解码状态[{}]", decodeContext.getDecodeState());
				break;
			}
		}

		// 解码失败
		return false;
	}

	/**
	 * 获取数据包解码上下文
	 */
	private DecodeContext getDecodeContext(IoSession session) {
		DecodeContext decodeContext = (DecodeContext) session.getAttribute(CONTEXT);
		if (decodeContext == null) {
			decodeContext = new DecodeContext();
			session.setAttribute(CONTEXT, decodeContext);
		}
		return decodeContext;
	}
}
