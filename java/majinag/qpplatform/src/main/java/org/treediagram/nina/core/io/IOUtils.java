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

package org.treediagram.nina.core.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * IO工具类
 * 
 * @author kidal
 * 
 */
public class IOUtils {
	/**
	 * nina管理的基础目录
	 */
	public static final String NINA_DIR = ".nina";

	/**
	 * 初始化
	 */
	static {
		final File ninaFile = new File(NINA_DIR);

		if (ninaFile.exists()) {
			if (ninaFile.isFile()) {
				throw new IllegalStateException("[.nina]不是文件夹而是一个文件");
			}
		} else {
			if (!ninaFile.mkdir()) {
				throw new IllegalStateException("[.nina]文件夹创建失败");
			}
		}
	}

	/**
	 * 打开nina管理的文件
	 * 
	 * @param path 文件路径
	 * @return nina管理的文件
	 */
	public static File newNinaFile(String path) {
		String pathname = combinePath(NINA_DIR, path);
		return new File(pathname);
	}

	/**
	 * 关闭
	 */
	public static void close(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
		}
	}

	/**
	 * 合并路径
	 * 
	 * @param paths 路径数组
	 * @return 合并后的路径
	 */
	public static String combinePath(String... paths) {
		if (paths.length == 0) {
			return "";
		} else {
			boolean firstPathAppended = false;
			StringBuilder builder = new StringBuilder();

			for (int i = 0; i < paths.length; i++) {
				final String path = paths[i];

				if (path == null) {
					continue;
				}

				if (path.length() == 0) {
					continue;
				}

				if (firstPathAppended) {
					builder.append("/");
				} else {
					firstPathAppended = true;
				}

				final char lastChar = path.charAt(path.length() - 1);

				if (lastChar == '\\' || lastChar == '/') {
					builder.append(path.substring(0, path.length() - 1));
				} else {
					builder.append(path);
				}
			}

			return builder.toString();
		}
	}
}
