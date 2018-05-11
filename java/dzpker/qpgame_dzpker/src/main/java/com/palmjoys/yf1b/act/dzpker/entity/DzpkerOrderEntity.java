package com.palmjoys.yf1b.act.dzpker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

@Entity
@Memcached
public class DzpkerOrderEntity implements IEntity<Long>{
	@Id
	private long recordId;
	//请求购买的玩家Id
	@Column(nullable = false)
	private long accountId;
	//桌子数据库唯 一Id
	@Column(nullable = false)
	private long tableRecordId;
	//桌子创建玩家Id
	@Column(nullable = false)
	private long tableCreatePlayer;
	//申请时间
	@Column(nullable = false)
	private long createTime;
	//处理时间
	@Column(nullable = false)
	private long transTime;
	//购买筹码数
	@Column(nullable = false)
	private long chipNum;
	//状态(0=等待房主处理,1=房主同意,2=房主拒绝)
	@Column(nullable = false)
	private int state;
	
	public static DzpkerOrderEntity valueOf(long recordId, long accountId, long tableRecordId, 
			long tableCreatePlayer, long chipNum){
		DzpkerOrderEntity retEntity = new DzpkerOrderEntity();
		retEntity.recordId = recordId;
		retEntity.accountId = accountId;
		retEntity.tableRecordId = tableRecordId;
		retEntity.tableCreatePlayer = tableCreatePlayer;
		retEntity.createTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		retEntity.transTime = 0;
		retEntity.chipNum = chipNum;
		retEntity.state = 0;
		
		return retEntity;
	}

	@Override
	public Long getId() {
		return recordId;
	}

	public long getRecordId() {
		return recordId;
	}

	@Enhance
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public long getAccountId() {
		return accountId;
	}

	@Enhance
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getTableRecordId() {
		return tableRecordId;
	}

	@Enhance
	public void setTableRecordId(long tableRecordId) {
		this.tableRecordId = tableRecordId;
	}

	public long getTableCreatePlayer() {
		return tableCreatePlayer;
	}

	@Enhance
	public void setTableCreatePlayer(long tableCreatePlayer) {
		this.tableCreatePlayer = tableCreatePlayer;
	}

	public long getCreateTime() {
		return createTime;
	}

	@Enhance
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getTransTime() {
		return transTime;
	}

	@Enhance
	public void setTransTime(long transTime) {
		this.transTime = transTime;
	}

	public long getChipNum() {
		return chipNum;
	}

	@Enhance
	public void setChipNum(long chipNum) {
		this.chipNum = chipNum;
	}

	public int getState() {
		return state;
	}

	@Enhance
	public void setState(int state) {
		this.state = state;
	}	
}
