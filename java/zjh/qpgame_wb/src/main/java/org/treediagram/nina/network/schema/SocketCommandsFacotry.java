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

package org.treediagram.nina.network.schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;
import org.treediagram.nina.network.codec.CoderManager;
import org.treediagram.nina.network.command.Commands;
import org.treediagram.nina.network.command.FacadeMethod;

/**
 * 命令集合工厂
 * 
 * @author kidal
 * 
 */
public class SocketCommandsFacotry implements FactoryBean<Commands>, BeanPostProcessor {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(SocketCommandsFacotry.class);

	/**
	 * 命令集合
	 */
	private final Commands socketCommands = new Commands();

	/**
	 * 编码解码器
	 */
	private final CoderManager coders;

	/**
	 * 创建命令集合工厂
	 */
	public SocketCommandsFacotry(CoderManager coders) {
		this.coders = coders;
	}

	/**
	 * {@link BeanPostProcessor#postProcessBeforeInitialization(Object, String)}
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 * {@link BeanPostProcessor#postProcessAfterInitialization(Object, String)}
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// 获取注解
		Class<?> cls = findAnnotationDeclaringClassAndInterface(NetworkFacade.class, bean.getClass());
		if (cls == null) {
			return bean;
		}

		// 必须注解网络前端
		NetworkFacade socketFacade = cls.getAnnotation(NetworkFacade.class);
		if (socketFacade == null) {
			return bean;
		}

		// 解析方法
		for (Method method : cls.getMethods()) {
			final NetworkApi networkApi = AnnotationUtils.findAnnotation(method, NetworkApi.class);
			if (networkApi == null) {
				continue;
			}

			try {
				registerCommand(networkApi.value(), cls, bean, method);
			} catch (Exception e) {
				FormattingTuple message = MessageFormatter.format("注册指令 {} 到对应方法 {} 时失败", networkApi.value(), method);
				logger.error(message.getMessage());
				throw new FatalBeanException(message.getMessage(), e);
			}
		}

		return bean;
	}

	/**
	 * {@link FactoryBean#getObject()}
	 */
	@Override
	public Commands getObject() throws Exception {
		return socketCommands;
	}

	/**
	 * {@link FactoryBean#getObjectType()}
	 */
	@Override
	public Class<?> getObjectType() {
		return Commands.class;
	}

	/**
	 * {@link FactoryBean#isSingleton()}
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * 注册命令
	 */
	private void registerCommand(int id, Class<?> cls, Object bean, Method method) throws Exception {
		FacadeMethod invoker = new FacadeMethod(id, bean, method, coders);
		getObject().register(invoker);
	}

	/**
	 * 查找注解
	 */
	private Class<?> findAnnotationDeclaringClassAndInterface(Class<? extends Annotation> annotationType,
			Class<? extends Object> clazz) {
		Assert.notNull(annotationType, "Annotation type must not be null");
		if (clazz == null || clazz.equals(Object.class)) {
			return null;
		}
		if (AnnotationUtils.isAnnotationDeclaredLocally(annotationType, clazz)) {
			return clazz;
		}
		for (Class<?> clz : clazz.getInterfaces()) {
			if (AnnotationUtils.isAnnotationDeclaredLocally(annotationType, clz)) {
				return clz;
			}
		}
		return findAnnotationDeclaringClassAndInterface(annotationType, clazz.getSuperclass());
	}
}
