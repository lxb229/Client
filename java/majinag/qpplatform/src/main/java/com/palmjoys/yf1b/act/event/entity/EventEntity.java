package com.palmjoys.yf1b.act.event.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

/**
 * 上报失败的事件
 * */
@Entity
@Memcached
@NamedQueries({
	@NamedQuery(name=EventEntity.NQ_EVENTENTITY_MAXUID, query="SELECT max(A.id) from EventEntity AS A")
})
public class EventEntity implements IEntity<Long>, Lifecycle{
	public static final String NQ_EVENTENTITY_MAXUID = "nq_evententity_maxuid";
	
	@Id
	private Long id;
	//事件Id
	@Column(nullable = false)
	private int eventId;
	//事件创建时间
	@Column(nullable = false)
	private long createTime;
	//事件参数Json
	@Lob
	@Column(nullable = false)
	private String paramsListJson;
	
	//事件参数列表
	@Transient
	private List<Object> paramsList;
	
	public static EventEntity valueOf(Long id){
		EventEntity retEntity = new EventEntity();
		retEntity.id = id;
		retEntity.eventId = 0;
		retEntity.createTime = 0;
		retEntity.paramsListJson = null;
		retEntity.paramsList = new ArrayList<Object>();
		return retEntity;
	}	
		
	public int getEventId() {
		return eventId;
	}

	@Enhance
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public long getCreateTime() {
		return createTime;
	}

	@Enhance
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public List<Object> getParamsList() {
		return paramsList;
	}

	@Enhance
	public void setParamsList(List<Object> paramsList) {
		this.paramsList = paramsList;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(paramsListJson)){
			paramsList = new ArrayList<Object>();
		}else{
			paramsList = JsonUtils.string2Collection(paramsListJson, List.class, Object.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		paramsListJson = JsonUtils.object2String(paramsList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

}
