package com.palmjoys.yf1b.act.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

import com.palmjoys.yf1b.act.order.manager.OrderManager;

@Entity
@Memcached
public class BuyOrderEntity implements IEntity<Long>{
	@Id
	private long orderId;
	//订单提交的玩家Id
	@Column(nullable = false)
	private String starNO;
	//订单提交时间
	@Column(nullable = false)
	private long createTime;
	//订单RMB
	@Column(nullable = false)
	private int rmb;
	//订单游戏币
	@Column(nullable = false)
	private long goldMoney;
	//订单状态(0=等待处理,1=正在处理,2=失败,3=成功)
	@Column(nullable = false)
	private int state;
	//订单处理时间
	@Column(nullable = false)
	private long transTime;
	//订单支付类型(1=WX,2=支付宝)
	@Column(nullable = false)
	private int payType;
	
	public static BuyOrderEntity valueOf(long orderId, String starNO, int rmb, long goldMoney){
		BuyOrderEntity retEntity = new BuyOrderEntity();
		retEntity.orderId = orderId;
		retEntity.starNO = starNO;
		retEntity.createTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		retEntity.rmb = rmb;
		retEntity.goldMoney = goldMoney;
		retEntity.state = OrderManager.STATE_ORDER_WAIT;
		retEntity.transTime = 0;
		retEntity.payType = 0;
		
		return retEntity;
	}
	
	@Override
	public Long getId() {
		return orderId;
	}	

	public String getStarNO() {
		return starNO;
	}

	@Enhance
	public void setStarNO(String starNO) {
		this.starNO = starNO;
	}

	public long getCreateTime() {
		return createTime;
	}

	@Enhance
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getRmb() {
		return rmb;
	}

	@Enhance
	public void setRmb(int rmb) {
		this.rmb = rmb;
	}

	public long getGoldMoney() {
		return goldMoney;
	}

	@Enhance
	public void setGoldMoney(long goldMoney) {
		this.goldMoney = goldMoney;
	}

	public int getState() {
		return state;
	}

	@Enhance
	public void setState(int state) {
		this.state = state;
	}

	public long getTransTime() {
		return transTime;
	}

	@Enhance
	public void setTransTime(long transTime) {
		this.transTime = transTime;
	}

	public int getPayType() {
		return payType;
	}

	@Enhance
	public void setPayType(int payType) {
		this.payType = payType;
	}

}
