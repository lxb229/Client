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

package org.treediagram.nina.network.command;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * 命令集合
 * 
 * @author kidal
 * 
 */
public class Commands {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(Commands.class);

	/**
	 * 命令调用器
	 */
	private ConcurrentMap<Integer, FacadeMethod> facadeMethods = new ConcurrentHashMap<Integer, FacadeMethod>();

	/**
	 * 创建命令集合
	 */
	public Commands() {

	}

	/**
	 * 注册统计项目
	 */
	public void registerStat() {
		for (FacadeMethod facadeMethod : facadeMethods.values()) {
			facadeMethod.registerStat();
		}
	}

	/**
	 * 注册命令
	 */
	public void register(FacadeMethod invoker) {
		// 注册
		FacadeMethod prevFacadeMethod = facadeMethods.putIfAbsent(invoker.getId(), invoker);
		if (prevFacadeMethod != null) {
			FormattingTuple message = MessageFormatter.format("指令 {} 重复注册 {}", invoker.getId(), prevFacadeMethod);
			logger.error(message.getMessage());
			throw new IllegalStateException(message.getMessage());
		}

		// 注册统计
		invoker.registerStat();

		if (logger.isDebugEnabled()) {
			logger.debug("完成指令注册:{}", invoker);
		}
	}

	/**
	 * 获取方法调用器
	 */
	public FacadeMethod getFacadeMethod(int id) {
		return facadeMethods.get(id);
	}
}
