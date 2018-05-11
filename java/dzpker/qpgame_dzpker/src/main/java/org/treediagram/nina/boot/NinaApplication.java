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

package org.treediagram.nina.boot;

import java.util.Date;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.treediagram.nina.console.Console;
import org.treediagram.nina.core.time.DateUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * 应用程序入口
 * 
 * @author kidal
 *
 */
public final class NinaApplication<T extends NinaApplicationArgs> {
	/**
	 * 启动
	 */
	public static <T extends NinaApplicationArgs> void run(Object source, String... args) throws Exception {
		run(new Object[] { source }, args);
	}

	/**
	 * 启动
	 */
	public static <T extends NinaApplicationArgs> void run(Object[] sources, String[] args) throws Exception {
		run(null, sources, args);
	}

	/**
	 * 启动
	 */
	public static <T extends NinaApplicationArgs> void run(T appArgs, Object source, String... args) throws Exception {
		run(appArgs, new Object[] { source }, args);
	}

	/**
	 * 启动
	 */
	public static <T extends NinaApplicationArgs> void run(T appArgs, Object[] sources, String[] args) throws Exception {
		new NinaApplication<NinaApplicationArgs>(appArgs, sources).run(args);
	}

	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(NinaApplication.class);

	/** 参数 */
	private final T appArgs;
	/** 启动源 */
	@SuppressWarnings("unused")
	private final Object[] sources;

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	private NinaApplication(T args, Object[] sources) {
		if (args == null) {
			this.appArgs = (T) new NinaApplicationArgs();
		} else {
			this.appArgs = args;
		}

		this.sources = sources;
	}

	/**
	 * 获取应用程序参数
	 */
	public T getAppArgs() {
		return appArgs;
	}

	/**
	 * 执行
	 */
	public final void run(String[] args) throws Exception {
		// 解析命令参数
		parseArgs(args);

		// 配置logback
		configureLogback();

		// 启动
		if (appArgs.getMode().equals(NinaApplicationArgs.MODE_CONSOLE)) {
			runConsole();
		} else if (appArgs.getMode().equals(NinaApplicationArgs.MODE_SERVER)) {
			runServer();
		} else {
			System.err.println("unknown app mode: " + appArgs.getMode());
		}
	}

	/**
	 * 解析命令参数
	 */
	private final void parseArgs(String[] args) {
		final CmdLineParser parser = new CmdLineParser(this.appArgs);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.err.println("java " + getClass().getName() + " [options...] arguments...");

			parser.printUsage(System.err);

			System.err.println();
			System.err.println(" Example: java " + getClass().getName() + parser.printExample(OptionHandlerFilter.ALL));

			Runtime.getRuntime().exit(-1);
		}
	}

	/**
	 * 配置logback
	 */
	private final void configureLogback() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(loggerContext);

			loggerContext.reset();

			configurator.doConfigure(appArgs.getLogbackLocation());
		} catch (JoranException e) {
		}

		StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
	}

	/**
	 * 控制台模式启动
	 */
	private void runConsole() {
		ClassPathXmlApplicationContext context = null;

		try {
			// 初始化应用程序
			logger.debug("初始化应用程序...");
			context = new ClassPathXmlApplicationContext(appArgs.getContextLocation());

			// 启动调试控制台
			logger.debug("启动控制台...");
			Console console = new Console(context);
			//console.start();
		} catch (Exception e) {
			logger.error("", e);

			if (context != null && context.isRunning()) {
				context.destroy();
			}
		}
	}

	/**
	 * 服务器模式启动
	 */
	private void runServer() {
		ClassPathXmlApplicationContext context = null;

		try {
			// 初始化应用程序
			logger.debug("初始化应用程序...");
			context = new ClassPathXmlApplicationContext(appArgs.getContextLocation());
		} catch (Exception e) {
			logger.error("", e);
			Runtime.getRuntime().exit(-1);
		}

		// 注册关闭钩子,启动服务器
		context.registerShutdownHook();
		context.start();

		// log
		logger.warn("启动成功 - {}", DateUtils.date2String(new Date(), DateUtils.PATTERN_DATE_TIME));

		// 等待应用程序退出命令
		while (context.isActive()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("主线程非法被打断", e);
				}
			}
		}

		// 等待退出
		while (context.isRunning()) {
			Thread.yield();
		}

		// log
		logger.warn("完毕成功 - {}", DateUtils.date2String(new Date(), DateUtils.PATTERN_DATE_TIME));

		// exit
		Runtime.getRuntime().exit(0);
	}
}
