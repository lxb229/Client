package com.palmjoys.yf1b.act.zjh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

@Entity
@Memcached
public class GameProfitPoolEntity implements IEntity<Integer>{
	@Id
	private Integer id;
	//每日预期总赢利数
	@Column(nullable = false)
	private long totalWinNum;
	//当前已赢利数
	@Column(nullable = false)
	private long currWinNum;
	//最小赢取概率
	@Column(nullable = false)
	private int minWinRate;
	//最大赢取概率
	@Column(nullable = false)
	private int maxWinRate;
	//倍率因子
	@Column(nullable = false)
	private int multitRatePower; 
	//押注因子
	@Column(nullable = false)
	private int betRatePower;
	
	public static GameProfitPoolEntity valueOf(int id){
		GameProfitPoolEntity retEntity = new GameProfitPoolEntity();
		retEntity.id = id;
		retEntity.totalWinNum = 100000;
		retEntity.currWinNum = 0;
		retEntity.minWinRate = 50;
		retEntity.maxWinRate = 90;
		retEntity.multitRatePower = 2;
		retEntity.betRatePower = 2;
		
		return retEntity;
	}
		
	@Override
	public Integer getId() {
		return id;
	}

	public long getTotalWinNum() {
		return totalWinNum;
	}

	@Enhance
	public void setTotalWinNum(long totalWinNum) {
		this.totalWinNum = totalWinNum;
	}

	public long getCurrWinNum() {
		return currWinNum;
	}

	@Enhance
	public void setCurrWinNum(long currWinNum) {
		this.currWinNum = currWinNum;
	}

	public int getMinWinRate() {
		return minWinRate;
	}

	@Enhance
	public void setMinWinRate(int minWinRate) {
		this.minWinRate = minWinRate;
	}

	public int getMaxWinRate() {
		return maxWinRate;
	}

	@Enhance
	public void setMaxWinRate(int maxWinRate) {
		this.maxWinRate = maxWinRate;
	}	

	public int getMultitRatePower() {
		return multitRatePower;
	}

	@Enhance
	public void setMultitRatePower(int multitRatePower) {
		this.multitRatePower = multitRatePower;
	}

	public int getBetRatePower() {
		return betRatePower;
	}

	@Enhance
	public void setBetRatePower(int betRatePower) {
		this.betRatePower = betRatePower;
	}

}
