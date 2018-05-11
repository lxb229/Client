package com.palmjoys.yf1b.act.framework.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

@Entity
@Memcached
public class AccountLuckyEntity implements IEntity<Long>{
	@Id
	private long accountId;
	//幸运值(百分比)
	@Column(nullable = false)
	private int luck;
	//幸运到期时间(毫秒时间)
	@Column(nullable = false)
	private long luckStartTime;
	//幸运到期时间(毫秒时间)
	@Column(nullable = false)
	private long luckEndTime;
	//幸运总次数
	@Column(nullable = false)
	private int luckTotalNum;
	//已幸运次数
	@Column(nullable = false)
	private int luckUsedNum;

	public static AccountLuckyEntity valueOf(long accountId){
		AccountLuckyEntity luckEntity = new AccountLuckyEntity();
		luckEntity.accountId = accountId;
		luckEntity.luck = 0;
		luckEntity.luckStartTime = 0;
		luckEntity.luckEndTime = 0;
		luckEntity.luckTotalNum = 0;
		luckEntity.luckUsedNum = 0;
		
		return luckEntity;
	}
	
	@Override
	public Long getId() {
		return accountId;
	}

	public long getAccountId() {
		return accountId;
	}

	@Enhance
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public int getLuck() {
		return luck;
	}

	@Enhance
	public void setLuck(int luck) {
		this.luck = luck;
	}	

	public long getLuckStartTime() {
		return luckStartTime;
	}

	@Enhance
	public void setLuckStartTime(long luckStartTime) {
		this.luckStartTime = luckStartTime;
	}

	public long getLuckEndTime() {
		return luckEndTime;
	}

	@Enhance
	public void setLuckEndTime(long luckEndTime) {
		this.luckEndTime = luckEndTime;
	}

	public int getLuckTotalNum() {
		return luckTotalNum;
	}

	@Enhance
	public void setLuckTotalNum(int luckTotalNum) {
		this.luckTotalNum = luckTotalNum;
	}

	public int getLuckUsedNum() {
		return luckUsedNum;
	}

	@Enhance
	public void setLuckUsedNum(int luckUsedNum) {
		this.luckUsedNum = luckUsedNum;
	}
	
	
	
}
