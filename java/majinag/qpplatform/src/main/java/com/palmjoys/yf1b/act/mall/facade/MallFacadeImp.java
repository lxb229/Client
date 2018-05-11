package com.palmjoys.yf1b.act.mall.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.mall.service.MallService;

@Component
public class MallFacadeImp implements MallFacade{
	@Autowired
	private MallService mallService;

	@Override
	public Object mall_itemlist(Long accountId) {
		return mallService.mall_itemlist(accountId);
	}

	@Override
	public Object mall_item_buy(Long accountId, int itemId) {
		return mallService.mall_item_buy(accountId, itemId);
	}

	@Override
	public Object mall_item_buy_ok(Long accountId, String order_no, String pay_price, String pay_time, int num) {
		return mallService.mall_item_buy_ok(accountId, order_no, pay_price, pay_time, num);
	}

}
