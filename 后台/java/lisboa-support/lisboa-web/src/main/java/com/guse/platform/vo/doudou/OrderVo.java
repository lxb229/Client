package com.guse.platform.vo.doudou;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * OrderVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class OrderVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer id;
	/** 订单Id */
	private java.lang.String orderId;
	/** 订单提交人 */
	private java.lang.String starNO;
	/** 订单提交时间 */
	private java.lang.Long createTime;
	private Date theCreateTime;
	/** 订单RMB */
	private java.lang.Integer rmb;
	/** 订单游戏币 */
	private java.lang.Integer goldMoney;
	/** 订单状态(0=等待处理,1=正在处理,2=失败,3=成功) */
	private java.lang.Integer state;
	/** 订单处理时间 */
	private java.lang.Long transTime;
	private Date theTransTime;
	/** 处理备注 */
	private java.lang.String reMarks;
	/** 订单处理人 */
	private java.lang.Integer transPlayer;
	/** 订单支付类型(1=WX,2=支付宝) */
	private java.lang.Integer payType;
	/** 订单支付帐号 */
	private java.lang.String payAccount;
	//columns END
	/**排序方式(1=小到大,2=大到小)*/
	private int sortStyle;
	/**排序(1=订单状态,2=创建时间)*/
	private int sortCondition;
	/**开始时间*/
	private Date orderStart;
	/**结束时间*/
	private Date orderEnd; 
	
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
	
	public java.lang.String getStarNO() {
		return starNO;
	}

	public void setStarNO(java.lang.String starNO) {
		this.starNO = starNO;
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
	
	public int getSortStyle() {
		return sortStyle;
	}

	public void setSortStyle(int sortStyle) {
		this.sortStyle = sortStyle;
	}

	public int getSortCondition() {
		return sortCondition;
	}

	public void setSortCondition(int sortCondition) {
		this.sortCondition = sortCondition;
	}

	public Date getOrderStart() {
		return orderStart;
	}

	public void setOrderStart(Date orderStart) {
		this.orderStart = orderStart;
	}

	public Date getOrderEnd() {
		return orderEnd;
	}

	public void setOrderEnd(Date orderEnd) {
		this.orderEnd = orderEnd;
	}

	public java.lang.Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.lang.Long createTime) {
		this.createTime = createTime;
	}

	public Date getTheCreateTime() {
		return theCreateTime;
	}

	public void setTheCreateTime(Date theCreateTime) {
		this.theCreateTime = theCreateTime;
	}

	public java.lang.Long getTransTime() {
		return transTime;
	}

	public void setTransTime(java.lang.Long transTime) {
		this.transTime = transTime;
	}

	public Date getTheTransTime() {
		return theTransTime;
	}

	public void setTheTransTime(Date theTransTime) {
		this.theTransTime = theTransTime;
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
		if(obj instanceof OrderVo == false) return false;
		if(this == obj) return true;
		OrderVo other = (OrderVo)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

