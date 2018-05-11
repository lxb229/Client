package com.palmjoys.yf1b.act.dzpker.entity;

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

import com.palmjoys.yf1b.act.dzpker.model.StatisticsAttrib;
import com.palmjoys.yf1b.act.dzpker.model.WinScoreAttrib;

@Entity
@Memcached
public class DzpkerTableRecordEntity implements IEntity<Long>, Lifecycle{
	//记录Id
	@Id
	private long recordId;
	//桌子Id
	@Column(nullable = false)
	private int tableId;
	//桌子名称
	@Column(nullable = false)
	private String tableName;
	//桌子创建玩家帐号
	@Column(nullable = false)
	private long createPlayer;
	//每局输赢详细记录Json
	@Lob
	@Column(nullable = false)
	private String detailedScoreMapJson;
	//玩家统计数据Json
	@Lob
	@Column(nullable = false)
	private String statisticsMapJson;
	//保险商总输赢分数
	@Column(nullable = false)
	private long insuranceScore;
	//记录时间
	@Column(nullable = false)
	private long createTime;
	
	//每局输赢详细记录Map(KEY=局数,VALUE=战绩数据)
	@Transient
	private Map<Integer, WinScoreAttrib> detailedScoreMap;
	//玩家统计数据map(KEY=玩家帐号Id,VALUE=统计数据)
	@Transient
	private Map<Long, StatisticsAttrib> statisticsMap;
		
	
	public static DzpkerTableRecordEntity valueOf(long recordId, int tableId, String tableName, long createPlayer, long createTime){
		DzpkerTableRecordEntity retEntity = new DzpkerTableRecordEntity();
		retEntity.recordId = recordId;
		retEntity.tableId = tableId;
		retEntity.tableName = tableName;
		retEntity.createPlayer = createPlayer;
		retEntity.detailedScoreMapJson = null;
		retEntity.detailedScoreMap = new HashMap<>();
		
		retEntity.statisticsMapJson = null;
		retEntity.statisticsMap = new HashMap<>();
		retEntity.insuranceScore = 0;
		retEntity.createTime = createTime;
		
		return retEntity;
	}

	@Override
	public Long getId() {
		return recordId;
	}
	
	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(detailedScoreMapJson)){
			detailedScoreMap = new HashMap<>();
		}else{
			detailedScoreMap = JsonUtils.string2Map(detailedScoreMapJson, Integer.class, WinScoreAttrib.class);
		}
		
		if(StringUtils.isBlank(statisticsMapJson)){
			statisticsMap = new HashMap<>();
		}else{
			statisticsMap = JsonUtils.string2Map(statisticsMapJson, Long.class, StatisticsAttrib.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		detailedScoreMapJson = JsonUtils.map2String(detailedScoreMap);
		statisticsMapJson = JsonUtils.map2String(statisticsMap);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

	public int getTableId() {
		return tableId;
	}

	@Enhance
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}	

	public String getTableName() {
		return tableName;
	}

	@Enhance
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<Integer, WinScoreAttrib> getDetailedScoreMap() {
		return detailedScoreMap;
	}

	@Enhance
	public void setDetailedScoreMap(Map<Integer, WinScoreAttrib> detailedScoreMap) {
		this.detailedScoreMap = detailedScoreMap;
	}	

	public Map<Long, StatisticsAttrib> getStatisticsMap() {
		return statisticsMap;
	}

	@Enhance
	public void setStatisticsMap(Map<Long, StatisticsAttrib> statisticsMap) {
		this.statisticsMap = statisticsMap;
	}

	public long getCreatePlayer() {
		return createPlayer;
	}

	@Enhance
	public void setCreatePlayer(long createPlayer) {
		this.createPlayer = createPlayer;
	}

	public long getInsuranceScore() {
		return insuranceScore;
	}

	@Enhance
	public void setInsuranceScore(long insuranceScore) {
		this.insuranceScore = insuranceScore;
	}

	public long getCreateTime() {
		return createTime;
	}

	@Enhance
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	
}
