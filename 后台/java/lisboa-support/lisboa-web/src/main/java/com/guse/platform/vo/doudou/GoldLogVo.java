package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * GoldLogVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class GoldLogVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/**  */
	private java.lang.Integer id;
	/** 玩家Id */
	private java.lang.String starNo;
	/** 时间 */
	public String eventTime;
	/** 时间 */
	private java.util.Date theEventTime;
	/** 输赢分数 */
	private java.lang.Integer score;
	/** 游戏(1=牛牛,2=金花) */
	private java.lang.Integer gameType;
	//columns END
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setStarNo(java.lang.String value) {
		this.starNo = value;
	}
	
	public java.lang.String getStarNo() {
		return this.starNo;
	}
	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public void setTheEventTime(java.util.Date value) {
		this.theEventTime = value;
	}
	
	public java.util.Date getTheEventTime() {
		return this.theEventTime;
	}
	public void setScore(java.lang.Integer value) {
		this.score = value;
	}
	
	public java.lang.Integer getScore() {
		return this.score;
	}
	public void setGameType(java.lang.Integer value) {
		this.gameType = value;
	}
	
	public java.lang.Integer getGameType() {
		return this.gameType;
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
		if(obj instanceof GoldLogVo == false) return false;
		if(this == obj) return true;
		GoldLogVo other = (GoldLogVo)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

