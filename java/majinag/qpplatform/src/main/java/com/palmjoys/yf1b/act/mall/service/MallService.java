package com.palmjoys.yf1b.act.mall.service;

public interface MallService {
	//获取所有商品列表
	public Object mall_itemlist(Long accountId);
	//购买指定商品
	public Object mall_item_buy(Long accountId, int itemId);
	//APP商品内购,购买成功(由客户端保证数据)
	public Object mall_item_buy_ok(Long accountId, String order_no, String pay_price, String pay_time, int num);

}
