package com.wangzhixuan.test.service;


import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class CommodityServiceTest extends BaseTest {
	
	@Autowired
	private ICommodityService commodityService;
	/*
	 * 分页查询用户
	 */
	@Test
	public void getJackpot(){
		List<String> silverList = commodityService.getAllSilverCommodity();
		
		System.out.println(JSONObject.toJSON(silverList));
	}
	
}
