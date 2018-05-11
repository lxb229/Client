package com.palmjoys.yf1b.act.order.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "订单模块")
public interface OrderDefine {
	// module
	@SocketModule("订单模块")
	int ORDER = 9;
	
	static final int ORDER_COMMAND_BASE = ORDER*100;
	static final int ORDER_ERROR_BASE = (0-ORDER)*1000;
	static final int ORDER_COMMAND_BASE_NOTIFY = ORDER*10000;
	
	//command id
	//获取充值兑换比例
	int ORDER_COMMAND_EXCHANAGE_PERCENT = ORDER_COMMAND_BASE + 1;
	//充值金币
	int ORDER_COMMAND_RMB2GOLDMONEY = ORDER_COMMAND_BASE + 2;
	//提现
	int ORDER_COMMAND_GOLDMONEY2RMB = ORDER_COMMAND_BASE + 3;
	//查询提现订单
	int ORDER_COMMAND_GOLDMONEY2RMB_QUERY = ORDER_COMMAND_BASE + 4;
	//推送消息(充值消息)
	int ORDER_COMMAND_RMB2GOLDMONEY_NOTIFY = ORDER_COMMAND_BASE_NOTIFY + 1;
	
	// error code
	@SocketCode("订单不存在")
	int ORDER_ERROR_UNEXIST = ORDER_ERROR_BASE - 1;
	@SocketCode("错误的金额")
	int ORDER_ERROR_MONEY = ORDER_ERROR_BASE - 2;
	@SocketCode("订单正在处理")
	int ORDER_ERROR_ORDERTRANS = ORDER_ERROR_BASE - 3;
	@SocketCode("命令参数错误")
	int ORDER_ERROR_PARAM = ORDER_ERROR_BASE - 4;
	@SocketCode("订单处理玩家必须一样")
	int ORDER_ERROR_TRANSPLAYER = ORDER_ERROR_BASE - 5;
	
}
