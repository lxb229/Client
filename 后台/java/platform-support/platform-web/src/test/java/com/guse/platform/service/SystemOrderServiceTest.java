package com.guse.platform.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.guse.platform.TestBase;
import com.guse.platform.common.base.Result;
import com.guse.platform.service.doudou.SystemOrderService;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class SystemOrderServiceTest extends TestBase {
	
	@Autowired
	private SystemOrderService orderService;
	
	
	/*
	 * 分页查询用户
	 */
	@Test
	public void WeixinNotifyUrl(){
//		Result<String> signReturn = orderService.phoneNotifyUrl(parrStr);
//		System.out.println("反向验证签名结果："+signReturn);
		
	}
	
}
