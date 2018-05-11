package com.palmjoys.yf1b.act.wallet.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

/**
 * 玩家钱包实体类
 * */
@Entity
@Memcached
public class WalletEntity implements IEntity<Long>{	
	//帐号Id
	@Id
	private Long accountId;
	//房卡
	@Column(nullable = false)
	private long roomCard;
	//金币
	@Column(nullable = false)
	private long goldMoney;
	//钻石
	@Column(nullable = false)
	private long diamond;
	
	//钱包数据修改锁
	@Transient
	private Lock _lock = new ReentrantLock();
	
	public static WalletEntity valueOf(long accountId){
		WalletEntity retEntity = new WalletEntity();
		retEntity.accountId = accountId;
		retEntity.roomCard = 2000;
		retEntity.goldMoney = 0;
		retEntity.diamond = 0;
		
		return retEntity;
	}	
	
	public void lock(){
		_lock.lock();
	}
	
	public void unLock(){
		_lock.unlock();
	}
	
	@Override
	public Long getId() {
		return accountId;
	}

	public long getRoomCard() {
		return roomCard;
	}

	@Enhance
	public void setRoomCard(long roomCard) {
		this.roomCard = roomCard;
	}

	public long getGoldMoney() {
		return goldMoney;
	}

	@Enhance
	public void setGoldMoney(long goldMoney) {
		this.goldMoney = goldMoney;
	}

	public long getDiamond() {
		return diamond;
	}

	@Enhance
	public void setDiamond(long diamond) {
		this.diamond = diamond;
	}	
	
}
