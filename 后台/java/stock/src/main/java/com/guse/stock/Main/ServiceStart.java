package com.guse.stock.Main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.guse.stock.netty.SocketServer;
import com.guse.stock.zk.ZKRegistry;

/** 
* @ClassName: ServiceStart 
* @Description: 服务启动类
* @author Fily GUSE
* @date 2017年9月13日 上午11:21:02 
*  
*/
public class ServiceStart {
	private final static Logger logger = LoggerFactory.getLogger(ServiceStart.class);
	
	public static void main(String[] args) {
		logger.info("Stock server start");
		run();
	}
	
	/** 
	* @Fields factory : spring上下文
	*/
	public static ApplicationContext factory;
	
	public static void run() {
		logger.info("Load applicationContext");
		// 加载spring上下文
		factory = new FileSystemXmlApplicationContext("config/applicationContext.xml");
		// 注册zookeeper
		try {
			logger.info("Registry zookeeper");
			ZKRegistry registry = factory.getBean(ZKRegistry.class);
			registry.register();
		} catch(Exception e) { // zookeeper 注册失败就不需要启动netty服务了
			e.printStackTrace();
			return;
		}
		
		// 启动netty服务
		logger.info("Start Socket Service");
		SocketServer server = factory.getBean(SocketServer.class);
		try {
			server.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
