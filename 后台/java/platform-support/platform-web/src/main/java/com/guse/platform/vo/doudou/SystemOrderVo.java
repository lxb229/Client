package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * SystemOrderVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemOrderVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer soId;
	/** 订单编号 */
	private java.lang.Integer soNo;
	/** 订单金额 */
	private java.lang.Integer soAmounts;
	/** 付款时间 */
	private java.util.Date payTime;
	/** 付款方式 1：支付宝 2：微信支付 */
	private java.lang.Integer payType;
	/** 付款金额 */
	private Long payPrice;
	/** 支付状态 1：已支付 0：未支付 */
	private java.lang.Integer payState;
	//columns END
	public void setSoId(java.lang.Integer value) {
		this.soId = value;
	}
	
	public java.lang.Integer getSoId() {
		return this.soId;
	}
	public void setSoNo(java.lang.Integer value) {
		this.soNo = value;
	}
	
	public java.lang.Integer getSoNo() {
		return this.soNo;
	}
	public void setSoAmounts(java.lang.Integer value) {
		this.soAmounts = value;
	}
	
	public java.lang.Integer getSoAmounts() {
		return this.soAmounts;
	}
	public void setPayTime(java.util.Date value) {
		this.payTime = value;
	}
	
	public java.util.Date getPayTime() {
		return this.payTime;
	}
	public void setPayType(java.lang.Integer value) {
		this.payType = value;
	}
	
	public java.lang.Integer getPayType() {
		return this.payType;
	}
	public void setPayPrice(Long value) {
		this.payPrice = value;
	}
	
	public Long getPayPrice() {
		return this.payPrice;
	}
	public void setPayState(java.lang.Integer value) {
		this.payState = value;
	}
	
	public java.lang.Integer getPayState() {
		return this.payState;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSoId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemOrderVo == false) return false;
		if(this == obj) return true;
		SystemOrderVo other = (SystemOrderVo)obj;
		return new EqualsBuilder()
			.append(getSoId(),other.getSoId())
			.isEquals();
	}
	
}

