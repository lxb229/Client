package com.palmjoys.yf1b.act.framework.account.manager;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.framework.account.entity.LoginEntity;

@Component
public class LoginManager {
	@Inject
	private EntityMemcache<Long, LoginEntity> loginCache;

	public LoginEntity loadOrCreate(long accountId){
		return loginCache.loadOrCreate(accountId, new EntityBuilder<Long, LoginEntity>(){
			@Override
			public LoginEntity createInstance(Long pk) {
				return LoginEntity.valueOf(accountId);
			}
		});
	}
	
	public LoginEntity load(long accountId){
		return loginCache.load(accountId);
	}
	
	public void clearOfCache(long accountId){
		loginCache.clear(accountId);
	}
}
