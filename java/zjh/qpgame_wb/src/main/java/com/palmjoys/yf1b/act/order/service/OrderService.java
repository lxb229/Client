package com.palmjoys.yf1b.act.order.service;

public interface OrderService {
	//获取兑换比例
	public Object order_get_exchanage_percent(Long accountId);
	//充值金币
	public Object order_charge_rmb2goldmoney(Long accountId, int rmb, int payType);
	//金币提现人民币
	public Object order_charge_goldmoney2rmb(Long accountId, int goldMoney);
	//提现订单查询
	public Object order_charge_goldmoney2rmb_query(Long accountId);
}
