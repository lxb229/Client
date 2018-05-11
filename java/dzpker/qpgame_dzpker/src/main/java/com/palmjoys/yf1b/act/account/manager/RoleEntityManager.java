package com.palmjoys.yf1b.act.account.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.account.entity.RoleEntity;

@Component
public class RoleEntityManager {
	@Inject
	private EntityMemcache<String, RoleEntity> roleCache;
	@Autowired
	private Querier querier;
	
	
	public RoleEntity create(String starNO, long accountId, String headImg, int sex, String nick){
		return roleCache.loadOrCreate(starNO, new EntityBuilder<String, RoleEntity>(){
			@Override
			public RoleEntity createInstance(String pk) {
				return RoleEntity.valueOf(starNO, accountId, headImg, sex, nick);
			}
		});
	}
	
	public RoleEntity findOf_starNO(String starNO){
		return roleCache.load(starNO);
	}

	public RoleEntity findOf_accountId(long accountId){
		//先查全表,后查缓存
		String querySql = "SELECT A.starNO FROM RoleEntity AS A WHERE A.accountId=" + accountId;
		List<Object> retObjects = querier.listBySqlLimit(RoleEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjects){
			if(null != obj){
				String starNO = (String) obj;
				return this.findOf_starNO(starNO);
			}
		}
		List<RoleEntity> retEntitys = roleCache.getFinder().find(RoleFilterManager.Instance().create_RoleFilter_AccountId(accountId));
		for(RoleEntity theEntity : retEntitys){
			return theEntity;
		}
				
		return null;
	}
	
}
