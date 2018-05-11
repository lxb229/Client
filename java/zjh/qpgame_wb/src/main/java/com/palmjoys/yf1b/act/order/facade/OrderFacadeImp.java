package com.palmjoys.yf1b.act.order.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.order.service.OrderService;

@Component
public class OrderFacadeImp implements OrderFacade{
	@Autowired
	private OrderService orderService;

	@Override
	public Object order_get_exchanage_percent(Long accountId) {
		return orderService.order_get_exchanage_percent(accountId);
	}

	@Override
	public Object order_charge_rmb2goldmoney(Long accountId, int rmb, int payType) {
		return orderService.order_charge_rmb2goldmoney(accountId, rmb, payType);
	}

	@Override
	public Object order_charge_goldmoney2rmb(Long accountId, int goldMoney) {
		return orderService.order_charge_goldmoney2rmb(accountId, goldMoney);
	}

	@Override
	public Object order_charge_goldmoney2rmb_query(Long accountId) {
		return orderService.order_charge_goldmoney2rmb_query(accountId);
	}

	@Override
	public Object order_rmb2goldmoney_notify() {
		return Result.valueOfSuccess();
	}

}
