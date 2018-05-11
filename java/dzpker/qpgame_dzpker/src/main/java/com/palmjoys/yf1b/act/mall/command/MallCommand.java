package com.palmjoys.yf1b.act.mall.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.mall.entity.MallBuyOrderEntity;
import com.palmjoys.yf1b.act.mall.entity.MallRateEntity;
import com.palmjoys.yf1b.act.mall.manager.MallOrderManager;
import com.palmjoys.yf1b.act.mall.manager.MallRateManager;
import com.palmjoys.yf1b.act.mall.model.MallBuyOrderVo;
import com.palmjoys.yf1b.act.mall.model.MallRateCfgVo;

@Component
@ConsoleBean
public class MallCommand {
	@Autowired
	private MallRateManager mallRateManager;
	@Autowired
	private Querier querier;
	@Autowired
	private MallOrderManager mallOrderManager;
	
	
	/**
	 * 获取游戏内货币与人民币比例配置
	 * */
	@ConsoleCommand(name = "gm_mall_get_money_rate", description = "获取游戏内货币与人民币比例配置")
	public Object gm_mall_get_money_rate(){
		MallRateCfgVo retVo = new MallRateCfgVo();
		MallRateEntity mallRateEntity = mallRateManager.loadOrCreate();
		retVo.rmb2GoldMoney = mallRateEntity.getRmb2GoldMoney();
		retVo.goldMoney2Rmb = mallRateEntity.getGoldMoney2Rmb();
		retVo.rmb2Diamond = mallRateEntity.getRmb2Diamond();
		retVo.diamond2Rmb = mallRateEntity.getDiamond2Rmb();
		
		return Result.valueOfSuccess(retVo);
	}
	
	/**
	 * 设置游戏内货币与人民币比例
	 * rmb2GoldMoney 1rmb比多少金币
	 * goldMoney2Rmb 多少金币比1人民币
	 * rmb2Diamond 1人民币比钻石
	 * diamond2Rmb 多少钻石比1人民币
	 * */
	@ConsoleCommand(name = "gm_mall_set_money_rate", description = "设置游戏内货币与人民币比例")
	public Object gm_mall_set_money_rate(int rmb2GoldMoney, int goldMoney2Rmb, int rmb2Diamond, int diamond2Rmb){
		MallRateEntity mallRateEntity = mallRateManager.loadOrCreate();
		mallRateEntity.setRmb2GoldMoney(rmb2GoldMoney);
		mallRateEntity.setGoldMoney2Rmb(goldMoney2Rmb);
		mallRateEntity.setRmb2Diamond(rmb2Diamond);
		mallRateEntity.setDiamond2Rmb(diamond2Rmb);
		
		return Result.valueOfSuccess();
	}
	
	/**
	 * 获取购买订单
	 * start 开始位置
	 * num 查询数量
	 * sortType 排序类型(1=升序,2=降序)
	 * sortCondition 排序条件(1=订单Id,2=人民币,3=房卡,4=金币,5=钻石,6=创建时间,7=订单状态)
	 * match 匹配类型,数组下标位置值>0表示有匹配值(0=订单号,1=明星号,2=订单状态,3=创建时间)
	 * matchParam 匹配参数值(0=订单号,1=明星号,2=订单状态,3=查询开始时间,查询结束时间)
	 * */
	@ConsoleCommand(name = "gm_mall_query_buy_order_list", description = "获取购买订单")
	public Object gm_mall_query_buy_order_list(int start, int num,
			int sortType, int sortCondition, int []match, Object []matchParam){
		if(match.length != 4){
			return Result.valueOfError(-1, "匹配类型参数错误", null);
		}
		
		MallBuyOrderVo retVo = new MallBuyOrderVo();
		if(start < 0){
			start = 0;
		}
		if(num < 0){
			num = 50;
		}
		
		String sortTypeSql = "ASC"; 
		if(sortType == 2){
			sortTypeSql = "DESC";
		}
				
		String cntSql = "";
		String querySql = "";
		String filterSql = "";
		String sortSql = "";
		if(sortCondition == 1){
			//订单Id排序
			sortSql = " ORDER BY A.orderId " + sortTypeSql;
		}else if(sortCondition == 2){
			//人民币状态
			sortSql = " ORDER BY A.rmb " + sortTypeSql;
		}else if(sortCondition == 3){
			//房卡
			sortSql = " ORDER BY A.roomCard " + sortTypeSql;
		}else if(sortCondition == 4){
			//金币
			sortSql = " ORDER BY A.goldMoney " + sortTypeSql;
		}else if(sortCondition == 5){
			//钻石
			sortSql = " ORDER BY A.diamond " + sortTypeSql;
		}else if(sortCondition == 6){
			//创建时间
			sortSql = " ORDER BY A.createTime " + sortTypeSql;
		}else{
			//订单状态
			sortSql = " ORDER BY A.state " + sortTypeSql;
		}
		int paramIndex = 0;
		for(int i=0; i<match.length; i++){
			if(match[i] > 0){
				switch(i){
				case 0://订单号
					filterSql += " AND A.orderId=" + matchParam[paramIndex];
					paramIndex++;
					break;
				case 1://明星号
					filterSql += " AND A.starNO ='" + matchParam[paramIndex] + "'";
					paramIndex++;
					break;
				case 2://订单状态
					filterSql += " AND A.state =" + matchParam[paramIndex] + "";
					paramIndex++;
					break;
				case 3://创建时间
					filterSql += " AND A.createTime>=" + matchParam[paramIndex];
					paramIndex++;
					filterSql += " AND A.createTime<" + matchParam[paramIndex];
					paramIndex++;
					break;
				}
			}
		}
		
		querySql = "SELECT A.orderId FROM MallBuyOrderEntity AS　A"
				+ " WHERE 1=1 " + filterSql;
		cntSql = "SELECT COUNT(A.orderId) FROM MallBuyOrderEntity AS　A"
				+ " WHERE 1=1 " + filterSql;
		querySql += sortSql;
		
		List<Object> retObjects = querier.listBySqlLimit(MallBuyOrderEntity.class, Object.class, cntSql, 0, 1);
		for(Object obj : retObjects){
			if(null != obj){
				if(obj instanceof Long){
					Long tmpObj = (Long) obj;
					retVo.totalNum = tmpObj.intValue();
				}else if(obj instanceof Integer){
					retVo.totalNum = (Integer)obj;
				}
				break;
			}
		}
		
		retObjects = querier.listBySqlLimit(MallBuyOrderEntity.class, Object.class, querySql, start, num);
		for(Object obj : retObjects){
			Long orderId = (Long) obj;
			MallBuyOrderEntity orderEntity = mallOrderManager.loadBuyorder(orderId);
			if(null != orderEntity){
				retVo.addItem(orderEntity.getOrderId(), orderEntity.getStarNO(), 
						orderEntity.getRmb(), orderEntity.getRoomCard(), orderEntity.getGoldMoney(),
						orderEntity.getDiamond(), orderEntity.getCreateTime(), orderEntity.getState());
			}
		}
		
		return Result.valueOfSuccess(retVo);
	}
	
	
	
}
