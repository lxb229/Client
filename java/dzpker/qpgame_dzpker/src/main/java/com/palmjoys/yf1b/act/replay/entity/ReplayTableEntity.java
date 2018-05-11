package com.palmjoys.yf1b.act.replay.entity;

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

import com.palmjoys.yf1b.act.replay.model.RecordScoreAttrib;

//战绩信息
@Entity
@Memcached
public class ReplayTableEntity implements IEntity<Integer>, Lifecycle{
	//记录所属桌子号
	@Id
	private int tableId;
	//记录时间
	@Column(nullable = false)
	private long recordTime;
	//所属俱乐部("0"=无俱乐部)
	@Column(nullable = false)
	private String corpsId;
	//本桌子游戏玩家JSON
	@Lob
	@Column(nullable = false)
	private String tablePlayerJson;
	//战绩详细数据JSON
	@Lob
	@Column(nullable = false)
	private String recordMapJson;
	
	//游戏桌子玩家列表
	@Transient
	private List<Long> tablePlayerList;
	//战绩详细数据MAP(KEY=局数,VALUE=详细数据)
	@Transient
	private Map<Integer, RecordScoreAttrib> recordMap;
	
	public static ReplayTableEntity valueOf(Integer tableId){
		ReplayTableEntity retEntity = new ReplayTableEntity();
		retEntity.tableId = tableId;
		retEntity.recordTime = 0;
		retEntity.corpsId = "0";
		retEntity.tablePlayerJson = null;
		retEntity.tablePlayerList = new ArrayList<>();
		
		retEntity.recordMapJson = null;
		retEntity.recordMap = new HashMap<>();
		return retEntity;
	}
	
	@Override
	public Integer getId() {
		return tableId;
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
	
	public String getCorpsId() {
		return corpsId;
	}

	@Enhance
	public void setCorpsId(String corpsId) {
		this.corpsId = corpsId;
	}

	@Enhance
	public void setRecordTime(long recordTime) {
		this.recordTime = recordTime;
	}	

	public Map<Integer, RecordScoreAttrib> getRecordMap() {
		return recordMap;
	}

	@Enhance
	public void setRecordMap(Map<Integer, RecordScoreAttrib> recordMap) {
		this.recordMap = recordMap;
	}

	public List<Long> getTablePlayerList() {
		return tablePlayerList;
	}

	@Enhance
	public void setTablePlayerList(List<Long> tablePlayerList) {
		this.tablePlayerList = tablePlayerList;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(tablePlayerJson)){
			tablePlayerList = new ArrayList<>();
		}else{
			tablePlayerList = JsonUtils.string2Collection(tablePlayerJson, List.class, Long.class);
		}
		
		if(StringUtils.isBlank(recordMapJson)){
			recordMap = new HashMap<>();
		}else{
			recordMap = JsonUtils.string2Map(recordMapJson, Integer.class, RecordScoreAttrib.class);
		}
	}
	
	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		tablePlayerJson = JsonUtils.object2String(tablePlayerList);
		recordMapJson = JsonUtils.map2String(recordMap);
		return false;
	}
	
	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}
	
	

	
}
