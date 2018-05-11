package com.palmjoys.yf1b.act.cooltime.manger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.cooltime.entity.PlayerCoolTimeEntity;
import com.palmjoys.yf1b.act.cooltime.resource.PlayerCoolTimeConfig;

/*角色冷却重置时间管理类*/
@Component
public class PlayerCoolTimeManager {

	@Inject
	private EntityMemcache<Long, PlayerCoolTimeEntity> CoolTimeEntityCache;
	@Static
	private Storage<Integer, PlayerCoolTimeConfig> playerCoolTimeConfigs;
	@Autowired
	private CheckResetTimeManager checkResetTimeManager;
	
	public PlayerCoolTimeEntity loadOrCreate(final Long playerId){
		return CoolTimeEntityCache.loadOrCreate(playerId, new EntityBuilder<Long, PlayerCoolTimeEntity>()	    {
			@Override
			public PlayerCoolTimeEntity createInstance(Long pk) {
				PlayerCoolTimeEntity coolEntity = PlayerCoolTimeEntity.valueOf(playerId);
				initCoolTimeEntity(coolEntity);
				return coolEntity;
			}
		});
	}
		
	/*初始化时间状态*/
	public void initCoolTimeEntity(PlayerCoolTimeEntity coolTimeEntity){
		Collection<PlayerCoolTimeConfig> cfgs = playerCoolTimeConfigs.getAll();
		for(PlayerCoolTimeConfig cfg : cfgs){
			long tmpTime = 0;
			if(cfg.checkReset !=null && cfg.checkReset.length>0){
				tmpTime = checkResetTimeManager.getFristResetTime(cfg.checkReset[0]);
			}
			coolTimeEntity.setResetTime(cfg.id, tmpTime);
		}
	}
	
}
