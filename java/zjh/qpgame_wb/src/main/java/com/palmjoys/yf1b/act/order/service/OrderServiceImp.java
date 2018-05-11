package com.palmjoys.yf1b.act.order.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.common.manager.ErrorCodeManager;
import com.palmjoys.yf1b.act.order.entity.BuyOrderEntity;
import com.palmjoys.yf1b.act.order.entity.SellOrderEntity;
import com.palmjoys.yf1b.act.order.manager.OrderCfgManager;
import com.palmjoys.yf1b.act.order.manager.OrderManager;
import com.palmjoys.yf1b.act.order.model.OrderCfgVo;
import com.palmjoys.yf1b.act.order.model.OrderDefine;
import com.palmjoys.yf1b.act.order.model.OrderPayVo;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Service
public class OrderServiceImp implements OrderService{
	@Autowired
	private OrderCfgManager orderCfgManager;
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private ErrorCodeManager errManager;
	@Autowired
	private WalletManager walletManager;
		

	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);
			
	@Override
	public Object order_get_exchanage_percent(Long accountId) {
		OrderCfgVo retVo = orderCfgManager.getChanagePercent();
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object order_charge_rmb2goldmoney(Long accountId, int rmb, int payType) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL, 
					errManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
		}
		if(rmb <= 0){
			return Result.valueOfError(OrderDefine.ORDER_ERROR_MONEY, 
					errManager.Error2Desc(OrderDefine.ORDER_ERROR_MONEY), null);
		}
		OrderCfgVo retVo = orderCfgManager.getChanagePercent();
		long goldMoney = retVo.rmb2goldMoney*rmb;
		
		BuyOrderEntity orderEntity = orderManager.loadOrCreateBuyOrder(accountEntity.getStarNO(), rmb, goldMoney);
		orderEntity.setPayType(payType);
		OrderPayVo payVo = orderManager.sendChargeData(orderEntity);
		if(payVo.code < 0){
			logger.debug("创建支付订单数据失败,订单号="+orderEntity.getId() + ",消息="+payVo.msg);
			return Result.valueOfError(payVo.code, payVo.msg, null);	
		}
		logger.debug("创建支付订单数据发送成功,订单号="+orderEntity.getId() + ",消息="+payVo.msg);
		return Result.valueOfSuccess(payVo.msg);
	}

	@Override
	public Object order_charge_goldmoney2rmb(Long accountId, int goldMoney) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL, 
					errManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
		}
		
		OrderCfgVo retVo = orderCfgManager.getChanagePercent();
		if(goldMoney<=0 || ((goldMoney%retVo.goldMoney2Rmb)!=0)){
			return Result.valueOfError(OrderDefine.ORDER_ERROR_MONEY, 
					errManager.Error2Desc(OrderDefine.ORDER_ERROR_MONEY), null);
		}
		int ret = walletManager.checkEnough(accountId, 1, goldMoney);
		if(ret < 0){
			return Result.valueOfError(OrderDefine.ORDER_ERROR_MONEY, 
					errManager.Error2Desc(OrderDefine.ORDER_ERROR_MONEY), null);
		}
		
		List<String> orderIds = new ArrayList<>();
		int err = OrderDefine.ORDER_ERROR_ORDERTRANS;
		orderManager.lock();
		try{
			while(true){
				SellOrderEntity sellOrder = orderManager.findSellOrderOf_starNO(accountEntity.getStarNO());
				if(null != sellOrder){
					break;
				}
				
				int rmb = goldMoney/retVo.goldMoney2Rmb;
				sellOrder = orderManager.loadOrCreateSellOrder(accountEntity.getStarNO(), rmb, goldMoney, 0, "");
				orderIds.add(String.valueOf(sellOrder.getId()));
				walletManager.addRoomCard(accountId, 0-goldMoney, true);
				
				err = 0;
				break;
			}
			
		}finally{
			orderManager.unLock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, 
					errManager.Error2Desc(err), null);	
		}
		return Result.valueOfSuccess(orderIds);
	}

	@Override
	public Object order_charge_goldmoney2rmb_query(Long accountId) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL, 
					errManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
		}
		List<String> orderIds = new ArrayList<>();
		SellOrderEntity sellOrder = orderManager.findSellOrderOf_starNO(accountEntity.getStarNO());
		if(null != sellOrder){
			orderIds.add(String.valueOf(sellOrder.getId()));
		}
		return Result.valueOfSuccess(orderIds);
	}

}
