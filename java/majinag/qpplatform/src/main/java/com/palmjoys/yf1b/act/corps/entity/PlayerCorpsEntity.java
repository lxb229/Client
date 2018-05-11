package com.palmjoys.yf1b.act.corps.entity;

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

import com.palmjoys.yf1b.act.corps.model.CorpsMemberAttrib;

@Entity
@Memcached
public class PlayerCorpsEntity implements IEntity<Long>, Lifecycle{
	@Id
	private long accountId;
	//所加入的帮会列表Json
	@Lob
	@Column(nullable = false)
	private String joinedCorpsListJson;
	
	//所加入的帮会记录数据列表Json
	@Lob
	@Column(nullable = false)
	private String corpsDataListJson;
	
	//所加入的帮会列表(KEY=帮会Id)
	@Transient
	private Map<String, String> joinedCorpsList;
	
	//所加入的帮会记录数据列表(KEY=帮会Id)
	@Transient
	private Map<String, CorpsMemberAttrib> corpsDataList;
	
	

	public static PlayerCorpsEntity valueOf(long accountId){
		PlayerCorpsEntity entity = new PlayerCorpsEntity();
		entity.accountId = accountId;
		entity.corpsDataListJson = null;
		entity.corpsDataList = new HashMap<>();
		entity.joinedCorpsListJson = null;
		entity.joinedCorpsList = new HashMap<>();
		
		return entity;
	}
	
	public long getAccountId() {
		return accountId;
	}

	@Enhance
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}	

	public Map<String, String> getJoinedCorpsList() {
		return joinedCorpsList;
	}

	@Enhance
	public void setJoinedCorpsList(Map<String, String> joinedCorpsList) {
		this.joinedCorpsList = joinedCorpsList;
	}

	public Map<String, CorpsMemberAttrib> getCorpsDataList() {
		return corpsDataList;
	}

	@Enhance
	public void setCorpsDataList(Map<String, CorpsMemberAttrib> corpsDataList) {
		this.corpsDataList = corpsDataList;
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
		if(StringUtils.isBlank(corpsDataListJson)){
			corpsDataList = new HashMap<>();
		}else{
			corpsDataList = JsonUtils.string2Map(corpsDataListJson, String.class, CorpsMemberAttrib.class);
		}
		
		if(StringUtils.isBlank(this.joinedCorpsListJson)){
			joinedCorpsList = new HashMap<>();
		}else{
			joinedCorpsList = JsonUtils.string2Map(joinedCorpsListJson, String.class, String.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		corpsDataListJson = JsonUtils.map2String(corpsDataList);
		joinedCorpsListJson = JsonUtils.map2String(joinedCorpsList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

}
