package com.wangzhixuan.test.service;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.wangzhixuan.model.vo.LuckVo;
import com.wangzhixuan.service.IWarehouseOutService;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class WarehouseOutServiceTest extends BaseTest {
	
	@Autowired
	private IWarehouseOutService warehouseOutService;
	/*
	 * 分页查询用户
	 */
	@Test
	public void getJackpot(){
		LuckVo silverList = warehouseOutService.outputCommodity("077901", 1, new LuckVo());
		System.out.println(JSONObject.toJSON(silverList));
		silverList = warehouseOutService.outputCommodity("077901", 2, new LuckVo());
		System.out.println(JSONObject.toJSON(silverList));
		silverList = warehouseOutService.outputCommodity("077901", 3, new LuckVo());
		System.out.println(JSONObject.toJSON(silverList));
		silverList = warehouseOutService.outputCommodity("077901", 4, new LuckVo());
		System.out.println(JSONObject.toJSON(silverList));
		silverList = warehouseOutService.outputCommodity("077901", 5, new LuckVo());
		System.out.println(JSONObject.toJSON(silverList));
	}
	
}
