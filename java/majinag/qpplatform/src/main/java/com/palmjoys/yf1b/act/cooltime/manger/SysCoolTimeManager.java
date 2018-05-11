package com.palmjoys.yf1b.act.cooltime.manger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.cooltime.entity.SysCoolTimeEntity;
import com.palmjoys.yf1b.act.cooltime.resource.SysCoolTimeConfig;

/*系统冷却重置时间管理类*/
@Component
public class SysCoolTimeManager{
	@Inject
	private EntityMemcache<Long, SysCoolTimeEntity> CoolTimeEntityCache;
	@Static
	private Storage<Integer, SysCoolTimeConfig> sysCoolTimeConfigs;
	@Autowired
	private CheckResetTimeManager checkResetTimeManager;
		
	public SysCoolTimeEntity loadOrCreate(){
		SysCoolTimeEntity coolEntity = CoolTimeEntityCache.loadOrCreate(1L, new EntityBuilder<Long, SysCoolTimeEntity>(){
			@Override
			public SysCoolTimeEntity createInstance(Long pk){
				SysCoolTimeEntity theCoolTimeEntity = SysCoolTimeEntity.valueOf();
				initCoolTimeEntity(theCoolTimeEntity);
				return theCoolTimeEntity;
			}
		});		
		return coolEntity;
	}
		
	/*初始化时间状态*/
	public void initCoolTimeEntity(SysCoolTimeEntity coolTimeEntity){
		Collection<SysCoolTimeConfig> cfgs = sysCoolTimeConfigs.getAll();
		for(SysCoolTimeConfig cfg : cfgs){
			long tmpTime = 0;
			if(cfg.checkReset !=null && cfg.checkReset.length>0){
				tmpTime = checkResetTimeManager.getFristResetTime(cfg.checkReset[0]);
			}
			coolTimeEntity.setResetTime(cfg.id, tmpTime);
		}
	}
	
}
