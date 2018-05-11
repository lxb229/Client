package com.palmjoys.yf1b.act.replay.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class ReplayPlayerEntity implements IEntity<Long>, Lifecycle{
	@Id
	private long accountId;
	//游戏过的桌子JSON
	@Lob
	@Column(nullable = false)
	private String gamedtablesJson;
	
	//游戏过的桌子列表
	@Transient
	private List<Integer> gamedtablesList;
	
	public static ReplayPlayerEntity valueOf(long accountId){
		ReplayPlayerEntity retEntity = new ReplayPlayerEntity();
		retEntity.accountId = accountId;
		retEntity.gamedtablesJson = null;
		retEntity.gamedtablesList = new ArrayList<>();
		
		return retEntity;
	}
	
	public List<Integer> getGamedtablesList() {
		return gamedtablesList;
	}

	@Enhance
	public void setGamedtablesList(List<Integer> gamedtablesList) {
		this.gamedtablesList = gamedtablesList;
	}

	@Override
	public Long getId() {
		return accountId;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(gamedtablesJson)){
			gamedtablesList = new ArrayList<>();
		}else{
			gamedtablesList = JsonUtils.string2Collection(gamedtablesJson, List.class, Integer.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		gamedtablesJson = JsonUtils.object2String(gamedtablesList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

}
