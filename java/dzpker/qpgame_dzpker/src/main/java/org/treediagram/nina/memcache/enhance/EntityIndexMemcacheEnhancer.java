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

import static org.treediagram.nina.memcache.enhance.EnhancerConstants.CLASS_SUFFIX;
import static org.treediagram.nina.memcache.enhance.EnhancerConstants.FIELD_ENTITY;
import static org.treediagram.nina.memcache.enhance.EnhancerConstants.FIELD_SERVICE;
import static org.treediagram.nina.memcache.enhance.EnhancerConstants.METHOD_GET_ENTITY;
import static org.treediagram.nina.memcache.enhance.EnhancerConstants.TYPE_CACHED_ENTITY_CONFIG;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;

import javax.naming.ConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.treediagram.nina.core.reflect.ReflectionUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.ChangeIndex;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.exception.EnhanceException;
import org.treediagram.nina.memcache.exception.MemcacheConfigurationException;
import org.treediagram.nina.memcache.service.IndexEnhanceService;

/**
 * 实体索引增强器
 * 
 * @author kidal
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EntityIndexMemcacheEnhancer implements Enhancer {
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
		ClassClassPath classClassPath = new ClassClassPath(EntityIndexMemcacheEnhancer.class);
		classPool.insertClassPath(classClassPath);

		// 收集Object的方法
		for (Method method : Object.class.getDeclaredMethods()) {
			objectMethods.add(method);
		}
	}

	/**
	 * 增强服务
	 */
	private IndexEnhanceService service;

	/**
	 * 增强类的构造器映射
	 */
	private ConcurrentHashMap<Class, Constructor<? extends IEntity>> constructors = new ConcurrentHashMap<Class, Constructor<? extends IEntity>>();

	/**
	 * 初始化方法
	 */
	public void initialize(IndexEnhanceService service) {
		this.service = service;
	}

	@Override
	public <T extends IEntity> T transform(T entity) {
		Class<? extends IEntity> clz = entity.getClass();
		if (service == null) {
			FormattingTuple message = MessageFormatter.format("实体类[{}]所对应的缓存服务对象不存在", clz.getSimpleName());
			logger.error(message.getMessage());
			throw new EnhanceException(entity, message.getMessage());
		}
		try {
			Constructor constructor = getConstructor(clz);
			return (T) constructor.newInstance(entity, service);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.arrayFormat("实体类[{}]增强失败:{}", new Object[] {
					clz.getSimpleName(), e.getMessage(), e });
			logger.error(message.getMessage());
			throw new EnhanceException(entity, message.getMessage(), e);
		}
	}

	/**
	 * 获取增强类构造器
	 * 
	 * @param clz 实体类
	 * @return
	 */
	private <T extends IEntity> Constructor<T> getConstructor(Class<T> clz) throws Exception {
		if (constructors.containsKey(clz)) {
			return (Constructor<T>) constructors.get(clz);
		}

		synchronized (clz) {
			if (constructors.containsKey(clz)) {
				return (Constructor<T>) constructors.get(clz);
			}
			Class current = createEnhancedClass(clz);
			Constructor<T> constructor = current.getConstructor(clz, IndexEnhanceService.class);
			constructors.put(clz, constructor);
			return constructor;
		}
	}

	/**
	 * 创建增强类构造器
	 * 
	 * @param clz 实体类
	 * @return 增强类对象
	 */
	private Class createEnhancedClass(final Class clz) throws Exception {
		final CtClass enhancedClz = buildCtClass(clz);
		buildFields(clz, enhancedClz);
		buildConstructor(clz, enhancedClz);
		buildEnhancedEntityMethods(clz, enhancedClz);

		ReflectionUtils.doWithMethods(clz, new MethodCallback() {
			@Override
			public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
				Enhance enhance = method.getAnnotation(Enhance.class);
				if (enhance == null) {
					// 不需要增强的桥接方法
					try {
						buildMethod(clz, enhancedClz, method);
					} catch (Exception e) {
						throw new IllegalArgumentException("无法增强类[" + clz.getSimpleName() + "]的方法[" + method.getName()
								+ "](桥接方法)", e);
					}
				} else {
					ChangeIndex changeIndex = null;
					int idx = 0;
					Annotation[][] annos = method.getParameterAnnotations();
					for (int i = 0; i < annos.length; i++) {
						for (Annotation anno : annos[i]) {
							if (anno instanceof ChangeIndex) {
								if (changeIndex != null) {
									throw new MemcacheConfigurationException("实体[" + clz.getName() + "]的方法["
											+ method.getName() + "]配置错误:索引属性域不唯一");
								}
								changeIndex = (ChangeIndex) anno;
								idx = i;
							}
						}
					}
					if (changeIndex == null) {
						// 普通的增强方法
						try {
							buildEnhanceMethod(clz, enhancedClz, method, enhance);
						} catch (Exception e) {
							throw new IllegalArgumentException("无法增强类[" + clz.getSimpleName() + "]的方法["
									+ method.getName() + "](普通的增强方法)", e);
						}
					} else {
						// 带更改索引属性值的增强方法
						try {
							buildEnhanceMethodWithChangeIndex(clz, enhancedClz, method, enhance, changeIndex, idx);
						} catch (Exception e) {
							throw new IllegalArgumentException("无法增强类[" + clz.getSimpleName() + "]的方法["
									+ method.getName() + "](带更改索引属性值的增强方法)", e);
						}
					}
				}
			}
		}, new MethodFilter() 
		{
			@Override
			public boolean matches(Method method) {
				if (objectMethods.contains(method)) {
					return false;
				}
				if (Modifier.isFinal(method.getModifiers()) || Modifier.isStatic(method.getModifiers())
						|| Modifier.isPrivate(method.getModifiers())) {
					return false;
				}
				if (method.isSynthetic() && method.getName().equals("getId")) {
					return false;
				}
				return true;
			}
		});

		/*
		 * for (Method method : clz.getDeclaredMethods()) { // 跳过 final / static
		 * / private 的方法 if (Modifier.isFinal(method.getModifiers()) ||
		 * Modifier.isStatic(method.getModifiers()) ||
		 * Modifier.isPrivate(method.getModifiers()) ) { continue; } if
		 * (method.isSynthetic() && method.getName().equals("getId")) {
		 * continue; } Enhance enhance = method.getAnnotation(Enhance.class); if
		 * (enhance == null) { // 不需要增强的桥接方法 buildMethod(clz, enhancedClz,
		 * method); } else { ChangeIndex changeIndex = null; int idx = 0;
		 * Annotation[][] annos = method.getParameterAnnotations(); for (int i =
		 * 0; i < annos.length; i++) { for (Annotation anno : annos[i]) { if
		 * (anno instanceof ChangeIndex) { if (changeIndex != null) { throw new
		 * ConfigurationException("实体[" + clz.getName() + "]的方法[" +
		 * method.getName() + "]配置错误:索引属性域不唯一"); } changeIndex = (ChangeIndex)
		 * anno; idx = i; } } } if (changeIndex == null) { // 普通的增强方法
		 * buildEnhanceMethod(clz, enhancedClz, method, enhance); } else { //
		 * 带更改索引属性值的增强方法 buildEnhanceMethodWithChangeIndex(clz, enhancedClz,
		 * method, enhance, changeIndex, idx); } } }
		 */
		return enhancedClz.toClass();
	}

	/**
	 * 创建增强类对象的增强代理方法
	 */
	private void buildEnhanceMethodWithChangeIndex(Class clz, CtClass enhancedClz, Method method, Enhance enhance,
			ChangeIndex changeIndex, int idx) throws Exception {
		if (StringUtils.isNotEmpty(enhance.value())) {
			throw new ConfigurationException("不支持的增强方法配置");
		}

		Class<?>[] ignoreExceptions = enhance.ignoreExceptions();
		if (ignoreExceptions.length != 0) {
			throw new ConfigurationException("不支持的增强方法配置");
		}

		// 创建方法定义
		Class<?> returnType = method.getReturnType();
		CtMethod ctMethod = new CtMethod( //
				classPool.get(returnType.getName()), //
				method.getName(), //
				toCtClassArray(method.getParameterTypes()), enhancedClz);
		ctMethod.setModifiers(Modifier.PUBLIC);
		if (method.getExceptionTypes().length != 0) {
			ctMethod.setExceptionTypes(toCtClassArray(method.getExceptionTypes()));
		}

		/**
		 * 代码增强结果
		 * 
		 * <pre>
		 * CachedEntityConfig config = service.getEntityConfig();
		 * Object prev = config.getIndexValue(&quot;owner&quot;, this);
		 * entity.setOwner(owner);
		 * service.changeIndexValue(&quot;owner&quot;, this, prev);
		 * service.writeBack(entity);
		 * </pre>
		 */
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append( //
				"{" + //
						TYPE_CACHED_ENTITY_CONFIG + " config = " + FIELD_SERVICE + ".getEntityConfig();" + //
						"Object prev = config.getIndexValue(\"" + changeIndex.value() + "\", this);");
		if (returnType == void.class) {
			bodyBuilder.append(FIELD_ENTITY + "." + method.getName() + "($$);" + //
					FIELD_SERVICE + ".changeIndexValue(\"" + changeIndex.value() + "\", this, prev);" + //
					FIELD_SERVICE + ".writeBack(entity);" + //
					"}");
		} else {
			bodyBuilder.append(returnType.getName() + " result = " + FIELD_ENTITY + "." + method.getName() + "($$);" + //
					FIELD_SERVICE + ".changeIndexValue(\"" + changeIndex.value() + "\", this, prev);" + //
					FIELD_SERVICE + ".writeBack(entity);" + //
					"return result;" + //
					"}");
		}
		ctMethod.setBody(bodyBuilder.toString());
		enhancedClz.addMethod(ctMethod);
	}

	/**
	 * 创建增强类对象的增强代理方法
	 */
	private void buildEnhanceMethod(Class clz, CtClass enhancedClz, Method method, Enhance enhance) throws Exception {
		// 创建方法定义
		Class<?> returnType = method.getReturnType();
		CtMethod ctMethod = new CtMethod( //
				classPool.get(returnType.getName()), //
				method.getName(), //
				toCtClassArray(method.getParameterTypes()), enhancedClz);
		ctMethod.setModifiers(Modifier.PUBLIC);
		if (method.getExceptionTypes().length != 0) {
			ctMethod.setExceptionTypes(toCtClassArray(method.getExceptionTypes()));
		}
		// 设置方法体
		if (returnType == void.class) {
			/**
			 * 代码增强结果 <code>
			 * entity.[methodName]([args]);
			 * service.writeBack(entity);
			 * </code>
			 */
			ctMethod.setBody("{" + //
					FIELD_ENTITY + "." + method.getName() + "($$);" + //
					FIELD_SERVICE + ".writeBack(entity);" + //
					"}");
		} else if (StringUtils.isBlank(enhance.value())) {
			/**
			 * 代码增强结果 <code>
			 * [retType] result = entity.[methodName]([args]);
			 * service.writeBack(entity);
			 * return result;
			 * </code>
			 */
			String ret = returnType.isArray() ? toArrayTypeDeclare(returnType) : returnType.getName();
			ctMethod.setBody("{" + //
					ret + " result =" + //
					FIELD_ENTITY + "." + method.getName() + "($$);" + //
					FIELD_SERVICE + ".writeBack(entity);" + //
					"return result;" + //
					"}");
		} else {
			/**
			 * 代码增强结果 <code>
			 * [retType] result = entity.[methodName]([args]);
			 * if (String.valueOf(result).equals("[enhance.value]")) {
			 * 		service.writeBack(entity);
			 * }
			 * return result;
			 * </code>
			 */
			ctMethod.setBody("{" + //
					method.getReturnType().getName() + " result = " + FIELD_ENTITY + "." + method.getName() + "($$);" + //
					"if (String.valueOf(result).equals(\"" + enhance.value() + "\")) {" + //
					FIELD_SERVICE + ".writeBack(entity);" + //
					"}" + //
					"return result;" + //
					"}");
		}

		// 添加异常捕获代码
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

		enhancedClz.addMethod(ctMethod);
	}

	/**
	 * 创建增强类对象的普通代理方法
	 */
	private void buildMethod(Class clz, CtClass enhancedClz, Method method) throws Exception {
		// 创建方法定义
		Class<?> returnType = method.getReturnType();
		CtMethod ctMethod = new CtMethod( //
				classPool.get(returnType.getName()), //
				method.getName(), //
				toCtClassArray(method.getParameterTypes()), enhancedClz);
		ctMethod.setModifiers(method.getModifiers());
		if (method.getExceptionTypes().length != 0) {
			ctMethod.setExceptionTypes(toCtClassArray(method.getExceptionTypes()));
		}

		// 设置方法体
		if (returnType == void.class) {
			// entity.[method.name]([args]);
			ctMethod.setBody("{" + //
					FIELD_ENTITY + "." + method.getName() + "($$);" + //
					"}");
		} else {
			// return entity.[method.name]([args]);
			ctMethod.setBody("{" + //
					"return " + FIELD_ENTITY + "." + method.getName() + "($$);" + //
					"}");
		}
		enhancedClz.addMethod(ctMethod);
	}

	/**
	 * 创建增强类对象
	 * 
	 * <pre>
	 * public class [entityClz.name]$ENHANCED extends [entityClz.name] implements EnhancedEntity {
	 * }
	 * </pre>
	 */
	private CtClass buildCtClass(Class entityClz) throws Exception {
		CtClass superClz = classPool.get(entityClz.getName());
		CtClass result = classPool.makeClass(entityClz.getCanonicalName() + CLASS_SUFFIX);
		result.setSuperclass(superClz);
		result.setInterfaces(new CtClass[] { classPool.get(EnhancedEntity.class.getName()) });
		return result;
	}

	/**
	 * 创建增强类的属性域
	 * 
	 * <pre>
	 * private final [entityClz.name] entity;
	 * private final CacheService service;
	 * </pre>
	 */
	private void buildFields(Class entityClz, CtClass enhancedClz) throws Exception {
		// 创建服务域
		CtField field = new CtField(classPool.get(IndexEnhanceService.class.getName()), //
				FIELD_SERVICE, enhancedClz);
		field.setModifiers(Modifier.PRIVATE + Modifier.FINAL);
		enhancedClz.addField(field);
		// 创建实体域
		field = new CtField(classPool.get(entityClz.getName()), FIELD_ENTITY, enhancedClz);
		field.setModifiers(Modifier.PRIVATE + Modifier.FINAL);
		enhancedClz.addField(field);
	}

	/**
	 * 创建增强类的构造方法
	 * 
	 * <pre>
	 * public [enhancedClz.name]([entityClz.name] entity, CacheService service) {
	 *     this.entity = entity;
	 *     this.service = service;
	 * }
	 * </pre>
	 */
	private void buildConstructor(Class entityClz, CtClass enhancedClz) throws Exception {
		CtConstructor constructor = new CtConstructor(toCtClassArray(entityClz, IndexEnhanceService.class), enhancedClz);
		constructor.setBody("{ " + //
				"this." + FIELD_ENTITY + " = $1; " + //
				"this." + FIELD_SERVICE + " = $2;" + //
				"}");
		constructor.setModifiers(Modifier.PUBLIC);
		enhancedClz.addConstructor(constructor);
	}

	/**
	 * 创建增强类的接口方法
	 */
	private void buildEnhancedEntityMethods(Class entityClass, CtClass enhancedClz) throws Exception {
		// 创建方法描述对象
		CtClass returnType = classPool.get(IEntity.class.getName());
		String mname = METHOD_GET_ENTITY;
		CtClass[] parameters = new CtClass[0];
		ConstPool cp = enhancedClz.getClassFile2().getConstPool();
		String desc = Descriptor.ofMethod(returnType, parameters);
		MethodInfo methodInfo = new MethodInfo(cp, mname, desc);
		// 添加JsonIgnore的注释到方法
		AnnotationsAttribute annoAttr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
		javassist.bytecode.annotation.Annotation annot = new javassist.bytecode.annotation.Annotation(
				"org.codehaus.jackson.annotate.JsonIgnore", cp);
		annoAttr.addAnnotation(annot);
		methodInfo.addAttribute(annoAttr);
		// 创建方法对象
		CtMethod method = CtMethod.make(methodInfo, enhancedClz);
		/**
		 * 代码增强结果 <code>
		 * public IEntity getEntity() {
		 *     return this.entity;
		 * }
		 * </code>
		 */
		method.setBody("{" + //
				"return this." + FIELD_ENTITY + ";" + //
				"}");
		method.setModifiers(Modifier.PUBLIC);
		enhancedClz.addMethod(method);
	}

	/**
	 * 将{@link Class}转换为{@link CtClass}
	 */
	private CtClass[] toCtClassArray(Class<?>... classes) throws NotFoundException {
		if (classes == null || classes.length == 0) {
			return new CtClass[0];
		}
		CtClass[] result = new CtClass[classes.length];
		for (int i = 0; i < classes.length; i++) {
			result[i] = classPool.get(classes[i].getName());
		}
		return result;
	}

	/**
	 * 获取数组类型的声明定义
	 */
	private String toArrayTypeDeclare(Class<?> arrayClz) {
		Class<?> type = arrayClz.getComponentType();
		return type.getName() + "[]";
	}
}
