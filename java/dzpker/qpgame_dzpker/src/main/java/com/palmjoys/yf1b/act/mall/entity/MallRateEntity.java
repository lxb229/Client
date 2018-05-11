package com.palmjoys.yf1b.act.mall.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

/**
 * 商城比例配置
 * */
@Entity
@Memcached
public class MallRateEntity implements IEntity<Integer>{
	@Id
	private int cfgId;
	//1人民币比游戏币
	@Column(nullable = false)
	private int rmb2GoldMoney;
	//多少游戏币比1人民币
	@Column(nullable = false)
	private int goldMoney2Rmb;
	//1人民币比钻石
	@Column(nullable = false)
	private int rmb2Diamond;
	//多少钻石比1人民币
	@Column(nullable = false)
	private int diamond2Rmb;	

	public static MallRateEntity valueOf(int cfgId){
		MallRateEntity retEntity = new MallRateEntity();
		retEntity.cfgId = cfgId;
		retEntity.rmb2GoldMoney = 1;
		retEntity.goldMoney2Rmb = 1;
		retEntity.rmb2Diamond = 1;
		retEntity.diamond2Rmb = 1;
		
		return retEntity;
	}
	
	@Override
	public Integer getId() {
		return cfgId;
	}

	public int getRmb2GoldMoney() {
		return rmb2GoldMoney;
	}

	@Enhance
	public void setRmb2GoldMoney(int rmb2GoldMoney) {
		this.rmb2GoldMoney = rmb2GoldMoney;
	}

	public int getGoldMoney2Rmb() {
		return goldMoney2Rmb;
	}

	@Enhance
	public void setGoldMoney2Rmb(int goldMoney2Rmb) {
		this.goldMoney2Rmb = goldMoney2Rmb;
	}

	public int getRmb2Diamond() {
		return rmb2Diamond;
	}

	@Enhance
	public void setRmb2Diamond(int rmb2Diamond) {
		this.rmb2Diamond = rmb2Diamond;
	}

	public int getDiamond2Rmb() {
		return diamond2Rmb;
	}

	@Enhance
	public void setDiamond2Rmb(int diamond2Rmb) {
		this.diamond2Rmb = diamond2Rmb;
	}

}
