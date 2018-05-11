package com.palmjoys.yf1b.act.mall.manager;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.mall.entity.MallEntity;

@Component
public class MallManager {
	@Inject
	private EntityMemcache<Integer, MallEntity> mallCache;
	
	public MallEntity loadOrCreate(){
		int id = 1;
		return mallCache.loadOrCreate(id, new EntityBuilder<Integer, MallEntity>(){

			@Override
			public MallEntity createInstance(Integer pk) {
				return MallEntity.valueOf(id);
			}
		});
		
	}

}
