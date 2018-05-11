package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 
 * 
 * statistics_roomcard
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class StatisticsRoomcard implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/**  */
	private java.lang.Integer srId;
	/** 区域id 为1表示总的 */
	private java.lang.Integer srCityId;
	/** 创建时间 */
	private java.util.Date srCreateTime;
	/** 生成房卡数量 */
	private java.lang.Integer createRoomcards;
	/** 销售房卡数量 */
	private java.lang.Integer marketRoomcards;
	/** 消耗房卡数量 */
	private java.lang.Integer consumeRoomcards;
	/** 发放房卡数量 */
	private java.lang.Integer giveOutRoomcards;
	//columns END
	public void setSrId(java.lang.Integer value) {
		this.srId = value;
	}
	
	public java.lang.Integer getSrId() {
		return this.srId;
	}
	public void setSrCityId(java.lang.Integer value) {
		this.srCityId = value;
	}
	
	public java.lang.Integer getSrCityId() {
		return this.srCityId;
	}
	public void setSrCreateTime(java.util.Date value) {
		this.srCreateTime = value;
	}
	
	public java.util.Date getSrCreateTime() {
		return this.srCreateTime;
	}
	public void setCreateRoomcards(java.lang.Integer value) {
		this.createRoomcards = value;
	}
	
	public java.lang.Integer getCreateRoomcards() {
		return this.createRoomcards;
	}
	public void setMarketRoomcards(java.lang.Integer value) {
		this.marketRoomcards = value;
	}
	
	public java.lang.Integer getMarketRoomcards() {
		return this.marketRoomcards;
	}
	public void setConsumeRoomcards(java.lang.Integer value) {
		this.consumeRoomcards = value;
	}
	
	public java.lang.Integer getConsumeRoomcards() {
		return this.consumeRoomcards;
	}
	public void setGiveOutRoomcards(java.lang.Integer value) {
		this.giveOutRoomcards = value;
	}
	
	public java.lang.Integer getGiveOutRoomcards() {
		return this.giveOutRoomcards;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSrId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StatisticsRoomcard == false) return false;
		if(this == obj) return true;
		StatisticsRoomcard other = (StatisticsRoomcard)obj;
		return new EqualsBuilder()
			.append(getSrId(),other.getSrId())
			.isEquals();
	}
	
}

