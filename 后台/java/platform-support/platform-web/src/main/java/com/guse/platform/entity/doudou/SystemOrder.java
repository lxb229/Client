package com.guse.platform.entity.doudou;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.entity.system.Users;


/**
 * 
 * 
 * system_order
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemOrder implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer soId;
	/** 购买人id */
	private java.lang.Integer userId;
	/** 购买产品id */
	private java.lang.Integer productId;
	/** 订单编号 */
	private java.lang.String soNo;
	/** 订单金额 */
	private java.lang.Double soAmounts;
	/** 付款时间 */
	private java.util.Date payTime;
	/** 付款方式 1：支付宝 2：微信支付 */
	private java.lang.Integer payType;
	/** 付款金额 */
	private java.lang.Double payPrice;
	/** 支付状态 1：已支付 0：未支付 */
	private java.lang.Integer payState;
	/** 创建时间 */
	private Date createTime;
	/** 提现标志，0：未体现 1：已提现 */
	private java.lang.Integer cashState;
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(userId == null){
			 vb.setMsg("购买人为空！");
	         return vb;
		 }
		 if(soNo == null){
			 vb.setMsg("订单编号为空！");
	         return vb;
		 }
		 if(productId == null){
			 vb.setMsg("产品为空！");
	         return vb;
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	
	//columns END
	
	private SystemProduct product;
	private Users users;
	
	public SystemProduct getProduct() {
		return product;
	}

	public void setProduct(SystemProduct product) {
		this.product = product;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public void setSoId(java.lang.Integer value) {
		this.soId = value;
	}
	
	public java.lang.Integer getSoId() {
		return this.soId;
	}
	
	public java.lang.Integer getUserId() {
		return userId;
	}

	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}

	public java.lang.Integer getProductId() {
		return productId;
	}

	public void setProductId(java.lang.Integer productId) {
		this.productId = productId;
	}

	public void setSoNo(java.lang.String value) {
		this.soNo = value;
	}
	
	public java.lang.String getSoNo() {
		return this.soNo;
	}
	public void setSoAmounts(java.lang.Double value) {
		this.soAmounts = value;
	}
	
	public java.lang.Double getSoAmounts() {
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
	public void setPayPrice(java.lang.Double value) {
		this.payPrice = value;
	}
	
	public java.lang.Double getPayPrice() {
		return this.payPrice;
	}
	public void setPayState(java.lang.Integer value) {
		this.payState = value;
	}
	
	public java.lang.Integer getPayState() {
		return this.payState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public java.lang.Integer getCashState() {
		return cashState;
	}

	public void setCashState(java.lang.Integer cashState) {
		this.cashState = cashState;
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
		if(obj instanceof SystemOrder == false) return false;
		if(this == obj) return true;
		SystemOrder other = (SystemOrder)obj;
		return new EqualsBuilder()
			.append(getSoId(),other.getSoId())
			.isEquals();
	}
	
}

