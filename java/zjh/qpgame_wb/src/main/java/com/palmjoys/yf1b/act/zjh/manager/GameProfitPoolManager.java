package com.palmjoys.yf1b.act.zjh.manager;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.zjh.entity.GameProfitPoolEntity;

@Component
public class GameProfitPoolManager {
	@Inject
	private EntityMemcache<Integer, GameProfitPoolEntity> profitPoolCache;
	
	public GameProfitPoolEntity loadOrCreate(){
		int id = 1;
		return profitPoolCache.loadOrCreate(id, new EntityBuilder<Integer, GameProfitPoolEntity>(){
			@Override
			public GameProfitPoolEntity createInstance(Integer pk) {
				return GameProfitPoolEntity.valueOf(id);
			}
		});
	}
	
	public int getWinRate(int X, int Y){
		int retWinRate = 50;
		GameProfitPoolEntity entity = this.loadOrCreate();
		
		int minRate = entity.getMinWinRate();
		int maxRate = entity.getMaxWinRate();
		long currWinNum = entity.getCurrWinNum();
		long totalWinNum = entity.getTotalWinNum();
		
		if(currWinNum >= totalWinNum){
			retWinRate = minRate;
		}else if(currWinNum <= 0){
			retWinRate = maxRate;
		}else{
			double tmp = (minRate-maxRate)*1.0;
			tmp = tmp/totalWinNum;
			tmp = tmp*currWinNum;
			retWinRate = (int) (tmp + maxRate);
			if(retWinRate >= maxRate){
				retWinRate = maxRate;
			}
			if(retWinRate <= minRate){
				retWinRate = minRate;
			}
		}
		double xRate = entity.getMultitRatePower()*1.0/100;
		double yRate = entity.getBetRatePower()*1.0/100;
		int tmpRate = (int) (((X-1)*xRate + (Y-1)*yRate)*100);
		retWinRate = retWinRate + tmpRate;
		return retWinRate;
	}
	
	public void saveRobotWinMoney(long winMoney){
		GameProfitPoolEntity entity = this.loadOrCreate();
		long currWinNum = entity.getCurrWinNum();
		entity.setCurrWinNum(currWinNum+winMoney);
	}
	
	public long getRobotWinMoney(){
		GameProfitPoolEntity entity = this.loadOrCreate();
		return entity.getCurrWinNum();
	}
	
	public void resetCurrWinMoney(){
		GameProfitPoolEntity entity = this.loadOrCreate();
		entity.setCurrWinNum(0);
	}
}
