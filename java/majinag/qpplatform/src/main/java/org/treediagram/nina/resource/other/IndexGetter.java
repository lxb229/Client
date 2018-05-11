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

package org.treediagram.nina.resource.other;

import java.util.Comparator;

/**
 * 索引值获取器接口
 * 
 * @author kidal
 */
public interface IndexGetter {
	/**
	 * 获取索引名
	 */
	String getName();

	/**
	 * 是否唯一值索引
	 */
	boolean isUnique();

	/**
	 * 获取索引值
	 * 
	 * @param obj 静态资源实例
	 * @return 索引值
	 */
	Object getValue(Object obj);

	/**
	 * 获取索引排序器
	 */
	@SuppressWarnings("rawtypes")
	Comparator getComparator();

	/**
	 * 检查是否存在索引排序器
	 */
	boolean hasComparator();
}
