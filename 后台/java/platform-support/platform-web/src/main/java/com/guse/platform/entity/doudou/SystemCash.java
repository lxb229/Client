package com.guse.platform.entity.doudou;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.entity.system.Users;


/**
 * 
 * 
 * system_cash
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemCash implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键 */
	private java.lang.Integer scId;
	/** 所属地区 */
	private java.lang.Integer cityId;
	/** 提现月份 */
	private java.lang.String scMouth;
	/** 提现金额 */
	private java.lang.Double scAmount;
	/** 提现状态 */
	private java.lang.Integer scState;
	/** 审核备注 */
	private java.lang.String scRemark;
	/** 创建时间 */
	private Date createTime;
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(scState==2 && StringUtils.isBlank(scRemark)){
			 vb.setMsg("请填写审核备注！");
	         return vb;
		 }
		 
		 vb.setFlag(true);
	     return vb; 
	}
	
	//columns END
	
	private City city;
	
	private SystemAgency agency;
	
	private Users agencyUser;
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public void setScId(java.lang.Integer value) {
		this.scId = value;
	}
	
	public java.lang.Integer getScId() {
		return this.scId;
	}
	public void setCityId(java.lang.Integer value) {
		this.cityId = value;
	}
	
	public java.lang.Integer getCityId() {
		return this.cityId;
	}
	public void setScMouth(java.lang.String value) {
		this.scMouth = value;
	}
	
	public java.lang.String getScMouth() {
		return this.scMouth;
	}
	public void setScAmount(java.lang.Double value) {
		this.scAmount = value;
	}
	
	public java.lang.Double getScAmount() {
		return this.scAmount;
	}
	public void setScState(java.lang.Integer value) {
		this.scState = value;
	}
	
	public java.lang.Integer getScState() {
		return this.scState;
	}
	public void setScRemark(java.lang.String value) {
		this.scRemark = value;
	}
	
	public java.lang.String getScRemark() {
		return this.scRemark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public SystemAgency getAgency() {
		return agency;
	}

	public void setAgency(SystemAgency agency) {
		this.agency = agency;
	}

	public Users getAgencyUser() {
		return agencyUser;
	}

	public void setAgencyUser(Users agencyUser) {
		this.agencyUser = agencyUser;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getScId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemCash == false) return false;
		if(this == obj) return true;
		SystemCash other = (SystemCash)obj;
		return new EqualsBuilder()
			.append(getScId(),other.getScId())
			.isEquals();
	}
	
}

