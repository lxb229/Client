package com.palmjoys.yf1b.act.dzpker.entity;

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
public class DzpkerTableIdEntity implements IEntity<Integer>, Lifecycle{
	@Id
	private int id;
	
	@Column(nullable = false)
	private int idIndex;
	
	@Lob
	@Column(nullable = false)
	private String idListJson;

	@Transient
	private List<Integer> idList;
	
	public static DzpkerTableIdEntity valueOf(int id){
		DzpkerTableIdEntity retEntity = new DzpkerTableIdEntity();
		retEntity.id = id;
		retEntity.idIndex = 0;
		retEntity.idListJson = null;
		retEntity.idList = new ArrayList<>();
		
		return retEntity;
	}

	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(idListJson)){
			idList = new ArrayList<>();
		}else{
			idList = JsonUtils.string2Collection(idListJson, List.class, Integer.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		idListJson = JsonUtils.object2String(idList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

	public int getIdIndex() {
		return idIndex;
	}

	@Enhance
	public void setIdIndex(int idIndex) {
		this.idIndex = idIndex;
	}

	public List<Integer> getIdList() {
		return idList;
	}

	@Enhance
	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}
	
}
