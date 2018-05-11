package com.palmjoys.yf1b.act.activity.entity;

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

import com.palmjoys.yf1b.act.activity.model.ActivityItemAttrib;

@Entity
@Memcached
public class ActivityEntity implements IEntity<Integer>, Lifecycle{
	@Id
	private int cfgId;

	//活动URL展示地址
	@Lob
	@Column(nullable = false)
	private String activityListJson;
	
	@Transient
	private List<ActivityItemAttrib> activityList;

	public static ActivityEntity valueOf(int cfgId){
		ActivityEntity retEntity = new ActivityEntity();
		retEntity.cfgId = cfgId;
		retEntity.activityListJson = null;
		retEntity.activityList = new ArrayList<>();
		return retEntity;
	}
	
	
	@Override
	public Integer getId() {
		return cfgId;
	}

	public List<ActivityItemAttrib> getActivityList() {
		return activityList;
	}

	@Enhance
	public void setActivityList(List<ActivityItemAttrib> activityList) {
		this.activityList = activityList;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(activityListJson)){
			activityList = new ArrayList<>();
		}else{
			activityList = JsonUtils.string2Collection(activityListJson, List.class, ActivityItemAttrib.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		activityListJson = JsonUtils.object2String(activityList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

}
