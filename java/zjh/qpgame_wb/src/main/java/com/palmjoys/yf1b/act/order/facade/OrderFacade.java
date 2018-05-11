package com.palmjoys.yf1b.act.order.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;
import com.palmjoys.yf1b.act.order.model.OrderDefine;

@NetworkFacade
public interface OrderFacade {
	
	@NetworkApi(value = OrderDefine.ORDER_COMMAND_EXCHANAGE_PERCENT, 
			desc="获取兑换比例")
	Object order_get_exchanage_percent(@InSession Long accountId);
	
	@NetworkApi(value = OrderDefine.ORDER_COMMAND_RMB2GOLDMONEY, 
			desc="充值金币")
	Object order_charge_rmb2goldmoney(@InSession Long accountId,
			@InBody(value = "rmb", desc = "人民币数量") int rmb,
			@InBody(value = "payType", desc = "支付类型(1=WX,2=支付宝)") int payType);
	
	@NetworkApi(value = OrderDefine.ORDER_COMMAND_GOLDMONEY2RMB, 
			desc="金币提现人民币")
	Object order_charge_goldmoney2rmb(@InSession Long accountId,
			@InBody(value = "goldMoney", desc = "金币数量") int goldMoney);
	
	@NetworkApi(value = OrderDefine.ORDER_COMMAND_GOLDMONEY2RMB_QUERY, 
			desc="提现订单查询")
	Object order_charge_goldmoney2rmb_query(@InSession Long accountId);

	@NetworkApi(value = OrderDefine.ORDER_COMMAND_RMB2GOLDMONEY_NOTIFY, 
			desc="推送消息(充值成功)")
	Object order_rmb2goldmoney_notify();
	
}
