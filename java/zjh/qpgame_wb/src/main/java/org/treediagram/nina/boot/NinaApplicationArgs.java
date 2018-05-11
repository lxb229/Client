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

import org.kohsuke.args4j.Option;

/**
 * 应用程序启动参数
 * 
 * @author kidal
 *
 */
public class NinaApplicationArgs {
	/** logback配置默认位置 */
	public static final String DEFAULT_LOGBACK_LOCATION = "res/app-logback.xml";
	/** Application context默认位置 */
	public static final String DEFAULT_CONTEXT_LOCATION = "file:res/app-context.xml";
	/** 控制台模式 */
	public static final String MODE_CONSOLE = "console";
	/** 服务器模式 */
	public static final String MODE_SERVER = "server";

	/** 是否启用控制台 */
	@Option(name = "-mode", usage = "启动模式")
	private String mode = MODE_CONSOLE;
	/** logback位置 */
	@Option(name = "-logback", usage = "logback配置位置")
	private String logbackLocation = DEFAULT_LOGBACK_LOCATION;

	/** app-context位置 */
	@Option(name = "-context", usage = "application context配置位置")
	private String contextLocation = DEFAULT_CONTEXT_LOCATION;

	/**
	 * 创建默认的应用程序启动参数
	 */
	public NinaApplicationArgs() {
	}

	// getters

	public String getMode() {
		return mode;
	}

	public String getLogbackLocation() {
		return logbackLocation;
	}

	public String getContextLocation() {
		return contextLocation;
	}
}
