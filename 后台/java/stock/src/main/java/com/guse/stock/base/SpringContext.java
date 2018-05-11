package com.guse.stock.base;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.guse.stock.netty.handler.dispose.service.InputDispose;

public class SpringContext implements ApplicationContextAware {
	/** spring容器上下文 */
	private static ApplicationContext applicationContext = null;
	
	/** 服务类 */
	private static InputDispose inputDsp;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContext.applicationContext = applicationContext;
	}
	
	public final static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
	
	public final static <T> Collection<T> getBeansOfType(Class<T> clazz) {
		return applicationContext.getBeansOfType(clazz).values();
	}
	
	public final static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}
	
	
	@Resource
	public void setInputDsp(InputDispose inputDsp) {
		SpringContext.inputDsp = inputDsp;
	}
	
	public final static InputDispose getInputDsp() {
		return inputDsp;
	}

}
