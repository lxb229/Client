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
 * {@link IoSession}的参数
 * 
 * @author kidal
 * 
 */
public class IoSessionParameter implements Parameter {
	/**
	 * 共享实例
	 */
	public static final IoSessionParameter INSTANCE = new IoSessionParameter();

	/**
	 * {@link Parameter#getType()}
	 */
	@Override
	public ParameterType getType() {
		return ParameterType.IO_SESSION;
	}

	/**
	 * {@link Parameter#getParameterClass()}
	 */
	@Override
	public Class<?> getParameterClass() {
		return null;
	}

	/**
	 * {@link Parameter#isSupportConvert()}
	 */
	@Override
	public boolean isSupportConvert() {
		return false;
	}

	/**
	 * {@link Parameter#getValue(Request, IoSession, FacadeMethodInvokeContext)}
	 */
	@Override
	public Object getValue(Request<?> request, IoSession session, FacadeMethodInvokeContext context) {
		return session;
	}
}
