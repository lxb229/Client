package com.palmjoys.yf1b.act.cooltime.manger;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.cooltime.entity.PlayerCoolTimeEntity;
import com.palmjoys.yf1b.act.cooltime.entity.SysCoolTimeEntity;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeCondition;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;
import com.palmjoys.yf1b.act.cooltime.resource.PlayerCoolTimeConfig;
import com.palmjoys.yf1b.act.cooltime.resource.SysCoolTimeConfig;
import com.palmjoys.yf1b.act.cooltime.service.ICoolTimeResetService;

@Component
public class CheckResetTimeManager implements ApplicationContextAware{
	
	@Autowired
	private PlayerCoolTimeManager playerCoolTimeManager;
	@Autowired
	private  SysCoolTimeManager  sysCoolTimeManager;
	@Static
	private Storage<Integer, PlayerCoolTimeConfig> playerCoolTimeConfigs;
	@Static
	private Storage<Integer, SysCoolTimeConfig> sysCoolTimeConfigs;
	
	
	private ApplicationContext applicationContext;
	private Map<Integer, ICoolTimeResetService> serviceList = new HashMap<Integer, ICoolTimeResetService>();
	@Override
	public void setApplicationContext(ApplicationContext context)throws BeansException 
	{
		applicationContext = context;
	}

	@PostConstruct
	private void init(){
		for (ICoolTimeResetService service : applicationContext.getBeansOfType(ICoolTimeResetService.class).values()){
			if (serviceList.containsKey(service.getServiceId()) == false){
			     serviceList.put(service.getServiceId(), service);
			}
		}
	}
	
	public long getFristResetTime(CoolTimeCondition condition){
		ICoolTimeResetService service = serviceList.get(condition.type);
		long retTime = service.getFristResetTime(condition);
		return retTime;
	}
	
	public CoolTimeResult checkResetTime(long nextRestTime, CoolTimeCondition condition){
		ICoolTimeResetService service = serviceList.get(condition.type);
		return service.resetTime(nextRestTime, condition);
	}
	
	/**角色冷却时间重置检测**/
	public CoolTimeResult checkPlayerResetTime(Long playerId, int resetId, boolean bResetTime){
		
		PlayerCoolTimeConfig cfg = playerCoolTimeConfigs.get(resetId, false);
		if(cfg == null)
			return new CoolTimeResult();
		
		PlayerCoolTimeEntity coolTimeEntity = playerCoolTimeManager.loadOrCreate(playerId);
		long nextTime = coolTimeEntity.getRestTime(resetId);
		CoolTimeResult timeResult = checkResetTime(nextTime, cfg.checkReset[0]);
		if(bResetTime && nextTime != timeResult.nextTime){
			coolTimeEntity.setResetTime(resetId, timeResult.nextTime);
		}
		
		return timeResult;
	}
	
	/**系统冷却时间重置检测**/
	public CoolTimeResult  checkSysResetTime( int resetId, boolean bResetTime){
		SysCoolTimeConfig cfg = sysCoolTimeConfigs.get(resetId, false);
		 if(cfg == null)
			return new CoolTimeResult();
		SysCoolTimeEntity coolTimeEntity = sysCoolTimeManager.loadOrCreate();
		long nextTime = coolTimeEntity.getRestTime(resetId);
		CoolTimeResult timeResult = checkResetTime(nextTime, cfg.checkReset[0]);
		if(bResetTime && nextTime != timeResult.nextTime){
			coolTimeEntity.setResetTime(resetId, timeResult.nextTime);
		}
		return timeResult;
	}	
}
