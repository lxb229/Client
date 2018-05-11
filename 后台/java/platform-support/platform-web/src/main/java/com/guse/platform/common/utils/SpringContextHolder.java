package com.guse.platform.common.utils;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @date 2017年7月14日 下午7:04:45 
 * @version V1.0
 */
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /** 
    * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量. 
    */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext; 
    }

    /** 
    * 取得存储在静态变量中的ApplicationContext. 
    */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /** 
    * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型. 
    */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /** 
    * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型. 
    */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBeansOfType(clazz);
    }

    /** 
    * 清除applicationContext静态变量. 
    */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }

}
