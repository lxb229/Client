package com.wangzhixuan.test.service;

import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.wangzhixuan.service.IJoinPartyLogService;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class JoinPartyLogServiceTest extends BaseTest {
	
	@Autowired
	private IJoinPartyLogService joinPartyLogService;
	/*
	 * 分页查询用户
	 */
	@Test
	public void getRoomPeopleNum(){
		Map<String, Object> map = joinPartyLogService.getRoomPartyNum(null, "812758", 100063, new Date());
		if(map != null && map.get("peopleNum") != null ){
			
			System.out.println(map.toString());
		}
	}
	
}
