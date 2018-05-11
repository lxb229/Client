package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * SystemAgencyVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemAgencyVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/**  */
	private java.lang.Integer saId;
	/** 用户id */
	private java.lang.Integer saUserId;
	/** 城市区域id */
	private java.lang.Integer saCityId;
	/** 成为代理时间 */
	private java.util.Date saCreateTime;
	/** 小于10万提成比例 */
	private Long saLessOne;
	/** 10万到20万提成比例 */
	private Long saOneToTwo;
	/** 20万到30万提成比例 */
	private Long saTwoToThree;
	/** 30万到40万提成比例 */
	private Long saThreeToFour;
	/** 超过40万提成比例 */
	private Long saGreaterFoure;
	//columns END
	public void setSaId(java.lang.Integer value) {
		this.saId = value;
	}
	
	public java.lang.Integer getSaId() {
		return this.saId;
	}
	public void setSaUserId(java.lang.Integer value) {
		this.saUserId = value;
	}
	
	public java.lang.Integer getSaUserId() {
		return this.saUserId;
	}
	public void setSaCityId(java.lang.Integer value) {
		this.saCityId = value;
	}
	
	public java.lang.Integer getSaCityId() {
		return this.saCityId;
	}
	public void setSaCreateTime(java.util.Date value) {
		this.saCreateTime = value;
	}
	
	public java.util.Date getSaCreateTime() {
		return this.saCreateTime;
	}
	public void setSaLessOne(Long value) {
		this.saLessOne = value;
	}
	
	public Long getSaLessOne() {
		return this.saLessOne;
	}
	public void setSaOneToTwo(Long value) {
		this.saOneToTwo = value;
	}
	
	public Long getSaOneToTwo() {
		return this.saOneToTwo;
	}
	public void setSaTwoToThree(Long value) {
		this.saTwoToThree = value;
	}
	
	public Long getSaTwoToThree() {
		return this.saTwoToThree;
	}
	public void setSaThreeToFour(Long value) {
		this.saThreeToFour = value;
	}
	
	public Long getSaThreeToFour() {
		return this.saThreeToFour;
	}
	public void setSaGreaterFoure(Long value) {
		this.saGreaterFoure = value;
	}
	
	public Long getSaGreaterFoure() {
		return this.saGreaterFoure;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSaId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemAgencyVo == false) return false;
		if(this == obj) return true;
		SystemAgencyVo other = (SystemAgencyVo)obj;
		return new EqualsBuilder()
			.append(getSaId(),other.getSaId())
			.isEquals();
	}
	
}

