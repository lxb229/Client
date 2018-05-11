package com.palmjoys.yf1b.act.mall.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.mall.model.MallDefine;

@NetworkFacade
public interface MallFacade {
	
	@NetworkApi(value = MallDefine.MALL_COMMAND_GET_MALL_LIST,
			desc="获取商城信息列表")
	Object mall_get_mall_List(@InSession Long accountId);
	
	@NetworkApi(value = MallDefine.MALL_COMMAND_BUY_ITEM,
			desc="购买商城物品")
	Object mall_buy_item(@InSession Long accountId,
			@InBody(value = "itemId", desc = "物品配置Id") int itemId);
	
	@NetworkApi(value = MallDefine.MALL_COMMAND_BUY_GOLDMONEY,
			desc="充值指定数量金币")
	Object mall_charge_goldmoney(@InSession Long accountId,
			@InBody(value = "rmb", desc = "人民币数量") int rmb,
			@InBody(value = "goldmoney", desc = "金币数量") int goldmoney);
	
	@NetworkApi(value = MallDefine.MALL_COMMAND_BUY_DIAMOND,
			desc="充值指定数量钻石")
	Object mall_charge_diamond(@InSession Long accountId,
			@InBody(value = "rmb", desc = "人民币数量") int rmb,
			@InBody(value = "diamond", desc = "钻石数量") int diamond);
	
	@NetworkApi(value = MallDefine.MALL_COMMAND_CHARGE_NOTIFY,
			desc="购买或充值成功通知")
	Object mall_charge_notify();
	
}
