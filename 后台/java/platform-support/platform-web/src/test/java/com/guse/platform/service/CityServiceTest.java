package com.guse.platform.service;

import java.sql.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.guse.platform.TestBase;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.CityService;
import com.guse.platform.service.doudou.SystemProductService;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.service.system.UsersTestService;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class CityServiceTest extends TestBase {
	
	@Autowired
	private CityService cityService;
	
	/*
	 * 分页查询用户
	 */
	@Test
	public void getCityName(){
		System.out.println(cityService.getCityByIp("182.150.136.177"));
		System.out.println(cityService.getCityByIp("119.4.253.2"));
		System.out.println(new Date(new Long("1516780062999")));
		System.out.println(new Date(new Long("1516876141999")));
	}
	
}
