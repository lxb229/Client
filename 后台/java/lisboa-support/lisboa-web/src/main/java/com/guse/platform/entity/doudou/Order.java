package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 
 * 
 * order
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class Order implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer id;
	/** 订单Id */
	private java.lang.String orderId;
	/** 订单提交人 */
	private java.lang.String starNo;
	/** 订单提交时间 */
	private java.util.Date createTime;
	/** 订单RMB */
	private java.lang.Integer rmb;
	/** 订单游戏币 */
	private java.lang.Integer goldMoney;
	/** 订单状态(0=等待处理,1=正在处理,2=失败,3=成功) */
	private java.lang.Integer state;
	/** 订单处理时间 */
	private java.util.Date transTime;
	/** 处理备注 */
	private java.lang.String reMarks;
	/** 订单处理人 */
	private java.lang.Integer transPlayer;
	/** 订单支付类型(1=WX,2=支付宝) */
	private java.lang.Integer payType;
	/** 订单支付帐号 */
	private java.lang.String payAccount;
	//columns END
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setOrderId(java.lang.String value) {
		this.orderId = value;
	}
	
	public java.lang.String getOrderId() {
		return this.orderId;
	}
	public void setStarNo(java.lang.String value) {
		this.starNo = value;
	}
	
	public java.lang.String getStarNo() {
		return this.starNo;
	}
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	public void setRmb(java.lang.Integer value) {
		this.rmb = value;
	}
	
	public java.lang.Integer getRmb() {
		return this.rmb;
	}
	public void setGoldMoney(java.lang.Integer value) {
		this.goldMoney = value;
	}
	
	public java.lang.Integer getGoldMoney() {
		return this.goldMoney;
	}
	public void setState(java.lang.Integer value) {
		this.state = value;
	}
	
	public java.lang.Integer getState() {
		return this.state;
	}
	public void setTransTime(java.util.Date value) {
		this.transTime = value;
	}
	
	public java.util.Date getTransTime() {
		return this.transTime;
	}
	public void setReMarks(java.lang.String value) {
		this.reMarks = value;
	}
	
	public java.lang.String getReMarks() {
		return this.reMarks;
	}
	public void setTransPlayer(java.lang.Integer value) {
		this.transPlayer = value;
	}
	
	public java.lang.Integer getTransPlayer() {
		return this.transPlayer;
	}
	public void setPayType(java.lang.Integer value) {
		this.payType = value;
	}
	
	public java.lang.Integer getPayType() {
		return this.payType;
	}
	public void setPayAccount(java.lang.String value) {
		this.payAccount = value;
	}
	
	public java.lang.String getPayAccount() {
		return this.payAccount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Order == false) return false;
		if(this == obj) return true;
		Order other = (Order)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

