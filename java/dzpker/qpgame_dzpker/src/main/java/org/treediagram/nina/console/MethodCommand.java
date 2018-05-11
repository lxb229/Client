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

import org.springframework.core.convert.ConversionService;
import org.treediagram.nina.console.exception.ArgumentException;
import org.treediagram.nina.console.exception.CommandException;
import org.treediagram.nina.console.exception.ExecuteException;

/**
 * 控制台命令方法
 * 
 * @author kidal
 * 
 */
public class MethodCommand implements Command {

	private final String name;
	private final String description;
	private final Object target;
	private final Method method;
	private final Class<?>[] types;
	private final ConversionService conversionService;

	public MethodCommand(Object target, Method method, ConversionService conversionService) {
		this.target = target;
		this.conversionService = conversionService;

		method.setAccessible(true);
		this.method = method;

		ConsoleCommand annotation = method.getAnnotation(ConsoleCommand.class);
		this.name = annotation.name().trim();
		this.description = annotation.description().trim();
		this.types = method.getParameterTypes();
	}

	@Override
	public boolean execute(String[] arguments) throws CommandException {
		if (arguments.length != types.length) {
			throw new ArgumentException("指令参数长度不正确");
		}

		Object[] args = new Object[arguments.length];
		try {
			for (int i = 0; i < arguments.length; i++) {
				args[i] = conversionService.convert(arguments[i], types[i]);
			}
		} catch (Exception e) {
			throw new ArgumentException("参数转换时出现异常", e);
		}

		try {
			Object result = method.invoke(target, args);
			if (result instanceof Boolean) {
				return ((Boolean) result).booleanValue();
			} else {
				return true;
			}
		} catch (Exception e) {
			throw new ExecuteException("方法执行时出现异常", e);
		}
	}
	
	@Override
	public Object execute2(String[] arguments) throws CommandException {
		if (arguments.length != types.length) {
			throw new ArgumentException("指令参数长度不正确");
		}

		Object[] args = new Object[arguments.length];
		try {
			for (int i = 0; i < arguments.length; i++) {
				args[i] = conversionService.convert(arguments[i], types[i]);
			}
		} catch (Exception e) {
			throw new ArgumentException("参数转换时出现异常", e);
		}

		try {
			Object result = method.invoke(target, args);
			return result;
		} catch (Exception e) {
			throw new ExecuteException("方法执行时出现异常", e);
		}
	}	

	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}

	
}
