package com.palmjoys.yf1b.act.corps.entity;

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
public class CorpsConfigEntity implements IEntity<Integer>, Lifecycle{
	@Id
	private Integer cfgId;
	//创建帮会房卡数限制
	@Column(nullable = false)
	private int roomCardLimit;
	//帮会最大创建数
	@Column(nullable = false)
	private int createMax;
	//帮会成员最大数
	@Column(nullable = false)
	private int maxMemberNum;
	//帮会加入申请最大数
	@Column(nullable = false)
	private int maxJoinQuestNum;
	//推荐的帮会列表Json
	@Lob
	@Column(nullable = false)
	private String recommendCorpsListJson;
	
	//推荐的帮会列表
	@Transient
	private List<String> recommendCorpsList;
	
	public static CorpsConfigEntity valueOf(int cfgId){
		CorpsConfigEntity retEntity = new CorpsConfigEntity();
		retEntity.cfgId = cfgId;
		retEntity.roomCardLimit = 1000;
		retEntity.createMax = 1;
		retEntity.maxMemberNum = 200;
		retEntity.maxJoinQuestNum = 1000;
		retEntity.recommendCorpsListJson = null;
		retEntity.recommendCorpsList = new ArrayList<>();
		
		return retEntity;
	}
	
	
	@Override
	public Integer getId() {
		return cfgId;
	}

	public int getRoomCardLimit() {
		return roomCardLimit;
	}

	@Enhance
	public void setRoomCardLimit(int roomCardLimit) {
		this.roomCardLimit = roomCardLimit;
	}

	public int getCreateMax() {
		return createMax;
	}

	@Enhance
	public void setCreateMax(int createMax) {
		this.createMax = createMax;
	}

	public int getMaxMemberNum() {
		return maxMemberNum;
	}

	@Enhance
	public void setMaxMemberNum(int maxMemberNum) {
		this.maxMemberNum = maxMemberNum;
	}

	public int getMaxJoinQuestNum() {
		return maxJoinQuestNum;
	}

	@Enhance
	public void setMaxJoinQuestNum(int maxJoinQuestNum) {
		this.maxJoinQuestNum = maxJoinQuestNum;
	}

	public List<String> getRecommendCorpsList() {
		return recommendCorpsList;
	}

	@Enhance
	public void setRecommendCorpsList(List<String> recommendCorpsList) {
		this.recommendCorpsList = recommendCorpsList;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(recommendCorpsListJson)){
			recommendCorpsList = new ArrayList<>();
		}else{
			recommendCorpsList = JsonUtils.string2Collection(recommendCorpsListJson, List.class, String.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		recommendCorpsListJson = JsonUtils.object2String(recommendCorpsList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}
}
