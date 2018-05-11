package com.guse.apple_reverse.Main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.guse.apple_reverse.netty.HttpServer;
import com.guse.apple_reverse.netty.SocketServer;
import com.guse.apple_reverse.netty.WebSocketServer;

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
		CONSOLE_LOG.info("Stock server start");
		run();
	}

	/**
	 * @Fields factory : spring上下文
	 */
	public static ApplicationContext factory;

	public static void run() {
		CONSOLE_LOG.info("Load applicationContext");
		// 加载spring上下文
		factory = new FileSystemXmlApplicationContext("config/applicationContext.xml");
		
		// 启动socket服务
		new Thread(new Runnable() {
			public void run() {
				CONSOLE_LOG.info("Start Socket Service");
				SocketServer server = factory.getBean(SocketServer.class);
				try {
					server.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		// 启动websocket服务
		new Thread(new Runnable() {
			public void run() {
				CONSOLE_LOG.info("Start WebSocket Service");
				WebSocketServer server = factory.getBean(WebSocketServer.class);
				try {
					server.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		// 启动http服务
		new Thread(new Runnable() {
			public void run() {
				CONSOLE_LOG.info("Start HTTP Service");
				HttpServer server = factory.getBean(HttpServer.class);
				try{
					server.run();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
