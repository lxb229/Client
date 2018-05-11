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
	private int roomCard;
	//体验用房卡(只能用于游戏,不参与抽奖等经济系统统计)
	@Column(nullable = false)
	private int replaceCard;
	//钻石
	@Column(nullable = false)
	private int diamond;
	//金币
	@Column(nullable = false)
	private int goldMoney;
	//银币
	@Column(nullable = false)
	private int silverMoney;
	
	//钱包数据修改锁
	@Transient
	private Lock _lock = new ReentrantLock();
	
	public static WalletEntity valueOf(long accountId){
		WalletEntity retEntity = new WalletEntity();
		retEntity.accountId = accountId;
		retEntity.replaceCard = 2;
		retEntity.roomCard = 0;
		retEntity.diamond = 0;
		retEntity.goldMoney = 0;
		retEntity.silverMoney = 0;
		
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

	public int getRoomCard() {
		return roomCard;
	}

	@Enhance
	public void setRoomCard(int roomCard) {
		this.roomCard = roomCard;
	}

	public int getDiamond() {
		return diamond;
	}

	@Enhance
	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	public int getReplaceCard() {
		return replaceCard;
	}

	@Enhance
	public void setReplaceCard(int replaceCard) {
		this.replaceCard = replaceCard;
	}

	public int getGoldMoney() {
		return goldMoney;
	}

	@Enhance
	public void setGoldMoney(int goldMoney) {
		this.goldMoney = goldMoney;
	}

	public int getSilverMoney() {
		return silverMoney;
	}

	@Enhance
	public void setSilverMoney(int silverMoney) {
		this.silverMoney = silverMoney;
	}
	
	
}
