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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.treediagram.nina.console.exception.ArgumentException;
import org.treediagram.nina.console.exception.CommandException;

import com.palmjoys.yf1b.act.framework.common.manager.ApplicationCloseManager;

import timer.TimerCenter;

/**
 * 控制台对象
 * 
 * @author kidal
 * 
 */
public class Console {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(Console.class);

	/**
	 * 控制台停止状态
	 */
	private boolean stop;

	/**
	 * 已经被分类的控制台指令集合
	 */
	private Map<String, Map<String, Command>> groupedCommands = new HashMap<String, Map<String, Command>>();

	/**
	 * 控制台指令集合
	 */
	private Map<String, Command> commands = new HashMap<String, Command>();

	/**
	 * 控制台管理的当前的应用上下文
	 */
	private final AbstractApplicationContext applicationContext;

	/**
	 * 当前应用上下文内的类型转换服务类
	 */
	private final ConversionService conversionService;

	/**
	 * 控制台运行方式,1=前台运行,0=后台运行
	 * */
	private int _runMode; 
	
	public static Console _instance = null;
	
	
	public static Console Instance(){
		return _instance;
	}
	/**
	 * 控制台构造方法
	 */
	public Console(AbstractApplicationContext applicationContext) {
		// 注册 JVM ShutdownHook
		applicationContext.registerShutdownHook();
		this.applicationContext = applicationContext;
		this.conversionService = applicationContext.getBean(ConversionService.class);
		registerCommand();
		commands.put(cmdStop.name(), cmdStop);
		commands.put(cmdList.name(), cmdList);
		if (logger.isDebugEnabled()) {
			logger.debug("控制台初始化完成");
		}
		if(_instance == null)
			_instance = this;
	}

	/**
	 * 停止控制台的方法
	 */
	public void stop() {
		TimerCenter.getInstance().shutDown();
		ApplicationCloseManager.Instance().closeApplication();
		applicationContext.close();
		stop = true;
		if (logger.isInfoEnabled()) {
			logger.info("控制台关闭");
		}
	}
	
	/**
	 * 控制台运行方式
	 * */
	public int getRunMode(){
		return _runMode;
	}

	/**
	 * 检查控制台是否已经停止
	 */
	public boolean isStop() {
		return stop;
	}

	/**
	 * 获取指令名对应的命令对象实例
	 */
	public Command getCommand(String name, String subname) {
		Map<String, Command> group = groupedCommands.get(name);
		return (group != null ? group.get(subname) : null);
	}

	/**
	 * 获取指令名对应的命令对象实例
	 */
	public Command getCommand(String name) {
		return commands.get(name);
	}

	/**
	 * 启动控制台
	 */
	public void start(String runMode) {
		this._runMode = 0;
		if(runMode.equalsIgnoreCase("open")){
			this._runMode = 1;
		}
		
		ConsoleRunner runner = new ConsoleRunner(this);
		Thread thread = new Thread(runner, "控制台输入线程");
		thread.start();
		if (logger.isInfoEnabled()) {
			logger.info("控制台启动");
		}
	}

	/**
	 * 命令方法过滤器
	 */
	private MethodFilter findCommand = new MethodFilter() {
		@Override
		public boolean matches(Method method) {
			if (method.getAnnotation(ConsoleCommand.class) != null) {
				return true;
			}
			return false;
		}
	};

	/**
	 * 注册控制台命令
	 */
	private void registerCommand() {
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ConsoleBean.class);

		for (Entry<String, Object> entry : beans.entrySet()) {
			final Object bean = entry.getValue();
			final Class<? extends Object> beanClass = bean.getClass();
			
			final ConsoleBean consoleBean = beanClass.getAnnotation(ConsoleBean.class);
			final String groupName = consoleBean.value();

			ReflectionUtils.doWithMethods(bean.getClass(), new MethodCallback() {
				@Override
				public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
					// 创建命令
					Command command = new MethodCommand(bean, method, conversionService);

					// 分类或者散装
					if (StringUtils.isNotBlank(groupName)) {
						// 获取组
						Map<String, Command> group = groupedCommands.get(groupName);
						if (group == null) {
							group = new HashMap<String, Command>();
							groupedCommands.put(groupName, group);
						}

						// 添加到组
						if (group.containsKey(command.name())) {
							throw new IllegalStateException("命令[" + groupName + "][" + command.name() + "]重复");
						}
						group.put(command.name(), command);
					} else {
						// 检查与分类组名的冲突
						if (groupedCommands.containsKey(command.name())) {
							throw new IllegalStateException("命令[" + command.name() + "]与分类组名冲突");
						}
						// 添加到散装
						if (commands.containsKey(command.name())) {
							throw new IllegalStateException("命令[" + command.name() + "]重复");
						}
						commands.put(command.name(), command);
					}
				}
			}, findCommand);
		}
	}

	/**
	 * 停止控制台的指令
	 */
	private Command cmdStop = new Command() {
		@Override
		public String name() {
			return "stop";
		}

		@Override
		public boolean execute(String[] arguments) throws CommandException {
			stop();
			return true;
		}

		@Override
		public String description() {
			return "停止控制台";
		}

		@Override
		public Object execute2(String[] arguments) throws CommandException {
			return execute(arguments);
		}
	};

	/**
	 * 列出全部控制台指令的指令
	 */
	private Command cmdList = new Command() {
		class Pair {
			private String name;
			private String description;

			public Pair(String name, String description) {
				this.name = name;
				this.description = description;
			}
		}

		@Override
		public String name() {
			return "list";
		}

		@Override
		public boolean execute(String[] arguments) throws CommandException {
			// 命令最大长度
			int longestLength = 0;

			// 收集命令和他的描述
			List<Pair> pairs = new ArrayList<Pair>();

			for (Command command : commands.values()) {
				if (command.name().length() > longestLength) {
					longestLength = command.name().length();
				}
				pairs.add(new Pair(command.name(), command.description()));
			}

			for (Entry<String, Map<String, Command>> e : groupedCommands.entrySet()) {
				for (Command command : e.getValue().values()) {
					String name = e.getKey() + " " + command.name();
					int nameLength = name.length();
					if (nameLength > longestLength) {
						longestLength = nameLength;
					}
					pairs.add(new Pair(name, command.description()));
				}
			}

			// 排序
			Collections.sort(pairs, new Comparator<Pair>() {
				@Override
				public int compare(Pair o1, Pair o2) {
					return o1.name.compareTo(o2.name);
				}
			});

			// 打印
			for (Pair pair : pairs) {
				String line = String.format("%-" + longestLength + "s %s", pair.name, pair.description);
				System.out.println(line);
			}

			// 完成
			return true;
		}

		@Override
		public String description() {
			return "列出全部控制台指令";
		}

		@Override
		public Object execute2(String[] arguments) throws CommandException {
			return execute(arguments);
		}
	};
	
	public Object executeCommand(String line)throws CommandException{
		// 获取命令名
		String[] names = CommandHelper.getNames(line);
		int namelen = names.length;

		// 获取命令
		Command command = null;
		if (names != null) {
			if (names.length > 1) {
				command = this.getCommand(names[0], names[1]);
			}
			if (command == null) {
				namelen = 1;
				command = this.getCommand(names[0]);
			}
		}
		if(command == null){
			throw new ArgumentException("未找到指定的命令");
		}
		String[] arguments = CommandHelper.getArguments(line, namelen);
		
		return command.execute2(arguments);
	}
	
}
