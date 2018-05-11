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

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.command.FacadeMethodInvokeContext;
import org.treediagram.nina.network.command.parameter.InBodyParameter;

/**
 * 编码解码器
 * 
 * @author kidal
 * 
 */
public interface Coder {
	/**
	 * 将消息数据解码为对象
	 */
	Object decode(byte[] bytes);

	/**
	 * 将消息对象编码为数据
	 */
	byte[] encode(Object object);

	/**
	 * 获取{@link InBody}注解的参数
	 */
	Object getInBody(Object body, InBodyParameter parameter, FacadeMethodInvokeContext context);
}
