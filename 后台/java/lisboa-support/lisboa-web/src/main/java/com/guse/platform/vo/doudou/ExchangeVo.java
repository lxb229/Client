package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * ExchangeVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class ExchangeVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer id;
	/** 人民币比游戏币比例 */
	private java.lang.Integer rmb2goldMoney;
	/** 游戏币比人民币比例 */
	private java.lang.Integer goldMoney2Rmb;
	//columns END
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setRmb2goldMoney(java.lang.Integer value) {
		this.rmb2goldMoney = value;
	}
	
	public java.lang.Integer getRmb2goldMoney() {
		return this.rmb2goldMoney;
	}
	public void setGoldMoney2Rmb(java.lang.Integer value) {
		this.goldMoney2Rmb = value;
	}
	
	public java.lang.Integer getGoldMoney2Rmb() {
		return this.goldMoney2Rmb;
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
		if(obj instanceof ExchangeVo == false) return false;
		if(this == obj) return true;
		ExchangeVo other = (ExchangeVo)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

