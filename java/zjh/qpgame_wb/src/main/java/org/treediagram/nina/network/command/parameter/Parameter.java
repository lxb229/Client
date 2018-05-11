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

package org.treediagram.nina.network.command.parameter;

import org.apache.mina.core.session.IoSession;
import org.treediagram.nina.network.command.FacadeMethodInvokeContext;
import org.treediagram.nina.network.model.Request;

/**
 * 命令参数
 * 
 * @author kidal
 * 
 */
public interface Parameter {
	/**
	 * 获取命令参数类型
	 */
	ParameterType getType();

	/**
	 * 获取参数类型
	 */
	Class<?> getParameterClass();

	/**
	 * 参数是否支持转换
	 */
	boolean isSupportConvert();

	/**
	 * 获取参数值
	 */
	Object getValue(Request<?> request, IoSession session, FacadeMethodInvokeContext context);
}
