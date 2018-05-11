package com.palmjoys.yf1b.act.mall.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "商城模块")
public interface MallDefine {
	// module
	@SocketModule("商城模块")
	int MALL = 8;
	
	static final int MALL_COMMAND_BASE = MALL*100;
	static final int MALL_ERROR_BASE = (0-MALL)*1000;
	static final int MALL_COMMAND_BASE_NOTIFY = MALL*10000;
	
	//获取商城信息
	int MALL_COMMAND_GET_MALL_LIST = MALL_COMMAND_BASE + 1;
	//购买指定物品
	int MALL_COMMAND_BUY_ITEM = MALL_COMMAND_BASE + 2;
	//充值指定数量金币
	int MALL_COMMAND_BUY_GOLDMONEY = MALL_COMMAND_BASE + 3;
	//充值指定数量钻石
	int MALL_COMMAND_BUY_DIAMOND = MALL_COMMAND_BASE + 4;
	//推送消息(充值或购买成功)
	int MALL_COMMAND_CHARGE_NOTIFY = MALL_COMMAND_BASE_NOTIFY + 1;
	
	
	//error id
	@SocketCode("商城物品不存在")
	int MALL_ERROR_ITEM_UNEXIST = MALL_ERROR_BASE - 1;
	@SocketCode("充值金额错误")
	int MALL_ERROR_CHARGE_MONEY = MALL_ERROR_BASE - 2;
	
}
