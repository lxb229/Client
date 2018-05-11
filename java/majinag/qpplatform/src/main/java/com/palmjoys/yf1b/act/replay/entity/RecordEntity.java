package com.palmjoys.yf1b.act.replay.entity;

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

import com.palmjoys.yf1b.act.replay.model.RecordScoreAttrib;

//战绩信息
@Entity
@Memcached
public class RecordEntity implements IEntity<Long>, Lifecycle{
	//记录Id
	@Id
	private long recordId;
	//记录所属桌子号
	@Column(nullable = false)
	private int tableId;
	//记录时间
	@Column(nullable = false)
	private long recordTime;
	//所属麻将馆("0"=无麻将馆)
	@Column(nullable = false)
	private String corpsId;
	//座位玩家列表Json
	@Lob
	@Column(nullable = false)
	private String seatPlayerMapJson;
	
	//战绩详细数据JSON
	@Lob
	@Column(nullable = false)
	private String recordMapJson;
	//座位玩家列表
	@Transient
	private Map<Long, Long> seatPlayerMap;
	//战绩详细数据MAP(KEY=局数,VALUE=详细数据)
	@Transient
	private Map<Integer, RecordScoreAttrib> recordMap;
	
	public static RecordEntity valueOf(long recordId, Integer tableId){
		RecordEntity retEntity = new RecordEntity();
		retEntity.recordId = recordId;
		retEntity.tableId = tableId;
		retEntity.recordTime = 0;
		retEntity.corpsId = "0";
		retEntity.seatPlayerMapJson = null;
		retEntity.seatPlayerMap = new HashMap<>();
		retEntity.recordMapJson = null;
		retEntity.recordMap = new HashMap<>();
		return retEntity;
	}
	
	@Override
	public Long getId() {
		return recordId;
	}
	
	public int getTableId() {
		return tableId;
	}
	
	@Enhance
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public long getRecordTime() {
		return recordTime;
	}
	
	@Enhance
	public void setRecordTime(long recordTime) {
		this.recordTime = recordTime;
	}
	
	public String getCorpsId() {
		return corpsId;
	}

	@Enhance
	public void setCorpsId(String corpsId) {
		this.corpsId = corpsId;
	}		

	public Map<Integer, RecordScoreAttrib> getRecordMap() {
		return recordMap;
	}

	@Enhance
	public void setRecordMap(Map<Integer, RecordScoreAttrib> recordMap) {
		this.recordMap = recordMap;
	}
	
	public Map<Long, Long> getSeatPlayerMap() {
		return seatPlayerMap;
	}

	@Enhance
	public void setSeatPlayerMap(Map<Long, Long> seatPlayerMap) {
		this.seatPlayerMap = seatPlayerMap;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(seatPlayerMapJson)){
			seatPlayerMap = new HashMap<>();
		}else{
			seatPlayerMap = JsonUtils.string2Map(seatPlayerMapJson, Long.class, Long.class);
		}
		
		if(StringUtils.isBlank(recordMapJson)){
			recordMap = new HashMap<>();
		}else{
			recordMap = JsonUtils.string2Map(recordMapJson, Integer.class, RecordScoreAttrib.class);
		}
	}
	
	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		seatPlayerMapJson = JsonUtils.map2String(seatPlayerMap);
		recordMapJson = JsonUtils.map2String(recordMap);
		return false;
	}
	
	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}	
}
