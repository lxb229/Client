
package com.guse.platform.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import com.guse.platform.service.doudou.MQService;


public class MQUtil implements ApplicationListener<ContextRefreshedEvent>{
	
	@Autowired
	private MQService mqService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		mqService.startConsumer();
		System.out.println("消费者启动成功");
	}

}
