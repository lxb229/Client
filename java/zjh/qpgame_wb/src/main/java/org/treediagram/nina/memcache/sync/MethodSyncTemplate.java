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

package org.treediagram.nina.memcache.sync;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treediagram.nina.memcache.sync.annotation.LockMe;
import org.treediagram.nina.memcache.sync.lock.LinkedLock;
import org.treediagram.nina.memcache.sync.lock.LinkedLockBuilder;

/**
 * 方法锁定模板
 * 
 * @author kidal
 * 
 */
public class MethodSyncTemplate {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(MethodSyncTemplate.class);

	/**
	 * 锁链构造器
	 */
	private LinkedLockBuilder linkedLockBuilder = null;

	/**
	 * 锁定参数
	 */
	private Map<Integer, LockMe> lockArgs = new HashMap<Integer, LockMe>();

	/**
	 * 创建锁定模板
	 */
	public MethodSyncTemplate(LinkedLockBuilder linkedLockBuilder, Method method) {
		this.linkedLockBuilder = linkedLockBuilder;

		Annotation[][] annotations = method.getParameterAnnotations();
		for (int i = 0; i < annotations.length; i++) {
			LockMe lockMe = null;
			for (Annotation annotation : annotations[i]) {
				if (annotation instanceof LockMe) {
					lockMe = (LockMe) annotation;
					break;
				}
			}
			if (lockMe != null) {
				lockArgs.put(i, lockMe);
			}
		}
	}

	/**
	 * 获取锁链
	 */
	@SuppressWarnings("rawtypes")
	public LinkedLock getLinkedLock(Object[] args) {
		List<Object> lockObjects = new ArrayList<Object>();

		for (Entry<Integer, LockMe> e : lockArgs.entrySet()) {
			Integer index = e.getKey();
			LockMe lockMe = e.getValue();
			Object arg = args[index];

			if (arg != null) {
				if (!lockMe.value()) {
					lockObjects.add(arg);
				} else if (arg instanceof Collection) {
					for (Object object : (Collection) arg) {
						if (object != null) {
							lockObjects.add(object);
						}
					}
				} else if (arg.getClass().isArray()) {
					for (int i = 0; i < Array.getLength(arg); i++) {
						Object object = Array.get(arg, i);
						if (object != null) {
							lockObjects.add(object);
						}
					}
				} else if (arg instanceof Map) {
					for (Object object : ((Map) arg).values()) {
						if (object != null) {
							lockObjects.add(object);
						}
					}
				} else {
					logger.error("不支持的类型 {}", arg.getClass());
				}
			}
		}

		if (lockObjects.isEmpty()) {
			if (logger.isDebugEnabled()) {
				logger.error("没有获得锁目标");
			}
			return null;
		}

		Object[] objects = lockObjects.toArray();

		if (logger.isDebugEnabled()) {
			logger.debug("锁定目标：{}", Arrays.toString(objects));
		}

		return linkedLockBuilder.createLinkedLock(objects);
	}
}
