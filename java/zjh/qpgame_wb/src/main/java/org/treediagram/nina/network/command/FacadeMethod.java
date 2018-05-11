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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.NotFoundException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.treediagram.nina.core.exception.ManagedException;
import org.treediagram.nina.network.codec.CoderManager;
import org.treediagram.nina.network.exception.ProcessingException;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.stat.StatFactory;
import org.treediagram.nina.stat.model.NanoTimeStat;
import org.treediagram.nina.stat.model.Stat;

/**
 * 前端方法
 * 
 * @author kidal
 * 
 */
public class FacadeMethod {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(FacadeMethod.class);

	/**
	 * 计时统计项目
	 */
	private NanoTimeStat timeStat;

	/**
	 * 错误统计项目
	 */
	private Stat errorStat;

	/**
	 * 命令编号
	 */
	private final int id;

	/**
	 * 实例对象
	 */
	private final Object target;

	/**
	 * 命令方法定义
	 */
	private final MethodDefinition definition;

	/**
	 * 创建前端方法
	 */
	public FacadeMethod(int id, Object target, Method method, CoderManager coders) throws NotFoundException {
		this.id = id;
		this.target = target;
		this.definition = new MethodDefinition(method, coders);
	}

	/**
	 * 调用命令方法
	 */
	public Object invoke(Request<?> request, IoSession session, FacadeMethodInvokeContext context) throws Throwable {
		// 获取方法
		Method method = definition.getMethod();

		// 开启计时
		timeStat.begin();

		// 执行
		try {
			Object[] args = definition.buildParameters(method, request, session, context);
			return method.invoke(target, args);
		} catch (Exception e) {
			// 统计错误
			errorStat.increment();
			e.printStackTrace();

			// 处理异常
			if (e instanceof ProcessingException) 
			{
				throw (ProcessingException) e;
			} else if (e instanceof ManagedException) 
			{
				throw (ManagedException) e;
			} else if (e instanceof InvocationTargetException) 
			{
				Throwable exception = ((InvocationTargetException) e).getTargetException();
				if (exception instanceof ManagedException) 
				{
					throw exception;
				}
			}

			// 意料外的异常
			FormattingTuple message = MessageFormatter.format("对象 {} 的方法 {} 调用异常", target.getClass().getName(),
					method.getName());
			logger.error(message.getMessage());
			throw new ProcessingException(message.getMessage(), e);
		} finally {
			timeStat.end();
		}
	}

	/**
	 * 注册统计项目
	 */
	public void registerStat() {
		// 计时统计项目
		String timeStatGroup = String.format("time-%s", target.getClass().getSimpleName());
		String timeStatName = String.format("%s.%d", definition.getMethod().getName(), id);
		timeStat = StatFactory.getStat(NanoTimeStat.class, timeStatGroup, timeStatName);

		// 错误统计项目
		String errorStatGroup = String.format("error-%s", target.getClass().getSimpleName());
		String errorStatName = String.format("%s.%d", definition.getMethod().getName(), id);
		errorStat = StatFactory.getStat(Stat.class, errorStatGroup, errorStatName);
	}

	// Object

	@Override
	public String toString() {
		return target.getClass().getName() + "." + definition.getMethod().getName();
	}

	// getters and setters

	public int getId() {
		return id;
	}

	public Object getTarget() {
		return target;
	}

	public MethodDefinition getDefinition() {
		return definition;
	}
}
