package com.guse.stock.Main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.guse.stock.netty.HttpServer;
import com.guse.stock.netty.SocketServer;
import com.guse.stock.netty.WebSocketServer;
import com.guse.stock.netty.handler.service.OrderQueueService;

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
		
		// 启动netty服务
		Thread socket = new Thread(new Runnable(){  
            public void run(){  
            	logger.info("Start Socket Service");
        		SocketServer server = factory.getBean(SocketServer.class);
        		try {
        			server.run();
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
            }});  
		socket.start(); 
        
        // 启动http服务
 		Thread http = new Thread(new Runnable(){  
             public void run(){  
             	logger.info("Start Http Service");
             	HttpServer server = factory.getBean(HttpServer.class);
         		try {
         			server.run();
         		} catch (Exception e) {
         			e.printStackTrace();
         		}
             }});  
         http.start(); 
         
      // 启动websocket服务
  		Thread websocket = new Thread(new Runnable(){  
              public void run(){  
              	logger.info("Start WebSocket Service");
              	WebSocketServer server = factory.getBean(WebSocketServer.class);
          		try {
          			server.run();
          		} catch (Exception e) {
          			e.printStackTrace();
          		}
              }});  
  		websocket.start(); 
	}

}
