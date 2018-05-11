package com.palmjoys.yf1b.act.gm.service;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.handler.JettyRequestHandler;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.gm.model.GMDefine;
import com.palmjoys.yf1b.act.order.entity.BuyOrderEntity;
import com.palmjoys.yf1b.act.order.manager.OrderManager;
import com.palmjoys.yf1b.act.order.model.OrderDefine;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

/**
 * 充值回调
 * */
@Service
public class GmImp_Charge implements JettyRequestHandler{
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private SessionManager sessionManager;

	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(GmImp_Charge.class);
			
	@Override
	public String getPath() {
		return GMDefine.GM_CMD_CHARGE;
	}

	@Override
	public byte[] handleRequest(String target, Request baseRequest, HttpServletRequest request) {
		String retStr = "success";
		orderManager.lock();
		try{
			while(true){
				String ubodingdan = request.getParameter("sdorderno");
				String ubozt = request.getParameter("status");
				logger.debug("收到购买订单回调通知,订单号="+ubodingdan + ",第三方通知支付状态="+ubozt);
				if(null==ubodingdan || null==ubozt){
					break;
				}
				
				long n_orderId = Long.valueOf(ubodingdan);
				int n_ubozt = Integer.parseInt(ubozt);
				if(n_ubozt != 1){
					break;
				}
				
				BuyOrderEntity orderEntity = orderManager.findBuyOrder_orderId(n_orderId);
				if(null == orderEntity){
					logger.debug("收到购买订单回调通知,订单不存在,订单号="+n_orderId);
					break;
				}
				int state = orderEntity.getState();
				if(state != OrderManager.STATE_ORDER_WAIT){
					logger.debug("收到购买订单回调通知,订单是已处理了的,订单号="+n_orderId);
					break;
				}
				
				String starNO = orderEntity.getStarNO();
				long goldMoney = orderEntity.getGoldMoney();
				AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
				if(null == accountEntity){
					logger.debug("收到购买订单回调通知,未找到订单玩家,订单号="+n_orderId);
					break;
				}
				
				long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				walletManager.addRoomCard(accountEntity.getId(), goldMoney, true);
				orderEntity.setState(OrderManager.STATE_ORDER_SUCESS);
				orderEntity.setTransTime(currTime);
				
				//推送消息
				@SuppressWarnings("rawtypes")
				org.treediagram.nina.network.model.Request pushMsg = org.treediagram.nina.network.model.Request.valueOf(OrderDefine.ORDER_COMMAND_RMB2GOLDMONEY_NOTIFY, Result.valueOfSuccess(goldMoney));				
				MessagePushQueueUtils.getPushQueue(sessionManager).push2(accountEntity.getId(), pushMsg);
				
				logger.debug("购买订单充值成功,订单号="+n_orderId);
				break;
			}
		}finally{
			orderManager.unLock();
		}
		byte []retBytes = null;
		try{
			retBytes = retStr.getBytes("utf8");
		}catch(Exception e){
		}
		return retBytes;
	}

}
