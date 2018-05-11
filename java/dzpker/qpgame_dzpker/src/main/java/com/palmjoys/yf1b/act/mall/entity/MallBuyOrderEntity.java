package com.palmjoys.yf1b.act.mall.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

import com.palmjoys.yf1b.act.mall.manager.MallOrderManager;

@Entity
@Memcached
public class MallBuyOrderEntity implements IEntity<Long>{
	//订单Id
	@Id
	private long orderId;
	//订单申请玩家
	@Column(nullable = false)
	private long accountId;
	//申请玩家明星号
	@Column(nullable = false)
	private String starNO;
	//订单支付RMB
	@Column(nullable = false)
	private int rmb;
	//订单获得的房卡
	@Column(nullable = false)
	private long roomCard;
	//订单获得的金币
	@Column(nullable = false)
	private long goldMoney;
	//订单获得的钻石
	@Column(nullable = false)
	private long diamond;
	//订单创建时间
	@Column(nullable = false)
	private long createTime;
	//订单处理状态(0=等待处理,1=正在处理,2=处理成功,3=处理失败)
	@Column(nullable = false)
	private int state;
	//订单处理时间
	@Column(nullable = false)
	private long transTime;
	
	public static MallBuyOrderEntity valueOf(long orderId, long accountId, String starNO, int rmb, long roomCard, 
			long goldMoney, long diamond){
		MallBuyOrderEntity retEntity = new MallBuyOrderEntity();
		retEntity.orderId = orderId;
		retEntity.starNO = starNO;
		retEntity.accountId = accountId;
		retEntity.rmb = rmb;
		retEntity.roomCard = roomCard;
		retEntity.goldMoney = goldMoney;
		retEntity.diamond = diamond;
		retEntity.createTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		retEntity.state = MallOrderManager.ORDER_STATE_WAIT;
		retEntity.transTime = 0;
		
		return retEntity;
	}

	@Override
	public Long getId() {
		return orderId;
	}

	public long getOrderId() {
		return orderId;
	}

	@Enhance
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getAccountId() {
		return accountId;
	}

	@Enhance
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getStarNO() {
		return starNO;
	}

	@Enhance
	public void setStarNO(String starNO) {
		this.starNO = starNO;
	}

	public int getRmb() {
		return rmb;
	}

	@Enhance
	public void setRmb(int rmb) {
		this.rmb = rmb;
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

	public long getCreateTime() {
		return createTime;
	}

	@Enhance
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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

}
