package com.palmjoys.yf1b.act.wallet.entity;

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

import com.palmjoys.yf1b.act.wallet.model.RoomCardGiveAttrib;

//房卡赠送记录数据
@Entity
@Memcached
public class GiveEntity implements IEntity<Long>, Lifecycle{
	//赠予玩家
	@Id
	private long accountId;
	@Lob
	@Column(nullable = false)
	private String giveListJson;
	
	@Transient
	private List<RoomCardGiveAttrib> giveList;
		
	public static GiveEntity valueOf(long accountId){
		GiveEntity retEntity = new GiveEntity();
		retEntity.accountId = accountId;
		retEntity.giveListJson = null;
		retEntity.giveList = new ArrayList<>();
		
		return retEntity;
	}
	
	@Override
	public Long getId() {
		return accountId;
	}
		
	public List<RoomCardGiveAttrib> getGiveList() {
		return giveList;
	}

	@Enhance
	public void setGiveList(List<RoomCardGiveAttrib> giveList) {
		this.giveList = giveList;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(giveListJson)){
			this.giveList = new ArrayList<>();
		}else{
			this.giveList = JsonUtils.string2Collection(giveListJson, List.class, RoomCardGiveAttrib.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		giveListJson = JsonUtils.object2String(this.giveList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}
}
