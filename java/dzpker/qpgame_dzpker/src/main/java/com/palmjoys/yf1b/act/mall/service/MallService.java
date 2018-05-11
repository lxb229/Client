package com.palmjoys.yf1b.act.mall.service;

public interface MallService {
	//获取商城信息列表
	public Object mall_get_mall_List(Long accountId);
	//购买商城物品
	public Object mall_buy_item(Long accountId, int itemId);
	//充值指定数量金币
	public Object mall_charge_goldmoney(Long accountId, int rmb, int goldmoney);
	//充值指定数量钻石
	public Object mall_charge_diamond(Long accountId, int rmb, int diamond);

}
