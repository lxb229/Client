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
public class SellOrderEntity implements IEntity<Long>{
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
	//处理备注
	@Column(nullable = false)
	private String reMarks;
	//订单处理人
	@Column(nullable = false)
	private int transPlayer;
	//订单支付类型(1=WX,2=支付宝)
	@Column(nullable = false)
	private int payType;
	//订单支付帐号
	@Column(nullable = false)
	private String payAccount;
	
	public static SellOrderEntity valueOf(long orderId, String starNO, int rmb, long goldMoney,
			int payType, String payAccount){
		SellOrderEntity retEntity = new SellOrderEntity();
		retEntity.orderId = orderId;
		retEntity.starNO = starNO;
		retEntity.createTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		retEntity.rmb = rmb;
		retEntity.goldMoney = goldMoney;
		retEntity.state = OrderManager.STATE_ORDER_WAIT;
		retEntity.transTime = 0;
		retEntity.reMarks = "";
		retEntity.transPlayer = 0;
		retEntity.payType = payType;
		retEntity.payAccount = payAccount;
		
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

	public String getReMarks() {
		return reMarks;
	}

	@Enhance
	public void setReMarks(String reMarks) {
		this.reMarks = reMarks;
	}

	public int getTransPlayer() {
		return transPlayer;
	}

	@Enhance
	public void setTransPlayer(int transPlayer) {
		this.transPlayer = transPlayer;
	}

	public int getPayType() {
		return payType;
	}

	@Enhance
	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getPayAccount() {
		return payAccount;
	}

	@Enhance
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

}
