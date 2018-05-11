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

import org.springframework.core.convert.ConversionService;
import org.treediagram.nina.network.command.parameter.ParameterPostProcessor;

/**
 * 前端方法调用上下文
 * 
 * @author kidal
 * 
 */
public class FacadeMethodInvokeContext {
	/**
	 * 转换服务
	 */
	private ConversionService conversionService;

	/**
	 * 参数后期处理
	 */
	private ParameterPostProcessor parameterPostProcessor;

	/**
	 * 创建
	 */
	public FacadeMethodInvokeContext(ConversionService conversionService, ParameterPostProcessor parameterPostProcessor) {
		super();
		this.conversionService = conversionService;
		this.parameterPostProcessor = parameterPostProcessor;
	}

	// getters

	public ConversionService getConversionService() {
		return conversionService;
	}

	public ParameterPostProcessor getParameterPostProcessor() {
		return parameterPostProcessor;
	}
}
