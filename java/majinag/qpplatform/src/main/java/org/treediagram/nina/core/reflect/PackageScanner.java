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

package org.treediagram.nina.core.reflect;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 包扫描器
 * 
 * @author kidal
 * 
 */
public class PackageScanner {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(PackageScanner.class);

	/**
	 * 类集合
	 */
	private final Collection<Class<?>> clazzCollection = new HashSet<Class<?>>();

	/**
	 * 创建
	 */
	public PackageScanner(final String... packageNames) {
		for (String packageName : packageNames) {
			try {
				final String packageDirectory = packageName.replace('.', '/');
				final Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
						.getResources(packageDirectory);
				while (urls.hasMoreElements()) {
					final URL url = urls.nextElement();
					if ("file".equals(url.getProtocol())) {
						final File directory = new File(url.getPath());
						if (!directory.isDirectory()) {
							throw new RuntimeException("package:[" + packageName + "] is not directory");
						}
						clazzCollection.addAll(PackageScanner.scanClassFromDirectory(packageName, directory));
					} else if ("jar".equals(url.getProtocol())) {
						final JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
						clazzCollection.addAll(PackageScanner.scanClassFromJar(packageName, jar));
					}
				}
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
		}
	}

	/**
	 * 创建
	 */
	public PackageScanner(final Collection<String> packageNames) {
		this(packageNames.toArray(new String[packageNames.size()]));
	}

	/**
	 * 获取类集合
	 */
	public Collection<Class<?>> getClazzCollection() {
		return this.clazzCollection;
	}

	/**
	 * 从jar扫描类
	 */
	public static Collection<Class<?>> scanClassFromJar(final String packageName, final JarFile jar) {
		final Enumeration<JarEntry> jarEntries = jar.entries();
		final Pattern pattern = Pattern.compile("(" + packageName.replace('.', '/') + ".*)\\.class");
		final Collection<Class<?>> clazzCollection = new HashSet<Class<?>>();

		while (jarEntries.hasMoreElements()) {
			final JarEntry entry = jarEntries.nextElement();
			final String name = entry.getName();
			final Matcher matcher = pattern.matcher(name.replace(File.separatorChar, '/'));
			if (matcher.find()) {
				final String className = matcher.group(1).replace('/', '.');
				try {
					final Class<?> clazz = Class.forName(className);
					clazzCollection.add(clazz);
				} catch (ClassNotFoundException e) {
					logger.error("无法加载类[{}]", className, e);
				}
			}
		}
		return clazzCollection;
	}

	/**
	 * 从目录扫描类
	 */
	public static Collection<Class<?>> scanClassFromDirectory(final String packageName, final File directory) {
		final Stack<File> scanDirectories = new Stack<File>();
		final Collection<File> classFiles = new ArrayList<File>();
		final FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					scanDirectories.push(file);
					return false;
				}
				return file.getName().matches(".*\\.class$");
			}
		};

		scanDirectories.push(directory);

		while (!scanDirectories.isEmpty()) {
			final File scanDirectory = scanDirectories.pop();
			Collections.addAll(classFiles, scanDirectory.listFiles(fileFilter));
		}
		final Pattern pattern = Pattern.compile("(" + packageName.replace('.', '/') + ".*)\\.class");
		final Collection<Class<?>> clazzCollection = new HashSet<Class<?>>();
		for (File file : classFiles) {
			final Matcher matcher = pattern.matcher(file.getAbsolutePath().replace(File.separatorChar, '/'));
			if (matcher.find()) {
				final String className = matcher.group(1).replace('/', '.');
				try {
					final Class<?> clazz = Class.forName(className);
					clazzCollection.add(clazz);
				} catch (ClassNotFoundException e) {
					logger.error("无法加载类[{}]", className, e);
				}
			}
		}

		return clazzCollection;
	}
}
