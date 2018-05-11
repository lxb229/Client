package com.guse.platform.controller.gm;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.guse.platform.TestBase;

public class PlayerInfoControllerTest extends TestBase{
	
	@Autowired
	private PlayerInfoController playerInfoController;
	
	@Test
	public void Users() {
		Object users = playerInfoController.users("3002701", null, null, null, null, null);
		
		System.out.println(JSON.toJSONString(users));
	}

}
