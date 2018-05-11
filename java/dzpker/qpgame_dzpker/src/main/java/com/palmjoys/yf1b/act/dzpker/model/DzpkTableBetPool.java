package com.palmjoys.yf1b.act.dzpker.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DzpkTableBetPool {
	// 池子中参与的玩家注额信息(KEY=参与玩家,VALUE=金额)
	public Map<Long, Long> betInfo = new HashMap<Long, Long>();

	//统计总下注金额
	public long countBetMoney() {
		long total = 0;
		Collection<Long> moneys = betInfo.values();
		for (Long money : moneys) {
			total += money.longValue();
		}
		return total;
	}
	
	//增加下注信息
	public void addBetMoney(long accountId, long betMoney){
		Long moneyObj = betInfo.get(accountId);
		if(null == moneyObj){
			betInfo.put(accountId, betMoney);
		}else{
			betMoney = betMoney + moneyObj.longValue();
			betInfo.put(accountId, betMoney);
		}
	}
	
	//清空下注信息
	public void clear(){
		this.betInfo.clear();
	}
	
	//最大的下注金额
	public long getMaxBetMoney(){
		long max = 0;
		Collection<Long> moneys = betInfo.values();
		for(Long money : moneys){
			if(money.longValue() > max){
				max = money;
			}
		}		
		return max;
	}
	
	//获取指定玩家下注总金额
	public long getBetMoneyOfAccountId(long accountId){
		Long moneyObj = betInfo.get(accountId);
		if(null == moneyObj){
			return 0; 
		}		
		return moneyObj.longValue();
	}

}
