package com.palmjoys.yf1b.act.majiang.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.palmjoys.yf1b.act.majiang.model.PlayerCreatedRuleAttrib;

/**
 * 创建桌子选择的规则
 * */
@Entity
@Memcached
public class PlayerGamedRecordEntity implements IEntity<Long>, Lifecycle{
	@Id
	private long accountId;
	//创建过的规则列表Json
	@Lob
	@Column(nullable = false)
	private String ruleMapJson;
	
	//参与过的游戏记录列表Json
	@Lob
	@Column(nullable = false)
	private String gamedRecordListJson;
	
	//创建过的规则列表(KEY=条目Id,VALUE=创建选择过的规则列表)
	@Transient
	private Map<Integer, PlayerCreatedRuleAttrib> ruleMap;
	
	//参与过的游戏记录列表
	@Transient
	private List<Long> gamedRecordList;

	public static PlayerGamedRecordEntity valueOf(Long accountId){
		PlayerGamedRecordEntity retEntity = new PlayerGamedRecordEntity();
		retEntity.accountId = accountId;
		retEntity.ruleMapJson = null;
		retEntity.ruleMap = new HashMap<>();
		retEntity.gamedRecordListJson = null;
		retEntity.gamedRecordList = new ArrayList<>();
		
		return retEntity;
	}

	public Map<Integer, PlayerCreatedRuleAttrib> getRuleMap() {
		return ruleMap;
	}

	@Enhance
	public void setRuleMap(Map<Integer, PlayerCreatedRuleAttrib> ruleMap) {
		this.ruleMap = ruleMap;
	}

	public List<Long> getGamedRecordList() {
		return gamedRecordList;
	}

	@Enhance
	public void setGamedRecordList(List<Long> gamedRecordList) {
		this.gamedRecordList = gamedRecordList;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(ruleMapJson)){
			this.ruleMap = new HashMap<>();
		}else{
			this.ruleMap = JsonUtils.string2Map(ruleMapJson, Integer.class, PlayerCreatedRuleAttrib.class);
		}
		
		if(StringUtils.isBlank(gamedRecordListJson)){
			gamedRecordList = new ArrayList<>();
		}else{
			gamedRecordList = JsonUtils.string2Collection(gamedRecordListJson, List.class, Long.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		this.ruleMapJson = JsonUtils.map2String(this.ruleMap);
		this.gamedRecordListJson = JsonUtils.object2String(gamedRecordList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

	@Override
	public Long getId() {
		return accountId;
	}
}
