package com.palmjoys.yf1b.act.cooltime.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

/*角色冷却重置时间存取类*/
@Entity
@Memcached
public class PlayerCoolTimeEntity implements IEntity<Long>, Lifecycle
{

	/*玩家id*/
	@Id
	private Long playerId;
	
	/*重置时间信息Json*/
	@Lob
	@Column(nullable = false)
	private String resetTimeJson;
	
	/*重置时间信息*/
	@Transient
	private Map<Integer, Long>resetTimeMap;
	
	
	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}


	protected PlayerCoolTimeEntity(){
		
	}
	
	public static PlayerCoolTimeEntity valueOf(Long playerId){
		PlayerCoolTimeEntity entity = new PlayerCoolTimeEntity();
		entity.playerId = playerId;
		entity.resetTimeJson = null;
		entity.resetTimeMap = new HashMap<Integer, Long>();
		
		return entity;
	}

	@Override
	public Long getId() {
		return playerId;
	}
	
	/**根据重置功能点编号获取上次重置时间,编号见PlayerCoolTimeConfig.xlsx表*/
	public long getRestTime(int resetId){
		
		Long retTime = resetTimeMap.get(resetId);
		if(retTime == null)
			return 0;
		
		
		return retTime.longValue();
	}
	
	/**根据重置功能点编号设置重置时间,编号见PlayerCoolTimeConfig.xlsx表*/
	@Enhance
	public void setResetTime(int resetId, long resetTime){
		resetTimeMap.put(resetId, resetTime);
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1)
	{
		if(StringUtils.isBlank(resetTimeJson)){
			resetTimeMap = new HashMap<Integer, Long>();
		}else{
			resetTimeMap = JsonUtils.string2Map(resetTimeJson, Integer.class, Long.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException
	{
		resetTimeJson = JsonUtils.map2String(resetTimeMap);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

	public String getResetTimeJson() {
		return resetTimeJson;
	}

	@Enhance
	public void setResetTimeJson(String resetTimeJson) {
		this.resetTimeJson = resetTimeJson;
	}

	public Map<Integer, Long> getResetTimeMap() {
		return resetTimeMap;
	}

	@Enhance
	public void setResetTimeMap(Map<Integer, Long> resetTimeMap) {
		this.resetTimeMap = resetTimeMap;
	}
}
