package com.palmjoys.yf1b.act.mall.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;
import com.palmjoys.yf1b.act.mall.model.MallDefine;

@NetworkFacade
public interface MallFacade {
	@NetworkApi(value = MallDefine.MALL_COMMAND_GET_MALL_LIST,
			desc="获取所有商品列表")
	Object mall_itemlist(@InSession Long accountId);

	@NetworkApi(value = MallDefine.MALL_COMMAND_ITEM_BUY,
			desc="购买指定商品")
	Object mall_item_buy(@InSession Long accountId,
			@InBody(value = "itemId", desc = "商品Id") int itemId);
	
	@NetworkApi(value = MallDefine.MALL_COMMAND_ITEM_BUY_OK,
			desc="商品购买成功")
	Object mall_item_buy_ok(@InSession Long accountId,
			@InBody(value = "order_no", desc = "订单号") String order_no,
			@InBody(value = "pay_price", desc = "付款金额") String pay_price,
			@InBody(value = "pay_time", desc = "付款时间") String pay_time,
			@InBody(value = "num", desc = "房卡数量") int num);
}
