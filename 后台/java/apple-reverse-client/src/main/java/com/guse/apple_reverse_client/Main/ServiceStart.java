package com.guse.apple_reverse_client.Main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.guse.apple_reverse_client.netty.ChangeIPService;
import com.guse.apple_reverse_client.netty.EchoClient;
import com.guse.apple_reverse_client.netty.QueryLineService;

/** 
* @ClassName: ServiceStart 
* @Description: 服务启动类
* @author Fily GUSE
* @date 2017年9月13日 上午11:21:02 
*  
*/
public class ServiceStart {
	// 输出到控制台的日志
	public final static Logger CONSOLE_LOG = LoggerFactory.getLogger("ConsoleLog");
	// 只记录文件的日志
	public final static Logger INFO_LOG = LoggerFactory.getLogger("InfoLog");
	
	public static void main(String[] args) {
		CONSOLE_LOG.info("Stock Client server start");
		run();
	}
	
	/** 
	* @Fields factory : spring上下文
	*/
	public static ApplicationContext factory;
	
	public static void run() {
		INFO_LOG.info("Load applicationContext");
		// 加载spring上下文
		factory = new FileSystemXmlApplicationContext("config/applicationContext.xml");
         
		// 启动socket客服端服务
		new Thread() {
			public void run() {
				EchoClient client = factory.getBean(EchoClient.class);
				try {
					client.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		// 初始化查询线程
		new Thread() {
			public void run(){
				factory.getBean(QueryLineService.class).init();
			}
		}.start();
		// 自动切换IP
		new Thread() {
			public void run() {
				factory.getBean(ChangeIPService.class).run();
			}
		}.start();
		
	}

}
