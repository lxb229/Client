package com.palmjoys.yf1b.act.majiang.manager;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.majiang.entity.PlayerGamedRecordEntity;
import com.palmjoys.yf1b.act.majiang.model.PlayerCreatedRuleAttrib;

@Component
public class PlayerGamedRecordManager {
	@Inject
	private EntityMemcache<Long, PlayerGamedRecordEntity> createRulesCache;
	
	public PlayerGamedRecordEntity loadOrCreate(long accountId){
		return createRulesCache.loadOrCreate(accountId, new EntityBuilder<Long, PlayerGamedRecordEntity>(){
			@Override
			public PlayerGamedRecordEntity createInstance(Long pk) {
				return PlayerGamedRecordEntity.valueOf(accountId);
			}
		});
	}
	
	public void saveCreateRuleList(long accountId, Map<Integer, PlayerCreatedRuleAttrib> ruleMap){
		PlayerGamedRecordEntity entity = this.loadOrCreate(accountId);
		entity.setRuleMap(ruleMap);
	}
	
	public Map<Integer, PlayerCreatedRuleAttrib> getPlayerCreateRuleList(long accountId){
		PlayerGamedRecordEntity entity = this.loadOrCreate(accountId);
		return entity.getRuleMap();
	}
	
	public List<Long> getPlayerGamedRecordList(long accountId){
		PlayerGamedRecordEntity entity = this.loadOrCreate(accountId);
		return entity.getGamedRecordList();
	}
	
	public void setPlayerGamedRecordList(long accountId, List<Long> playerGamedRecordList){
		PlayerGamedRecordEntity entity = this.loadOrCreate(accountId);
		entity.setGamedRecordList(playerGamedRecordList);
	}
}
