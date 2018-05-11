package com.palmjoys.yf1b.act.mall.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.mall.service.MallService;

@Component
public class MallFacadeImp implements MallFacade{
	@Autowired
	private MallService mallService;
	
	
	@Override
	public Object mall_get_mall_List(Long accountId) {
		return mallService.mall_get_mall_List(accountId);
	}

	@Override
	public Object mall_buy_item(Long accountId, int itemId) {
		return mallService.mall_buy_item(accountId, itemId);
	}

	@Override
	public Object mall_charge_goldmoney(Long accountId, int rmb, int goldmoney) {
		return mallService.mall_charge_goldmoney(accountId, rmb, goldmoney);
	}

	@Override
	public Object mall_charge_diamond(Long accountId, int rmb, int diamond) {
		return mallService.mall_charge_goldmoney(accountId, rmb, diamond);
	}

	@Override
	public Object mall_charge_notify() {
		return Result.valueOfSuccess();
	}

}
