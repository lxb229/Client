package com.guse.platform.entity.doudou;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.guse.platform.common.base.ValidataBean;


/**
 * 
 * 
 * user_roomcards
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class UserRoomcards implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer urcId;
	/** 拥有人人id */
	private java.lang.Integer userId;
	/** 当前拥有房卡数量 */
	private java.lang.Integer newRoomcards;
	/** 累计购买房卡数 */
	private java.lang.Integer buyRoomcards;
	/** 累计消耗房卡数量 */
	private java.lang.Integer useRoomcards;
	/** 获赠房卡数量 */
	private java.lang.Integer givenRoomcards;
	/** 送出房卡数量 */
	private java.lang.Integer sendOutRoomcards;
	/** 发放房卡数量 */
	private java.lang.Integer receiveRoomcards;
	/** 累计消费金额 */
	private java.lang.Double consumptionAmount;
	/** 累计收益金额 */
	private java.lang.Double earningsAmount;
	/** 创建时间 */
	private Date createTime;
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(userId == null){
			 vb.setMsg("拥有人为空！");
	         return vb;
		 }
		 if(newRoomcards == null){
			 vb.setMsg("当前房卡数量为空！");
	         return vb;
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	
	//columns END
	public void setUrcId(java.lang.Integer value) {
		this.urcId = value;
	}
	
	public java.lang.Integer getUrcId() {
		return this.urcId;
	}
	
	public java.lang.Integer getUserId() {
		return userId;
	}

	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}

	public void setNewRoomcards(java.lang.Integer value) {
		this.newRoomcards = value;
	}
	
	public java.lang.Integer getNewRoomcards() {
		return this.newRoomcards;
	}
	public void setBuyRoomcards(java.lang.Integer value) {
		this.buyRoomcards = value;
	}
	
	public java.lang.Integer getBuyRoomcards() {
		return this.buyRoomcards;
	}
	public void setUseRoomcards(java.lang.Integer value) {
		this.useRoomcards = value;
	}
	
	public java.lang.Integer getUseRoomcards() {
		return this.useRoomcards;
	}
	public void setGivenRoomcards(java.lang.Integer value) {
		this.givenRoomcards = value;
	}
	
	public java.lang.Integer getGivenRoomcards() {
		return this.givenRoomcards;
	}
	public void setSendOutRoomcards(java.lang.Integer value) {
		this.sendOutRoomcards = value;
	}
	
	public java.lang.Integer getSendOutRoomcards() {
		return this.sendOutRoomcards;
	}
	public void setReceiveRoomcards(java.lang.Integer value) {
		this.receiveRoomcards = value;
	}
	
	public java.lang.Integer getReceiveRoomcards() {
		return this.receiveRoomcards;
	}
	public void setConsumptionAmount(java.lang.Double value) {
		this.consumptionAmount = value;
	}
	
	public java.lang.Double getConsumptionAmount() {
		return this.consumptionAmount;
	}
	public void setEarningsAmount(java.lang.Double value) {
		this.earningsAmount = value;
	}
	
	public java.lang.Double getEarningsAmount() {
		return this.earningsAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getUrcId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UserRoomcards == false) return false;
		if(this == obj) return true;
		UserRoomcards other = (UserRoomcards)obj;
		return new EqualsBuilder()
			.append(getUrcId(),other.getUrcId())
			.isEquals();
	}
	
}

