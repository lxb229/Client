package com.palmjoys.yf1b.act.order.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.common.manager.ErrorCodeManager;
import com.palmjoys.yf1b.act.order.entity.BuyOrderEntity;
import com.palmjoys.yf1b.act.order.entity.OrderCfgEntity;
import com.palmjoys.yf1b.act.order.entity.SellOrderEntity;
import com.palmjoys.yf1b.act.order.manager.OrderCfgManager;
import com.palmjoys.yf1b.act.order.manager.OrderManager;
import com.palmjoys.yf1b.act.order.model.OrderCfgVo;
import com.palmjoys.yf1b.act.order.model.OrderDefine;
import com.palmjoys.yf1b.act.order.model.OrderGmVo;
import com.palmjoys.yf1b.act.order.service.OrderService;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Component
@ConsoleBean
public class OrderCommand {
	@Autowired
	private Querier querier;
	@Autowired
	private OrderCfgManager orderCfgManager;
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private ErrorCodeManager errManager;
	@Autowired
	private OrderService orderService;
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private AccountManager accountManager;
	
	/**
	 * 获取购买订单列表
	 * start 查询开始位置
	 * num 查询数量
	 * sortStyle 排序方式(1=小到大,2=大到小)
	 * sortCondition 排序(1=订单状态,2=创建时间)
	 * match 匹配方式数组,位置为1有匹配要求,取对应参数(0=订单号,1=玩家Id,2=订单创建时间,3=订单状态)
	 * matchParam 匹配参数(0=订单号,1=玩家Id,2=查询开始时间和查询结束时间,3=订单状态)
	 * */
	@ConsoleCommand(name = "gm_order_query_buy_order_list", description = "获取购买订单列表")
	public Object gm_order_query_buy_order_list(int start, int num, int sortStyle, 
			int sortCondition, int[] match, Object []matchParam){
		if(start < 0){
			start = 0;
		}
		if(num <= 0){
			num = 50;
		}
		if(match.length != 4){
			return Result.valueOfError(-1, "接口参数错误", null);
		}
		
		String macthSql = "";
		int paramIndex=0;
		for(int i=0; i<4; i++){
			if(match[i] > 0){
				switch(i){
				case 0://订单号
					macthSql += " AND A.orderId=" + matchParam[paramIndex];
					paramIndex++;
					break;
				case 1://玩家Id
					macthSql += " AND A.starNO='" + matchParam[paramIndex] + "'";
					paramIndex++;
					break;
				case 2://订单创建时间
					macthSql += " AND A.createTime>=" + matchParam[paramIndex];
					paramIndex++;
					macthSql += " AND A.createTime<" + matchParam[paramIndex];
					paramIndex++;
					break;
				case 3://订单状态
					macthSql += " AND A.state=" + matchParam[paramIndex];
					paramIndex++;
					break;
				}
			}
		}
		
		long totalNum = 0;
		String cntSql = "SELECT COUNT(A.orderId) FROM BuyOrderEntity AS A WHERE 1=1" + macthSql;
		String querySql = "SELECT A.orderId, A.starNO, A.createTime, A.rmb, A.goldMoney, A.state, A.transTime, A.payType"
				+ " FROM BuyOrderEntity AS A"
				+ " WHERE 1=1" + macthSql
				+ " ORDER BY ";
		
		String sAsc = "ASC";
		if(sortStyle == 2){
			sAsc = "DESC";
		}
		
		String sType = "A.state ";
		if(sortCondition == 2){
			sType = "A.createTime ";
		}
		querySql += sType;
		querySql += sAsc;
		
		OrderGmVo retVo = new OrderGmVo();
		
		List<Object> retObjs = querier.listBySqlLimit(BuyOrderEntity.class, Object.class, cntSql, 0, 1);
		for(Object obj : retObjs){
			if(null != obj){
				if(obj instanceof Integer){
					totalNum = (Integer) obj;
				}else if(obj instanceof Long){
					totalNum = (Long) obj;
				}
			}
		}
		retVo.totalNum = (int) totalNum;
		retObjs = querier.listBySqlLimit(BuyOrderEntity.class, Object.class, querySql, start, num);
		for(Object obj : retObjs){
			Object []objArry = (Object[]) obj;
			long orderId = (Long) objArry[0];
			String starNO = (String) objArry[1];
			long createTime = (Long) objArry[2];
			int rmb = (Integer) objArry[3];
			long goldMoney = (Long) objArry[4];
			int state = (Integer) objArry[5];
			long transTime = (Long) objArry[6];
			int payType = (Integer) objArry[7];
			retVo.addItem(orderId, starNO, createTime, rmb, goldMoney, state, 
					transTime, "", 0, payType, "");
		}
		return Result.valueOfSuccess(retVo);
	}
	
	/**
	 * 获取出售订单列表
	 * start 查询开始位置
	 * num 查询数量
	 * sortStyle 排序方式(1=小到大,2=大到小)
	 * sortCondition 排序(1=订单状态,2=创建时间)
	 * match 匹配方式数组,位置为1有匹配要求,取对应参数(0=订单号,1=玩家Id,2=订单创建时间,3=订单状态)
	 * matchParam 匹配参数(0=订单号,1=玩家Id,2=查询开始时间和查询结束时间,3=订单状态)
	 * */
	@ConsoleCommand(name = "gm_order_query_sell_order_list", description = "获取出售订单列表")
	public Object gm_order_query_sell_order_list(int start, int num, int sortStyle, 
			int sortCondition, int[] match, Object []matchParam){
		if(start < 0){
			start = 0;
		}
		if(num <= 0){
			num = 50;
		}
		
		String macthSql = "";
		int paramIndex=0;
		for(int i=0; i<4; i++){
			if(match[i] > 0){
				switch(i){
				case 0://订单号
					macthSql += " AND A.orderId=" + matchParam[paramIndex];
					paramIndex++;
					break;
				case 1://玩家Id
					macthSql += " AND A.starNO='" + matchParam[paramIndex] + "'";
					paramIndex++;
					break;
				case 2://订单创建时间
					macthSql += " AND A.createTime>=" + matchParam[paramIndex];
					paramIndex++;
					macthSql += " AND A.createTime<" + matchParam[paramIndex];
					paramIndex++;
					break;
				case 3://订单状态
					macthSql += " AND A.state=" + matchParam[paramIndex];
					paramIndex++;
					break;
				}
			}
		}
		
		long totalNum = 0;
		String cntSql = "SELECT COUNT(A.orderId) FROM SellOrderEntity AS A WHERE 1=1" + macthSql;
		String querySql = "SELECT A.orderId, A.starNO, A.createTime, A.rmb, A.goldMoney, A.state, A.transTime,"
				+ " A.reMarks, A.transPlayer, A.payType, A.payAccount"
				+ " FROM SellOrderEntity AS A"
				+ " WHERE 1=1" + macthSql
				+ " ORDER BY ";
		
		String sAsc = "ASC";
		if(sortStyle == 2){
			sAsc = "DESC";
		}
		
		String sType = "A.state ";
		if(sortCondition == 2){
			sType = "A.createTime ";
		}
		querySql += sType;
		querySql += sAsc;
		
		OrderGmVo retVo = new OrderGmVo();
		
		List<Object> retObjs = querier.listBySqlLimit(SellOrderEntity.class, Object.class, cntSql, 0, 1);
		for(Object obj : retObjs){
			if(null != obj){
				if(obj instanceof Integer){
					totalNum = (Integer) obj;
				}else if(obj instanceof Long){
					totalNum = (Long) obj;
				}
			}
		}
		retVo.totalNum = (int) totalNum;
		retObjs = querier.listBySqlLimit(SellOrderEntity.class, Object.class, querySql, start, num);
		for(Object obj : retObjs){
			Object []objArry = (Object[]) obj;
			long orderId = (Long) objArry[0];
			String starNO = (String) objArry[1];
			long createTime = (Long) objArry[2];
			int rmb = (Integer) objArry[3];
			long goldMoney = (Long) objArry[4];
			int state = (Integer) objArry[5];
			long transTime = (Long) objArry[6];
			String reMarks = (String) objArry[7];
			int transPlayer = (Integer) objArry[8];
			int payType = (Integer) objArry[9];
			String payAccount = (String) objArry[10];
			retVo.addItem(orderId, starNO, createTime, rmb, goldMoney, state, 
					transTime, reMarks, transPlayer, payType, payAccount);
		}
		return Result.valueOfSuccess(retVo);
	}
	
	/**
	 * 获取兑换比例
	 * */
	@ConsoleCommand(name = "gm_order_get_exchanage_percent", description = "获取兑换比例")
	public Object gm_order_get_exchanage_percent(){
		OrderCfgVo retVo = orderCfgManager.getChanagePercent();
		return Result.valueOfSuccess(retVo);
	}
	
	/**
	 * 设置兑换比例
	 * rmb2goldMoney 人民币比游戏币
	 * goldMoney2Rmb 游戏币比人民币
	 * minRmb 最小人民币数
	 * */
	@ConsoleCommand(name = "gm_order_set_exchanage_percent", description = "设置兑换比例")
	public Object gm_order_set_exchanage_percent(int rmb2goldMoney, int goldMoney2Rmb, int minRmb){
		OrderCfgEntity entity = orderCfgManager.loadOrCreate();
		entity.setRmb2goldMoney(rmb2goldMoney);
		entity.setGoldMoney2Rmb(goldMoney2Rmb);
		entity.setMinRmb(minRmb);
		return Result.valueOfSuccess();
	}
	
	/**
	 * 开始处理订单
	 * orderId 订单号
	 * transPlayer 处理玩家
	 * */
	@ConsoleCommand(name = "gm_order_order_handle_start", description = "开始处理订单")
	public Object gm_order_order_handle_start(long orderId, int transPlayer){
		int err = OrderDefine.ORDER_ERROR_UNEXIST;
		orderManager.lock();
		try{
			while(true){
				SellOrderEntity sellOrder = orderManager.findSellOrder_orderId(orderId);
				if(null == sellOrder){
					break;
				}
				if(sellOrder.getState() != OrderManager.STATE_ORDER_WAIT){
					err = OrderDefine.ORDER_ERROR_ORDERTRANS;
					break;
				}
				sellOrder.setState(OrderManager.STATE_ORDER_TRANSING);
				sellOrder.setTransPlayer(transPlayer);
				
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
		return Result.valueOfSuccess();
	}
	
	/**
	 * 订单处理完成
	 * orderId 订单号
	 * transPlayer 订单处理人
	 * result 处理结果(2=失败,3=成功)
	 * reMarks 处理备注
	 * */
	@ConsoleCommand(name = "gm_order_order_handle_complete", description = "订单处理完成")
	public Object gm_order_order_handle_complete(long orderId, int transPlayer, int result, String reMarks){
		int err = OrderDefine.ORDER_ERROR_UNEXIST;
		orderManager.lock();
		try{
			while(true){
				if(result < OrderManager.STATE_ORDER_FAIL || result > OrderManager.STATE_ORDER_SUCESS){
					err = OrderDefine.ORDER_ERROR_PARAM;
					break;
				}
				
				SellOrderEntity sellOrder = orderManager.findSellOrder_orderId(orderId);
				if(null == sellOrder){
					break;
				}
				AccountEntity accountEntity = accountManager.findOf_starNO(sellOrder.getStarNO());
				if(null == accountEntity){
					err = AccountDefine.ACCOUNT_ERROR_UN_EXIST;
					break;
				}
				
				if(sellOrder.getState() != OrderManager.STATE_ORDER_TRANSING){
					err = OrderDefine.ORDER_ERROR_PARAM;
					break;
				}				
				
				if(sellOrder.getTransPlayer() != transPlayer){
					err = OrderDefine.ORDER_ERROR_TRANSPLAYER;
					break;
				}				
				
				sellOrder.setState(result);
				sellOrder.setReMarks(reMarks);
				
				if(result == 2){
					//处理失败返回玩家金币
					walletManager.addRoomCard(accountEntity.getId(), sellOrder.getGoldMoney(), false);
				}
				
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
		return Result.valueOfSuccess();
	}
	
	//充值订单测试
	@ConsoleCommand(name = "gm_order_pay_test", description = "充值订单测试")
	public Object gm_order_pay_test(long accountId, int rmb, int payType){
		return orderService.order_charge_rmb2goldmoney(accountId, rmb, payType);
	}
	
}
