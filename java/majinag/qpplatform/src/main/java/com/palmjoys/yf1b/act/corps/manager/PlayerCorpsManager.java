package com.palmjoys.yf1b.act.corps.manager;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.corps.entity.PlayerCorpsEntity;

@Component
public class PlayerCorpsManager {
	@Inject
	private EntityMemcache<Long, PlayerCorpsEntity> playerCorpsCache;
	
	public PlayerCorpsEntity loadOrCreate(long accountId){
		return playerCorpsCache.loadOrCreate(accountId, new EntityBuilder<Long, PlayerCorpsEntity>(){
			@Override
			public PlayerCorpsEntity createInstance(Long pk) {
				return PlayerCorpsEntity.valueOf(accountId);
			}
		});
	}
	
}
