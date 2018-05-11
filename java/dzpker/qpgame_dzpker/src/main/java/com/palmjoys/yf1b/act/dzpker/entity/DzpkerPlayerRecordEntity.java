package com.palmjoys.yf1b.act.dzpker.entity;

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

@Entity
@Memcached
public class DzpkerPlayerRecordEntity implements IEntity<Long>, Lifecycle{
	//帐号Id
	@Id
	private long accountId;
	//参与过游戏的桌子列表Json
	@Lob
	@Column(nullable = false)
	private String recordTableMapJson;
	
	//参与过游戏的桌子列表(KEY=桌子记录数据库Id, VALUE=桌子记录数据库Id)
	@Transient
	private Map<Long, Long> recordTableMap; 
	

	public static DzpkerPlayerRecordEntity valueOf(long accountId){
		DzpkerPlayerRecordEntity retEntity = new DzpkerPlayerRecordEntity();
		retEntity.accountId = accountId;
		retEntity.recordTableMapJson = null;
		retEntity.recordTableMap = new HashMap<>();
		return retEntity;
	}

	@Override
	public Long getId() {
		return accountId;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(recordTableMapJson)){
			recordTableMap = new HashMap<>();
		}else{
			recordTableMap = JsonUtils.string2Map(recordTableMapJson, Long.class, Long.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		recordTableMapJson = JsonUtils.map2String(recordTableMap);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

	public Map<Long, Long> getRecordTableMap() {
		return recordTableMap;
	}

	@Enhance
	public void setRecordTableMap(Map<Long, Long> recordTableMap) {
		this.recordTableMap = recordTableMap;
	}
	
	
}
