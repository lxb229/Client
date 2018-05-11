package com.palmjoys.yf1b.act.framework.account.manager;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.framework.account.entity.AccountLuckyEntity;

@Component
public class AccountLuckyManager {
	@Inject
	private EntityMemcache<Long, AccountLuckyEntity> accountLuckCache;
	
	public AccountLuckyEntity loadOrCreate(long accountId){
		return accountLuckCache.loadOrCreate(accountId, new EntityBuilder<Long, AccountLuckyEntity>(){
			@Override
			public AccountLuckyEntity createInstance(Long pk) {
				return AccountLuckyEntity.valueOf(accountId);
			}
		});
	}
	
	public void clearOfCache(long accountId){
		accountLuckCache.clear(accountId);
	}
}
