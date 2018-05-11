package com.palmjoys.yf1b.act.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

/**
 * 订单配置表
 * */
@Entity
@Memcached
public class OrderCfgEntity implements IEntity<Integer>{
	@Id
	private int recordId;
	//人民币比游戏币比例(1个人民币比多少游戏币)
	@Column(nullable = false)
	private int rmb2goldMoney;
	//游戏币比人民币比例(多少游戏币比1人民币)
	@Column(nullable = false)
	private int goldMoney2Rmb;
	//人民币最小兑换数
	@Column(nullable = false)
	private int minRmb;
	
	public static OrderCfgEntity valueOf(int id){
		OrderCfgEntity retEntity = new OrderCfgEntity();
		retEntity.recordId = id;
		retEntity.rmb2goldMoney = 100;
		retEntity.goldMoney2Rmb = 120;
		retEntity.minRmb = 5;
		return retEntity;
	}
	
	
	@Override
	public Integer getId() {
		return recordId;
	}

	public int getRmb2goldMoney() {
		return rmb2goldMoney;
	}

	@Enhance
	public void setRmb2goldMoney(int rmb2goldMoney) {
		this.rmb2goldMoney = rmb2goldMoney;
	}

	public int getGoldMoney2Rmb() {
		return goldMoney2Rmb;
	}

	@Enhance
	public void setGoldMoney2Rmb(int goldMoney2Rmb) {
		this.goldMoney2Rmb = goldMoney2Rmb;
	}

	public int getMinRmb() {
		return minRmb;
	}

	@Enhance
	public void setMinRmb(int minRmb) {
		this.minRmb = minRmb;
	}
}
