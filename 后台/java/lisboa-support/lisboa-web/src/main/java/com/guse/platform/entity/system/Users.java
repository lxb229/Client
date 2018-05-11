package com.guse.platform.entity.system;


import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.vo.system.MenusVo;




/**
 * tableName : system_users
 * @author nbin
 * @version 1.0
 */
public class Users  implements java.io.Serializable {
	
	private static final long serialVersionUID = 9039886636563940853L;
	//columns START
	/** userId */
	private java.lang.Integer userId;
	/** loginName */
	private java.lang.String loginName;
	/** loginPass */
	private java.lang.String loginPass;
	/** roleId */
	private java.lang.Integer roleId;
	/** userStatus */
	private java.lang.Integer userStatus;
	/** createTime */
	private java.util.Date createTime;
	/** updateTime */
	private java.util.Date updateTime;
	//columns END
	
	/** 菜单*/
	private List<MenusVo> menuList;	
	/** 用户菜单列表 */
	private List<Menus> menusList;
	/** 用户资源列表 */
	private List<Resource> resourceList;
	
	public List<Resource> getResourceList() {
		return resourceList;
	}
	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}
	public List<Menus> getMenusList() {
		return menusList;
	}
	public void setMenusList(List<Menus> menusList) {
		this.menusList = menusList;
	}
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		if(userId == null){
			 if(StringUtils.isBlank(loginName)){
				 vb.setMsg("请填写登录名！");
		         return vb;
			 }
		}
		vb.setFlag(true);
	    return vb; 
	}
	public List<MenusVo> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<MenusVo> menuList) {
		this.menuList = menuList;
	}
	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}
	
	public java.lang.Integer getUserId() {
		return this.userId;
	}
	public void setLoginName(java.lang.String loginName) {
		this.loginName = loginName;
	}
	public java.lang.String getLoginName() {
		return this.loginName;
	}
	public void setLoginPass(java.lang.String loginPass) {
		this.loginPass = loginPass;
	}
	
	public java.lang.String getLoginPass() {
		return this.loginPass;
	}
	public void setRoleId(java.lang.Integer roleId) {
		this.roleId = roleId;
	}
	
	public java.lang.Integer getRoleId() {
		return this.roleId;
	}
	public void setUserStatus(java.lang.Integer userStatus) {
		this.userStatus = userStatus;
	}
	
	public java.lang.Integer getUserStatus() {
		return this.userStatus;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
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
			.append(getUserId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Users == false) return false;
		if(this == obj) return true;
		Users other = (Users)obj;
		return new EqualsBuilder()
			.append(getUserId(),other.getUserId())
			.isEquals();
	}

}

