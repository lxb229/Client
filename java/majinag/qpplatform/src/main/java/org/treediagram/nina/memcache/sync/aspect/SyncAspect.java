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

package org.treediagram.nina.memcache.sync.aspect;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.sync.ObjectSyncCallable;
import org.treediagram.nina.memcache.sync.ObjectSyncService;
import org.treediagram.nina.memcache.sync.annotation.LockParameters;

/**
 * 加锁拦截切面
 * 
 * @author kidalsama
 * 
 */
@Aspect
@Component
public class SyncAspect {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(SyncAspect.class);

	/**
	 * 对象同步服务
	 */
	@Resource
	private ObjectSyncService objectSyncService;

	/**
	 * 执行锁定的方法
	 */
	@Around("@annotation(lockParameters)")
	public Object execute(final ProceedingJoinPoint pjp, LockParameters lockParameters) throws Throwable {
		// 获取签名
		Signature signature = pjp.getSignature();

		// 函数签名
		if (signature instanceof MethodSignature) {
			// 同步执行
			final Method method = ((MethodSignature) signature).getMethod();
			return objectSyncService.syncCall(lockParameters.value(), method, new ObjectSyncCallable<Object>() {
				@Override
				public Object call(Object[] args, boolean success) throws Throwable {
					if (logger.isDebugEnabled() && !success) {
						logger.error("锁定方法[{}.{}]失败", pjp.getTarget().getClass().getName(), method.getName());
					}
					return pjp.proceed(args);
				}
			}, pjp.getArgs());
		} else {
			logger.error("不支持的拦截切面：{}", signature);
			return pjp.proceed();
		}
	}
}
