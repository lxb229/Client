package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 
 * 
 * game_activity_rotor_statistics
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class GameActivityRotorStatistics implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 活动概况id */
	private java.lang.Long garsId;
	/** 统计日期 */
	private java.util.Date garsDate;
	/** 玩家总数 */
	private java.lang.Integer garsPlayerNum;
	/** 总次数 */
	private java.lang.Integer garsTatolCount;
	/** 总回收金币 */
	private java.lang.Long garsRecycledGold;
	/** 周卡 */
	private java.lang.Integer garsWeekNum;
	/** 月卡 */
	private java.lang.Integer garsMonthNum;
	/** 钻石(5) */
	private java.lang.Integer garsDiamond5Num;
	/** 钻石(8) */
	private java.lang.Integer garsDiamond8Num;
	/** 金币(2000) */
	private java.lang.Long garsGold2000Num;
	/** 金币(5000) */
	private java.lang.Long garsGold5000Num;
	/** 金币(1W) */
	private java.lang.Long garsGold1wNum;
	/** 金币(5W) */
	private java.lang.Long garsGold5wNum;
	/** 金币(10W) */
	private java.lang.Long garsGold10wNum;
	/** 金币(50W) */
	private java.lang.Long garsGold50wNum;
	/** 金币(300W) */
	private java.lang.Long garsGold300wNum;
	/** 金币(1000W) */
	private java.lang.Long garsGold1000wNum;
	/** 数据创建时间 */
	private java.util.Date garsCreateTime;
	//columns END
	public void setGarsId(java.lang.Long value) {
		this.garsId = value;
	}
	
	public java.lang.Long getGarsId() {
		return this.garsId;
	}
	public void setGarsDate(java.util.Date value) {
		this.garsDate = value;
	}
	
	public java.util.Date getGarsDate() {
		return this.garsDate;
	}
	public void setGarsPlayerNum(java.lang.Integer value) {
		this.garsPlayerNum = value;
	}
	
	public java.lang.Integer getGarsPlayerNum() {
		return this.garsPlayerNum;
	}
	public void setGarsTatolCount(java.lang.Integer value) {
		this.garsTatolCount = value;
	}
	
	public java.lang.Integer getGarsTatolCount() {
		return this.garsTatolCount;
	}
	public void setGarsRecycledGold(java.lang.Long value) {
		this.garsRecycledGold = value;
	}
	
	public java.lang.Long getGarsRecycledGold() {
		return this.garsRecycledGold;
	}
	public void setGarsWeekNum(java.lang.Integer value) {
		this.garsWeekNum = value;
	}
	
	public java.lang.Integer getGarsWeekNum() {
		return this.garsWeekNum;
	}
	public void setGarsMonthNum(java.lang.Integer value) {
		this.garsMonthNum = value;
	}
	
	public java.lang.Integer getGarsMonthNum() {
		return this.garsMonthNum;
	}
	public void setGarsDiamond5Num(java.lang.Integer value) {
		this.garsDiamond5Num = value;
	}
	
	public java.lang.Integer getGarsDiamond5Num() {
		return this.garsDiamond5Num;
	}
	public void setGarsDiamond8Num(java.lang.Integer value) {
		this.garsDiamond8Num = value;
	}
	
	public java.lang.Integer getGarsDiamond8Num() {
		return this.garsDiamond8Num;
	}
	public void setGarsGold2000Num(java.lang.Long value) {
		this.garsGold2000Num = value;
	}
	
	public java.lang.Long getGarsGold2000Num() {
		return this.garsGold2000Num;
	}
	public void setGarsGold5000Num(java.lang.Long value) {
		this.garsGold5000Num = value;
	}
	
	public java.lang.Long getGarsGold5000Num() {
		return this.garsGold5000Num;
	}
	public void setGarsGold1wNum(java.lang.Long value) {
		this.garsGold1wNum = value;
	}
	
	public java.lang.Long getGarsGold1wNum() {
		return this.garsGold1wNum;
	}
	public void setGarsGold5wNum(java.lang.Long value) {
		this.garsGold5wNum = value;
	}
	
	public java.lang.Long getGarsGold5wNum() {
		return this.garsGold5wNum;
	}
	public void setGarsGold10wNum(java.lang.Long value) {
		this.garsGold10wNum = value;
	}
	
	public java.lang.Long getGarsGold10wNum() {
		return this.garsGold10wNum;
	}
	public void setGarsGold50wNum(java.lang.Long value) {
		this.garsGold50wNum = value;
	}
	
	public java.lang.Long getGarsGold50wNum() {
		return this.garsGold50wNum;
	}
	public void setGarsGold300wNum(java.lang.Long value) {
		this.garsGold300wNum = value;
	}
	
	public java.lang.Long getGarsGold300wNum() {
		return this.garsGold300wNum;
	}
	public void setGarsGold1000wNum(java.lang.Long value) {
		this.garsGold1000wNum = value;
	}
	
	public java.lang.Long getGarsGold1000wNum() {
		return this.garsGold1000wNum;
	}
	public void setGarsCreateTime(java.util.Date value) {
		this.garsCreateTime = value;
	}
	
	public java.util.Date getGarsCreateTime() {
		return this.garsCreateTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getGarsId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GameActivityRotorStatistics == false) return false;
		if(this == obj) return true;
		GameActivityRotorStatistics other = (GameActivityRotorStatistics)obj;
		return new EqualsBuilder()
			.append(getGarsId(),other.getGarsId())
			.isEquals();
	}
	
}

