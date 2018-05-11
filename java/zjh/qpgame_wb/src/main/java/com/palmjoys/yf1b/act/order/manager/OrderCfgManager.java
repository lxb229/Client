package com.palmjoys.yf1b.act.order.manager;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.order.entity.OrderCfgEntity;
import com.palmjoys.yf1b.act.order.model.OrderCfgVo;

@Component
public class OrderCfgManager {
	@Inject
	private EntityMemcache<Integer, OrderCfgEntity> orderCfgCache;
	
	public OrderCfgEntity loadOrCreate(){
		int id = 1;
		return orderCfgCache.loadOrCreate(id, new EntityBuilder<Integer, OrderCfgEntity>(){
			@Override
			public OrderCfgEntity createInstance(Integer pk) {
				return OrderCfgEntity.valueOf(id);
			}
		});
	}
	
	public OrderCfgVo getChanagePercent(){
		OrderCfgVo retVo = new OrderCfgVo();
		OrderCfgEntity entity = this.loadOrCreate();
		retVo.rmb2goldMoney = entity.getRmb2goldMoney();
		retVo.goldMoney2Rmb = entity.getGoldMoney2Rmb();
		retVo.minRmb = entity.getMinRmb();
		
		if(retVo.rmb2goldMoney == 0){
			retVo.rmb2goldMoney = 100;
		}
		if(retVo.goldMoney2Rmb == 0){
			retVo.goldMoney2Rmb = 120;
		}
		if(retVo.minRmb == 0){
			retVo.minRmb = 1;
		}
		
		return retVo;
	}

}
