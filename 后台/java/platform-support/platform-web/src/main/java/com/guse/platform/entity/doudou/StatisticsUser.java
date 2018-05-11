package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 
 * 
 * statistics_user
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class StatisticsUser implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer suId;
	/** 区域id */
	private java.lang.Integer suCityId;
	/** 创建时间 */
	private java.util.Date suCreateTime;
	/** 新增用户数量 */
	private java.lang.Integer suNewUser;
	/** 活跃用户数量 */
	private java.lang.Integer suActiveUser;
	/** 新增代理数量 */
	private java.lang.Integer suNewAgency;
	//columns END
	public void setSuId(java.lang.Integer value) {
		this.suId = value;
	}
	
	public java.lang.Integer getSuId() {
		return this.suId;
	}
	public void setSuCityId(java.lang.Integer value) {
		this.suCityId = value;
	}
	
	public java.lang.Integer getSuCityId() {
		return this.suCityId;
	}
	public void setSuCreateTime(java.util.Date value) {
		this.suCreateTime = value;
	}
	
	public java.util.Date getSuCreateTime() {
		return this.suCreateTime;
	}
	public void setSuNewUser(java.lang.Integer value) {
		this.suNewUser = value;
	}
	
	public java.lang.Integer getSuNewUser() {
		return this.suNewUser;
	}
	public void setSuActiveUser(java.lang.Integer value) {
		this.suActiveUser = value;
	}
	
	public java.lang.Integer getSuActiveUser() {
		return this.suActiveUser;
	}
	public void setSuNewAgency(java.lang.Integer value) {
		this.suNewAgency = value;
	}
	
	public java.lang.Integer getSuNewAgency() {
		return this.suNewAgency;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSuId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StatisticsUser == false) return false;
		if(this == obj) return true;
		StatisticsUser other = (StatisticsUser)obj;
		return new EqualsBuilder()
			.append(getSuId(),other.getSuId())
			.isEquals();
	}
	
}

