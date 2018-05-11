package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * SystemProductVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemProductVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer spId;
	/** 产品名称 */
	private java.lang.String spName;
	/** 房卡数量 */
	private java.lang.Integer spAmount;
	/** 产品原价 */
	private Long spCost;
	/** 产品现价 */
	private Long spPrice;
	/** 状态：0 商场展示 -1 商场不展示 */
	private java.lang.Integer spState;
	//columns END
	public void setSpId(java.lang.Integer value) {
		this.spId = value;
	}
	
	public java.lang.Integer getSpId() {
		return this.spId;
	}
	public void setSpName(java.lang.String value) {
		this.spName = value;
	}
	
	public java.lang.String getSpName() {
		return this.spName;
	}
	public void setSpAmount(java.lang.Integer value) {
		this.spAmount = value;
	}
	
	public java.lang.Integer getSpAmount() {
		return this.spAmount;
	}
	public void setSpCost(Long value) {
		this.spCost = value;
	}
	
	public Long getSpCost() {
		return this.spCost;
	}
	public void setSpPrice(Long value) {
		this.spPrice = value;
	}
	
	public Long getSpPrice() {
		return this.spPrice;
	}
	public void setSpState(java.lang.Integer value) {
		this.spState = value;
	}
	
	public java.lang.Integer getSpState() {
		return this.spState;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSpId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemProductVo == false) return false;
		if(this == obj) return true;
		SystemProductVo other = (SystemProductVo)obj;
		return new EqualsBuilder()
			.append(getSpId(),other.getSpId())
			.isEquals();
	}
	
}

