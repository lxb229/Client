package com.palmjoys.yf1b.act.mall.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "商城模块")
public interface MallDefine {
	@SocketModule("商城模块")
	int MALL = 9;

	static final int MALL_COMMAND_BASE = MALL * 100;
	static final int MALL_ERROR_BASE = (0 - MALL) * 1000;
	static final int MALL_COMMAND_BASE_NOTIFY = MALL * 10000;
	
	//command id
	//获取商品列表
	int MALL_COMMAND_GET_MALL_LIST = MALL_COMMAND_BASE + 1;
	//购买指定物品
	int MALL_COMMAND_ITEM_BUY = MALL_COMMAND_BASE + 2;
	//购买指定物品成功
	int MALL_COMMAND_ITEM_BUY_OK = MALL_COMMAND_BASE + 3;
		
	
}
