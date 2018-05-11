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

package org.treediagram.nina.resource.reader;

import java.io.InputStream;
import java.util.Iterator;

/**
 * 资源读取接口
 * 
 * @author kidal
 */
public interface ResourceReader {
	/**
	 * 配置方法
	 * 
	 * @param config 配置信息
	 */
	void config(String config);

	/**
	 * 获取处理资源的格式名
	 */
	String getFormat();

	/**
	 * 从输入流读取资源实例
	 * 
	 * @param <E>
	 * @param input 输入流
	 * @param clz 资源实例类型
	 * @param startMark 起始标识
	 */
	<E> Iterator<E> read(InputStream input, Class<E> clz, String startMark);
}
