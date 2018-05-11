package com.palmjoys.yf1b.act.activity.manager;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.activity.entity.ActivityEntity;

@Component
public class ActivityManager {
	@Inject
	private EntityMemcache<Integer, ActivityEntity> activityCache;
	
	public ActivityEntity loadOrCreate(){
		int cfgId = 1;
		return activityCache.loadOrCreate(cfgId, new EntityBuilder<Integer, ActivityEntity>(){

			@Override
			public ActivityEntity createInstance(Integer pk) {
				return ActivityEntity.valueOf(cfgId);
			}
		});
	}
}
