package com.palmjoys.yf1b.act.account.entity;

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
public class StarNOEntity implements IEntity<Integer>, Lifecycle{
	@Id
	private Integer id;
	@Column(nullable = false)
	private long idIndex; 
	@Lob
	@Column(nullable = false)
	private String idListJson;
	
	@Transient
	private List<List<String>> idList;
	
	public static StarNOEntity valueOf(int id){
		StarNOEntity retEntity = new StarNOEntity();
		retEntity.id = id;
		retEntity.idIndex = 0;
		retEntity.idListJson = null;
		retEntity.idList = new ArrayList<List<String>>();
		
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
			idList = new ArrayList<List<String>>();
		}else{
			idList = JsonUtils.string2Collection(idListJson, List.class, List.class);
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

	public long getIdIndex() {
		return idIndex;
	}

	@Enhance
	public void setIdIndex(long idIndex) {
		this.idIndex = idIndex;
	}

	public List<List<String>> getIdList() {
		return idList;
	}

	@Enhance
	public void setIdList(List<List<String>> idList) {
		this.idList = idList;
	}
	
	
}
