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

import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.codec.Coder;
import org.treediagram.nina.network.codec.CoderManager;
import org.treediagram.nina.network.command.FacadeMethodInvokeContext;
import org.treediagram.nina.network.exception.ParameterException;
import org.treediagram.nina.network.model.Request;

/**
 * {@link InBody}注解的参数
 * 
 * @author kidal
 * 
 */
public class InBodyParameter implements Parameter {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(InBodyParameter.class);

	/**
	 * 注解
	 */
	private final InBody annotation;

	/**
	 * 参数名
	 */
	private final String parameterName;

	/**
	 * 类型
	 */
	private final Class<?> parameterClass;

	/**
	 * 编码解码器
	 */
	private final CoderManager coders;

	/**
	 * 创建{@link InBody}注解的参数
	 */
	public InBodyParameter(InBody annotation, Method method, CoderManager coders, int i) throws NotFoundException {
		this.annotation = annotation;
		this.parameterName = (StringUtils.isNoneBlank(annotation.value()) ? annotation.value() : parseParamaterName(
				method, i));
		this.parameterClass = method.getParameterTypes()[i];
		this.coders = coders;
	}

	/**
	 * {@link Parameter#getType()}
	 */
	@Override
	public ParameterType getType() {
		return ParameterType.IN_BODY;
	}

	/**
	 * {@link Parameter#isSupportConvert()}
	 */
	@Override
	public boolean isSupportConvert() {
		return true;
	}

	/**
	 * {@link Parameter#getValue(Request, IoSession, FacadeMethodInvokeContext)}
	 */
	@Override
	public Object getValue(Request<?> request, IoSession session, FacadeMethodInvokeContext context) {
		Coder coder = coders.getCoder(request.getCode());
		return coder.getInBody(request.getBody(), this, context);
	}

	/**
	 * 解析参数名
	 */
	private String parseParamaterName(Method method, int index) throws NotFoundException {
		final Class<?> cls = method.getDeclaringClass();
		if (cls.isInterface()) {
			FormattingTuple message = MessageFormatter.format("接口 {} 的方法 {} 的参数必须指定 InBody 注释的 value(参数名推测只能使用在非接口类)",
					method.getDeclaringClass().getName(), method.getName());
			logger.error(message.getMessage());
			throw new ParameterException(message.getMessage());
		}

		final ClassPool pool = ClassPool.getDefault();
		final CtClass ctcls = pool.get(cls.getName());
		final CtClass[] params = new CtClass[method.getParameterTypes().length];
		for (int i = 0; i < method.getParameterTypes().length; i++) {
			params[i] = pool.getCtClass(method.getParameterTypes()[i].getName());
		}

		final CtMethod ctmethod = ctcls.getDeclaredMethod(method.getName(), params);
		final MethodInfo methodInfo = ctmethod.getMethodInfo();
		final CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		final LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
				.getAttribute(LocalVariableAttribute.tag);
		final int pos = Modifier.isStatic(ctmethod.getModifiers()) ? 0 : 1;
		final String name = attr.variableName(index + pos);

		return name;
	}

	// getters and setters

	public InBody getAnnotation() {
		return annotation;
	}

	public String getParameterName() {
		return parameterName;
	}

	public Class<?> getParameterClass() {
		return parameterClass;
	}
}
