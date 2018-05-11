package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * SystemPropLogVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemPropLogVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 增减道具ID */
	private java.lang.Integer splId;
	/** 道具增减数量 */
	private java.lang.Integer splAmount;
	/** 操作人 */
	private java.lang.Integer splOprtuser;
	/**  */
	private java.util.Date splTime;
	/**  */
	private java.lang.String splContent;
	/** 增加类型 1：购买 2：发放 3：扣除 4：赠送 5：游戏消耗 */
	private java.lang.Integer splType;
	//columns END
	public void setSplId(java.lang.Integer value) {
		this.splId = value;
	}
	
	public java.lang.Integer getSplId() {
		return this.splId;
	}
	public void setSplAmount(java.lang.Integer value) {
		this.splAmount = value;
	}
	
	public java.lang.Integer getSplAmount() {
		return this.splAmount;
	}
	public void setSplOprtuser(java.lang.Integer value) {
		this.splOprtuser = value;
	}
	
	public java.lang.Integer getSplOprtuser() {
		return this.splOprtuser;
	}
	public void setSplTime(java.util.Date value) {
		this.splTime = value;
	}
	
	public java.util.Date getSplTime() {
		return this.splTime;
	}
	public void setSplContent(java.lang.String value) {
		this.splContent = value;
	}
	
	public java.lang.String getSplContent() {
		return this.splContent;
	}
	public void setSplType(java.lang.Integer value) {
		this.splType = value;
	}
	
	public java.lang.Integer getSplType() {
		return this.splType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSplId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemPropLogVo == false) return false;
		if(this == obj) return true;
		SystemPropLogVo other = (SystemPropLogVo)obj;
		return new EqualsBuilder()
			.append(getSplId(),other.getSplId())
			.isEquals();
	}
	
}

