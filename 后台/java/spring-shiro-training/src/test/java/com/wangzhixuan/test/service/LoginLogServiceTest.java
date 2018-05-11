package com.wangzhixuan.test.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangzhixuan.service.ILoginLogService;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class LoginLogServiceTest extends BaseTest {
	
	@Autowired
	private ILoginLogService logService;
	/*
	 * 分页查询用户
	 */
	@Test
	public void getContinuousLogin(){
		
		List<Map<String, Object>> list = logService.getContinuousLogin("888888", new Date());
		System.out.println(list);
		
	}
	
}
