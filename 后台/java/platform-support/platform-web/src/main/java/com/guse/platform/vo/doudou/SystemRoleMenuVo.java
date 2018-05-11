package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * SystemRoleMenuVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemRoleMenuVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 角色菜单主键 */
	private java.lang.Integer srmId;
	/** 菜单id */
	private java.lang.Integer smId;
	/** 角色ID */
	private java.lang.Integer sroId;
	//columns END
	public void setSrmId(java.lang.Integer value) {
		this.srmId = value;
	}
	
	public java.lang.Integer getSrmId() {
		return this.srmId;
	}
	public void setSmId(java.lang.Integer value) {
		this.smId = value;
	}
	
	public java.lang.Integer getSmId() {
		return this.smId;
	}
	public void setSroId(java.lang.Integer value) {
		this.sroId = value;
	}
	
	public java.lang.Integer getSroId() {
		return this.sroId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSrmId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemRoleMenuVo == false) return false;
		if(this == obj) return true;
		SystemRoleMenuVo other = (SystemRoleMenuVo)obj;
		return new EqualsBuilder()
			.append(getSrmId(),other.getSrmId())
			.isEquals();
	}
	
}

