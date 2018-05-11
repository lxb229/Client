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

package org.treediagram.nina.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.treediagram.nina.console.exception.ArgumentException;
import org.treediagram.nina.console.exception.CommandException;
import org.treediagram.nina.console.exception.ExecuteException;

/**
 * 控制台线程的处理类
 * 
 * @author kidal
 */
public class ConsoleRunner implements Runnable {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(ConsoleRunner.class);

	/**
	 * 控制台
	 */
	private Console console;

	public ConsoleRunner(Console console) {
		this.console = console;
	}

	/**
	 * 拆壳
	 */
	private Throwable unwrapThrowable(Throwable e) {
		if (e == null) {
			return null;
		} else if (e instanceof java.lang.reflect.InvocationTargetException) {
			return e.getCause();
		} else {
			return e;
		}
	}

	@Override
	public void run() {
		ConsoleCommandBuffer commandReader = new ConsoleCommandBuffer(console);
		try {
			while (!console.isStop()) {
				// 读取一行
				String line = commandReader.readLine();
				if(line == null || line.isEmpty()){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				// 获取命令名
				String[] names = CommandHelper.getNames(line);
				String nameDesc = (names.length == 1 ? names[0] : names[0] + " " + names[1]);
				int namelen = names.length;

				// 获取命令
				Command command = null;
				if (names != null) {
					if (names.length > 1) {
						command = console.getCommand(names[0], names[1]);
					}
					if (command == null) {
						namelen = 1;
						command = console.getCommand(names[0]);
					}
				}

				// 检查是否找到命令
				if (command == null) {
					FormattingTuple message = MessageFormatter.format("指令[{}]不存在", nameDesc);
					if (logger.isDebugEnabled()) {
						logger.debug(message.getMessage());
					}
					System.err.println(message.getMessage());
					continue;
				}
				String[] arguments = CommandHelper.getArguments(line, namelen);
				try {
					boolean success = command.execute(arguments);
					if (success) {
						System.out.println("[" + nameDesc + " OK]");
					} else {
						System.err.println("[" + nameDesc + " FAILED]");
					}
				} catch (ArgumentException e) {
					FormattingTuple message = MessageFormatter.format("指令[{}]参数[{}]异常", nameDesc, arguments);
					if (logger.isDebugEnabled()) {
						logger.debug(message.getMessage(), e);
					}
					System.err.println(message.getMessage());
				} catch (ExecuteException e) {
					FormattingTuple message = MessageFormatter.format("指令[{} - {}]执行异常", new Object[] { nameDesc,
							arguments }, unwrapThrowable(e.getCause()));
					if (logger.isDebugEnabled()) {
						logger.debug(message.getMessage(), e);
					}
					System.err.println(message.getMessage());
				} catch (CommandException e) {
					FormattingTuple message = MessageFormatter.format("指令[{} - {}]未知异常", new Object[] { nameDesc,
							arguments }, unwrapThrowable(e.getCause()));
					if (logger.isDebugEnabled()) {
						logger.debug(message.getMessage(), e);
					}
					System.err.println(message.getMessage());
				}
			}

			// 修复在 JDK 6 环境下，出现 JDWP exit error AGENT_ERROR_NO_JNI_ENV(183) 的问题
			System.exit(0);
		} catch (Exception e) {
			logger.error("获取命令行输入时出现异常", e);
		}
	}
}
