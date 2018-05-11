package com.palmjoys.yf1b.act.mall.manager;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.mall.entity.MallRateEntity;

@Component
public class MallRateManager {
	@Inject
	private EntityMemcache<Integer, MallRateEntity> mallRateEntityCache;

	public MallRateEntity loadOrCreate(){
		int cfgId = 1;
		return mallRateEntityCache.loadOrCreate(cfgId, new EntityBuilder<Integer, MallRateEntity>(){

			@Override
			public MallRateEntity createInstance(Integer pk) {
				return MallRateEntity.valueOf(cfgId);
			}
		});
	}
	
	
}
