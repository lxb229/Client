package com.wangzhixuan.test.service;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.wangzhixuan.model.Jackpot;
import com.wangzhixuan.service.IJackpotService;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class JackpotServiceTest extends BaseTest {
	
	@Autowired
	private IJackpotService jackpotService;
	/*
	 * 分页查询用户
	 */
	@Test
	public void getJackpot(){
		Jackpot jackpot = jackpotService.getJackpot();
		System.out.println(JSONObject.toJSON(jackpot));
	}
	
}
