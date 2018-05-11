package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 
 * 
 * statistics_earnings
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class StatisticsEarnings implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer seId;
	/** 收益地区id，地区为1表示总的 */
	private java.lang.Integer seCityId;
	/** 创建时间 */
	private java.util.Date seCreateTime;
	/** 收入金额 */
	private Long earning;
	/** 提现金额 */
	private Long kiting;
	/** 利润金额 */
	private Long profit;
	/** 最大单笔金额 */
	private Long maximum;
	//columns END
	public void setSeId(java.lang.Integer value) {
		this.seId = value;
	}
	
	public java.lang.Integer getSeId() {
		return this.seId;
	}
	public void setSeCityId(java.lang.Integer value) {
		this.seCityId = value;
	}
	
	public java.lang.Integer getSeCityId() {
		return this.seCityId;
	}
	public void setSeCreateTime(java.util.Date value) {
		this.seCreateTime = value;
	}
	
	public java.util.Date getSeCreateTime() {
		return this.seCreateTime;
	}
	public void setEarning(Long value) {
		this.earning = value;
	}
	
	public Long getEarning() {
		return this.earning;
	}
	public void setKiting(Long value) {
		this.kiting = value;
	}
	
	public Long getKiting() {
		return this.kiting;
	}
	public void setProfit(Long value) {
		this.profit = value;
	}
	
	public Long getProfit() {
		return this.profit;
	}
	public void setMaximum(Long value) {
		this.maximum = value;
	}
	
	public Long getMaximum() {
		return this.maximum;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSeId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StatisticsEarnings == false) return false;
		if(this == obj) return true;
		StatisticsEarnings other = (StatisticsEarnings)obj;
		return new EqualsBuilder()
			.append(getSeId(),other.getSeId())
			.isEquals();
	}
	
}

