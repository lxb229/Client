package com.palmjoys.yf1b.act.corps.manager;

import java.util.List;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.corps.entity.CorpsConfigEntity;

@Component
public class CorpsCfgManager {
	@Inject
	private EntityMemcache<Integer, CorpsConfigEntity> corpsCfgCache;
	
	public CorpsConfigEntity loadOrCreate(){
		int id = 1;
		return corpsCfgCache.loadOrCreate(id, new EntityBuilder<Integer, CorpsConfigEntity>(){

			@Override
			public CorpsConfigEntity createInstance(Integer pk) {
				return CorpsConfigEntity.valueOf(id);
			}
		});
	}
	
	public int getCreateNeedCard(){
		CorpsConfigEntity entity = this.loadOrCreate();
		return entity.getRoomCardLimit();
	}
	
	public int getMaxCreateNum(){
		CorpsConfigEntity entity = this.loadOrCreate();
		return entity.getCreateMax();
	}
	
	public int getMaxMemberNum(){
		CorpsConfigEntity entity = this.loadOrCreate();
		return entity.getMaxMemberNum();
	}
	
	public int getMaxQuestJionNum(){
		CorpsConfigEntity entity = this.loadOrCreate();
		return entity.getMaxJoinQuestNum();
	}
	
	public List<String> getRecommendCorpsList(){
		CorpsConfigEntity entity = this.loadOrCreate();
		return entity.getRecommendCorpsList();
	}
}
