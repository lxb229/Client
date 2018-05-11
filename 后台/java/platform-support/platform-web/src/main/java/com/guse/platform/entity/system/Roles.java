package com.guse.platform.entity.system;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import com.guse.platform.common.base.ValidataBean;


/**
 * tableName : system_roles
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class Roles implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/**  */
	private java.lang.Integer roleId;
	/** 角色名称 */
	private java.lang.String roleName;
	/** 菜单权限 */
	private java.lang.String menuRights;
	/** 资源权限 */
	private java.lang.String resourceRights;
	/** 角色状态 */
	private java.lang.Integer roleStatus;
	/** 创建日期 */
	private java.util.Date createTime;
	/** 更新日期 */
	private java.util.Date updateTime;
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(StringUtils.isBlank(roleName)){
			 vb.setMsg("请填写角色名称！");
	         return vb;
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	//columns END
	public void setRoleId(java.lang.Integer value) {
		this.roleId = value;
	}
	
	public java.lang.Integer getRoleId() {
		return this.roleId;
	}
	public void setRoleName(java.lang.String value) {
		this.roleName = value;
	}
	
	public java.lang.String getRoleName() {
		return this.roleName;
	}
	public void setMenuRights(java.lang.String value) {
		this.menuRights = value;
	}
	
	public java.lang.String getMenuRights() {
		return this.menuRights;
	}
	public void setResourceRights(java.lang.String value) {
		this.resourceRights = value;
	}
	
	public java.lang.String getResourceRights() {
		return this.resourceRights;
	}
	public java.lang.Integer getRoleStatus() {
		return roleStatus;
	}
	public void setRoleStatus(java.lang.Integer roleStatus) {
		this.roleStatus = roleStatus;
	}
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}
	
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getRoleId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Roles == false) return false;
		if(this == obj) return true;
		Roles other = (Roles)obj;
		return new EqualsBuilder()
			.append(getRoleId(),other.getRoleId())
			.isEquals();
	}
	
}

