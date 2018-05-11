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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.treediagram.nina.network.exception.CoderNotFoundException;

/**
 * 编码解码器管理器
 * 
 * @author kidal
 * 
 */
public class CoderManager {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(CoderManager.class);

	/**
	 * 编码解码器
	 */
	private final Map<Integer, Coder> coders;

	/**
	 * 创建编码解码器管理器
	 */
	public CoderManager(Map<Integer, Coder> coders) {
		this.coders = coders;
	}

	/**
	 * 将消息数据解码为对象
	 */
	public Object decode(int id, byte[] bytes) {
		Coder coder = getCoder(id);
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return coder.decode(bytes);
	}

	/**
	 * 将消息对象编码为数据
	 */
	public byte[] encode(int id, Object object) {
		Coder coder = getCoder(id);
		if (object == null) {
			return new byte[0];
		}
		return coder.encode(object);
	}

	/**
	 * 获取编码解码器
	 */
	public Coder getCoder(int id) {
		Coder coder = coders.get(id);
		if (coder == null) {
			FormattingTuple message = MessageFormatter.format("格式[{}]对应的编码器不存在", id);
			logger.error(message.getMessage());
			throw new CoderNotFoundException(message.getMessage());
		}
		return coder;
	}
}
