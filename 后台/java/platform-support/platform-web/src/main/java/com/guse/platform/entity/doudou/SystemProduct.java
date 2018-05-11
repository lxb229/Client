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
 * system_product
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemProduct implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer spId;
	/** 产品名称 */
	private java.lang.String spName;
	/** 房卡数量 */
	private java.lang.Integer spAmount;
	/** 产品原价 */
	private java.lang.Double spCost;
	/** 产品现价 */
	private java.lang.Double spPrice;
	/** 状态：1商场展示 0 商场不展示 */
	private java.lang.Integer spState;
	/** 生成时间 */
	private Date createTime;
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(StringUtils.isBlank(spName)){
			 vb.setMsg("请填写产品名称！");
	         return vb;
		 }
		 if(spAmount == null) {
			 vb.setMsg("请填写房卡数量！");
	         return vb;
		 }
		 if(spCost == null) {
			 vb.setMsg("请填写产品原价！");
	         return vb;
		 }
		 if(spPrice == null) {
			 vb.setMsg("请填写产品现价！");
	         return vb;
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	
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
	public void setSpCost(java.lang.Double value) {
		this.spCost = value;
	}
	
	public java.lang.Double getSpCost() {
		return this.spCost;
	}
	public void setSpPrice(java.lang.Double value) {
		this.spPrice = value;
	}
	
	public java.lang.Double getSpPrice() {
		return this.spPrice;
	}
	public void setSpState(java.lang.Integer value) {
		this.spState = value;
	}
	
	public java.lang.Integer getSpState() {
		return this.spState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
		if(obj instanceof SystemProduct == false) return false;
		if(this == obj) return true;
		SystemProduct other = (SystemProduct)obj;
		return new EqualsBuilder()
			.append(getSpId(),other.getSpId())
			.isEquals();
	}
	
}

