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

package org.treediagram.nina.memcache.enhance;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.treediagram.nina.core.reflect.ReflectionUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.exception.EnhanceException;
import org.treediagram.nina.memcache.service.EntityMemcache;

/**
 * 实体缓存增强器
 * 
 * @author kidal
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EntityMemcacheEnhancer implements Enhancer {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(EntityMemcacheEnhancer.class);

	/**
	 * 类池
	 */
	private static final ClassPool classPool = ClassPool.getDefault();

	/**
	 * 对象方法集合
	 */
	private static final Set<Method> objectMethods = new HashSet<Method>();

	/**
	 * 初始化对象
	 */
	static {
		// The default ClassPool returned by a static method
		// ClassPool.getDefault() searches the same path that the underlying JVM
		// (Java virtual machine) has. If a program is running on a web
		// application server such as JBoss and Tomcat, the ClassPool object may
		// not be able to find user classes since such a web application server
		// uses multiple class loaders as well as the system class loader. In
		// that case, an additional class path must be registered to the
		// ClassPool.
		ClassClassPath classClassPath = new ClassClassPath(EntityMemcacheEnhancer.class);
		classPool.insertClassPath(classClassPath);

		// 收集Object的方法
		for (Method method : Object.class.getDeclaredMethods()) {
			objectMethods.add(method);
		}
	}

	/**
	 * 实体缓存
	 */
	private EntityMemcache memcache;

	/**
	 * 构造函数缓存
	 */
	private ConcurrentMap<Class, Constructor<? extends IEntity>> constructors = new ConcurrentHashMap<Class, Constructor<? extends IEntity>>();

	/**
	 * 创建实体缓存增强器
	 */
	public EntityMemcacheEnhancer(EntityMemcache memcache) {
		this.memcache = memcache;
	}

	/**
	 * {@link Enhancer#transform(IEntity)}
	 */
	@Override
	public <T extends IEntity> T transform(T entity) throws EnhanceException {
		// 获取试题类型
		Class<? extends IEntity> entityClass = entity.getClass();
		try {
			// 获取试题构造方法
			Constructor constructor = getConstructor(entityClass);

			// 构造实体
			return (T) constructor.newInstance(entity, memcache);
		} catch (Exception e) {
			// 处理异常
			FormattingTuple message = MessageFormatter.arrayFormat("实体类 {} 增强失败 {}",
					new Object[] { entityClass.getSimpleName(), e.getMessage(), e });

			// 记录异常
			logger.error(message.getMessage());

			// 抛出异常
			throw new EnhanceException(entity, message.getMessage(), e);
		}
	}

	/**
	 * 获取构造函数
	 */
	private <T extends IEntity> Constructor<T> getConstructor(Class<? extends IEntity> entityClass) throws Exception {
		// 从缓存查询
		if (constructors.containsKey(entityClass)) {
			return (Constructor<T>) constructors.get(entityClass);
		}

		// 锁定
		synchronized (entityClass) {
			// 从缓存查询
			if (constructors.containsKey(entityClass)) {
				return (Constructor<T>) constructors.get(entityClass);
			}

			// 创建增强类
			Class enhancedClass = createEnhancedClass(entityClass);

			// 获取构造方法
			Constructor constructor = enhancedClass.getConstructor(entityClass, EntityMemcache.class);

			// 缓存构造方法
			constructors.put(entityClass, constructor);

			// 返回
			return constructor;
		}
	}

	/**
	 * 创建增强类型
	 */
	private Class createEnhancedClass(final Class entityClass) throws Exception {
		// 类
		final CtClass enhancedClass = buildClass(entityClass);

		// 域
		buildFields(entityClass, enhancedClass);

		// 构造方法
		buildConstructor(entityClass, enhancedClass);

		// 接口方法
		buildEnhancedEntityMethods(entityClass, enhancedClass);

		// 增强方法
		ReflectionUtils.doWithMethods(entityClass, new MethodCallback() {
			@Override
			public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
				// 获取增强注解
				Enhance enhance = method.getAnnotation(Enhance.class);

				// 方法分为增强方法和不增强方法
				if (enhance == null) {
					// 不需要增强
					try {
						buildMethod(entityClass, enhancedClass, method);
					} catch (Exception e) {
						throw new IllegalArgumentException("无法增强类 " + entityClass.getName() + " 的方法 "
								+ method.getName() + "(桥接方法)", e);
					}
				} else {
					// 增强
					try {
						buildEnhanceMethod(entityClass, enhancedClass, method, enhance);
					} catch (Exception e) {
						throw new IllegalArgumentException("无法增强类 " + entityClass.getName() + " 的方法 "
								+ method.getName() + "(增强)", e);
					}
				}
			}
		}, new MethodFilter() {
			@Override
			public boolean matches(Method method) {
				// Object方法
				if (objectMethods.contains(method)) {
					return false;
				}

				// 获取访问权限
				int mod = method.getModifiers();

				// 最终、静态、私有
				if (Modifier.isFinal(mod) || Modifier.isStatic(mod) || Modifier.isPrivate(mod)) {
					return false;
				}

				// 合成、获取主键
				if (method.isSynthetic() && method.getName().equals(EnhancerConstants.METHOD_GET_ID)) {
					return false;
				}

				// 匹配
				return true;
			}
		});

		// 完成
		return enhancedClass.toClass();
	}

	/**
	 * 创建增强类普通方法
	 */
	private void buildMethod(final Class entityClass, final CtClass enhancedClass, Method method) throws Exception {
		// 返回类型
		Class<?> returnType = method.getReturnType();

		// 桥接方法
		CtMethod ctMethod = new CtMethod( //
				classPool.get(returnType.getName()), //
				method.getName(), //
				toCtClassArray(method.getParameterTypes()), //
				enhancedClass);

		// 访问器
		ctMethod.setModifiers(method.getModifiers());

		// 异常
		if (method.getExceptionTypes().length > 0) {
			ctMethod.setExceptionTypes(toCtClassArray(method.getExceptionTypes()));
		}

		// 增强
		if (returnType == void.class) {
			// entity.[method.name]([args]);
			ctMethod.setBody("{" + //
					EnhancerConstants.FIELD_ENTITY + "." + method.getName() + "($$);" + //
					"}");
		} else {
			// return entity.[method.name]([args]);
			ctMethod.setBody("{" + //
					"return " + EnhancerConstants.FIELD_ENTITY + "." + method.getName() + "($$);" + //
					"}");
		}

		// 完成
		enhancedClass.addMethod(ctMethod);
	}

	/**
	 * 创建增强代理方法
	 */
	private void buildEnhanceMethod(final Class entityClass, final CtClass enhancedClass, Method method, Enhance enhance)
			throws Exception {
		// 返回类型
		Class<?> returnType = method.getReturnType();

		// 增强方法
		CtMethod ctMethod = new CtMethod( //
				classPool.get(returnType.getName()), //
				method.getName(), //
				toCtClassArray(method.getParameterTypes()), //
				enhancedClass);

		// 访问器
		ctMethod.setModifiers(Modifier.PUBLIC);

		// 异常
		if (method.getExceptionTypes().length > 0) {
			ctMethod.setExceptionTypes(toCtClassArray(method.getExceptionTypes()));
		}

		// 增强代码
		StringBuilder builder = new StringBuilder();
		builder.append("{");

		// 增强
		if (returnType == void.class) {
			/**
			 * <code>
			 * entity.[methodName]([args]);
			 * memcache.writeBack(entity);
			 * </code>
			 */
			builder.append(EnhancerConstants.FIELD_ENTITY + "." + method.getName() + "($$);");
			builder.append(EnhancerConstants.FIELD_SERVICE + ".writeBack(entity);");
		} else if (StringUtils.isBlank(enhance.value())) {
			String ret = returnType.isArray() ? toArrayTypeDeclare(returnType) : returnType.getName();
			/**
			 * <code>
			 * [ret] result = entity.[methodName]([args]);
			 * memcache.writeBack(entity);
			 * return result;
			 * </code>
			 */
			builder.append(ret + " result = " + EnhancerConstants.FIELD_ENTITY + "." + method.getName() + "($$);");
			builder.append(EnhancerConstants.FIELD_SERVICE + ".writeBack(entity);");
			builder.append("return result;");
		} else {
			/**
			 * <code>
			 * [ret] result = entity.[methodName]([args]);
			 * if (String.valueOf(result).equals("[enhance.value()]")) {
			 * 		memcache.writeBack(entity);
			 * }
			 * return result;
			 * </code>
			 */
			builder.append(method.getReturnType().getName() + " result = " + EnhancerConstants.FIELD_ENTITY + "."
					+ method.getName() + "($$);");
			builder.append("if (String.valueOf(result).equals(\"" + enhance.value() + "\")) {");
			builder.append(EnhancerConstants.FIELD_SERVICE + ".writeBack(entity);");
			builder.append("}");
			builder.append("return result;");
		}

		// 设置方法体
		builder.append("}");
		String body = builder.toString();
		ctMethod.setBody(body);

		// 异常
		if (enhance.ignoreExceptions().length > 0) {
			for (Class ignoreException : enhance.ignoreExceptions()) {
				CtClass eType = classPool.get(ignoreException.getName());
				/**
				 * <code>
				 * try {
				 * 		[body];
				 * }
				 * catch([ignoreException] e)
				 * {
				 * 		memcache.writeBack(entity); 
				 * 		throw e; 
				 * }
				 * </code>
				 */
				ctMethod.addCatch("{" + //
						EnhancerConstants.FIELD_SERVICE + ".writeBack(entity);" + //
						"throw $e;" + //
						"}", eType);
			}
		}

		// 完成
		enhancedClass.addMethod(ctMethod);
	}

	/**
	 * 构建类
	 * 
	 * <pre>
	 * public class [entityClass.name]$ENHANCED extends [entityClass.name] implements EnhancedEntity {
	 * }
	 * </pre>
	 */
	private CtClass buildClass(Class entityClass) throws Exception {
		CtClass superCtClass = classPool.get(entityClass.getName());
		CtClass ctClass = classPool.makeClass(entityClass.getCanonicalName() + EnhancerConstants.CLASS_SUFFIX);
		ctClass.setSuperclass(superCtClass);
		ctClass.setInterfaces(new CtClass[] { classPool.get(EnhancedEntity.class.getName()) });
		return ctClass;
	}

	/**
	 * 构建域
	 * 
	 * <pre>
	 * private final [entityClass.name] entity;
	 * </pre>
	 */
	private void buildFields(final Class entityClass, final CtClass enhancedClass) throws Exception {
		CtField ctField = new CtField(classPool.get(entityClass.getName()), EnhancerConstants.FIELD_ENTITY,
				enhancedClass);
		ctField.setModifiers(Modifier.PRIVATE + Modifier.FINAL);
		enhancedClass.addField(ctField);

		ctField = new CtField(classPool.get(EntityMemcache.class.getName()), EnhancerConstants.FIELD_SERVICE,
				enhancedClass);
		ctField.setModifiers(Modifier.PRIVATE + Modifier.FINAL);
		enhancedClass.addField(ctField);
	}

	/**
	 * 构建构造方法
	 */
	private void buildConstructor(final Class entityClass, final CtClass enhancedClass) throws Exception {
		CtConstructor ctConstructor = new CtConstructor( //
				toCtClassArray(entityClass, EntityMemcache.class), //
				enhancedClass);
		ctConstructor.setBody("{ " + //
				"this." + EnhancerConstants.FIELD_ENTITY + " = $1;" + //
				"this." + EnhancerConstants.FIELD_SERVICE + " = $2;" + //
				"}");
		ctConstructor.setModifiers(Modifier.PUBLIC);
		enhancedClass.addConstructor(ctConstructor);
	}

	/**
	 * 创建增强类的接口方法
	 */
	private void buildEnhancedEntityMethods(final Class entityClass, final CtClass enhancedClass) throws Exception {
		// 创建方法描述对象
		CtClass returnType = classPool.get(IEntity.class.getName());
		String methodName = EnhancerConstants.METHOD_GET_ENTITY;
		CtClass[] parameters = new CtClass[0];
		ConstPool cp = enhancedClass.getClassFile2().getConstPool();
		String desc = Descriptor.ofMethod(returnType, parameters);
		MethodInfo methodInfo = new MethodInfo(cp, methodName, desc);
		// 添加JsonIgnore的注释到方法
		AnnotationsAttribute annoAttr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
		javassist.bytecode.annotation.Annotation annot = new javassist.bytecode.annotation.Annotation(
				"org.codehaus.jackson.annotate.JsonIgnore", cp);
		annoAttr.addAnnotation(annot);
		methodInfo.addAttribute(annoAttr);
		// 创建方法对象
		CtMethod method = CtMethod.make(methodInfo, enhancedClass);
		/**
		 * 代码增强结果 <code>
		 * public IEntity getEntity() {
		 *     return this.entity;
		 * }
		 * </code>
		 */
		method.setBody("{" + //
				"return this." + EnhancerConstants.FIELD_ENTITY + ";" + //
				"}");
		method.setModifiers(Modifier.PUBLIC);
		enhancedClass.addMethod(method);
	}

	/**
	 * 将{@link Class}转换为{@link CtClass}
	 */
	private CtClass[] toCtClassArray(Class... classes) throws NotFoundException {
		if (classes == null || classes.length == 0) {
			return new CtClass[0];
		} else {
			CtClass[] result = new CtClass[classes.length];
			for (int i = 0; i < classes.length; i++) {
				result[i] = classPool.get(classes[i].getName());
			}
			return result;
		}
	}

	/**
	 * 获取数组类型的声明定义
	 */
	private String toArrayTypeDeclare(Class<?> arrayClass) {
		Class<?> type = arrayClass.getComponentType();
		return type.getName() + "[]";
	}
}
