package com.guse.platform.entity.doudou;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import com.guse.platform.common.base.ValidataBean;


/**
 * 
 * 
 * gold_log
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class GoldLog implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/**  */
	private java.lang.Integer id;
	/** 玩家Id */
	private java.lang.String starNo;
	/** 时间 */
	private java.util.Date theEventTime;
	/** 输赢分数 */
	private java.lang.Integer score;
	/** 游戏(1=牛牛,2=金花) */
	private java.lang.Integer gameType;
	/**开始时间*/
	private Date luckStart;
	/**结束时间*/
	private Date luckEnd;
	//columns END
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(StringUtils.isBlank(starNo)){
			 vb.setMsg("玩家id为空！");
	         return vb;
		 }
		 if(theEventTime == null) {
			 vb.setMsg("时间为空！");
	         return vb;
		 }
		 if(score == null) {
			 vb.setMsg("输赢分数为空！");
	         return vb;
		 }
		 if(gameType == null) {
			 vb.setMsg("游戏类型为空！");
	         return vb;
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	
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

	public Date getLuckStart() {
		return luckStart;
	}

	public void setLuckStart(Date luckStart) {
		this.luckStart = luckStart;
	}

	public Date getLuckEnd() {
		return luckEnd;
	}

	public void setLuckEnd(Date luckEnd) {
		this.luckEnd = luckEnd;
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
		if(obj instanceof GoldLog == false) return false;
		if(this == obj) return true;
		GoldLog other = (GoldLog)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

