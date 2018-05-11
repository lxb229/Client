package com.guse.platform.vo.system;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;



/**
 * MenusVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class MenusVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** menuId */
	private java.lang.Integer menuId;
	/** 菜单标题 */
	private java.lang.String menuTitle;
	/** 父级菜单id */
	private java.lang.Integer pid;
	/** 菜单图标 */
	private java.lang.String menuIcon;
	/** 排序 */
	private java.lang.Integer menuSort;
	/** 菜单路径 */
	private java.lang.String menuUrl;
	/** 菜单层级 */
	private java.lang.Integer menuLevel;
	/** 菜单状态 */
	private java.lang.Integer menuStatus;
	/** 创建日期 */
	private java.util.Date createTime;
	/** 更新日期 */
	private java.util.Date updateTime;
	//columns END
	
	private List<MenusVo> childMenus;//下级菜单
	
	private String menuNode;//菜单节点
	
	public void setMenuId(java.lang.Integer value) {
		this.menuId = value;
	}
	
	public java.lang.Integer getMenuId() {
		return this.menuId;
	}
	public void setMenuTitle(java.lang.String value) {
		this.menuTitle = value;
	}
	
	public java.lang.String getMenuTitle() {
		return this.menuTitle;
	}
	public void setPid(java.lang.Integer value) {
		this.pid = value;
	}
	
	public java.lang.Integer getPid() {
		return this.pid;
	}
	public void setMenuIcon(java.lang.String value) {
		this.menuIcon = value;
	}
	
	public java.lang.String getMenuIcon() {
		return this.menuIcon;
	}
	public void setMenuSort(java.lang.Integer value) {
		this.menuSort = value;
	}
	
	public java.lang.Integer getMenuSort() {
		return this.menuSort;
	}
	public void setMenuUrl(java.lang.String value) {
		this.menuUrl = value;
	}
	
	public java.lang.String getMenuUrl() {
		return this.menuUrl;
	}
	
	public java.lang.Integer getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(java.lang.Integer menuLevel) {
		this.menuLevel = menuLevel;
	}
	public java.lang.Integer getMenuStatus() {
		return menuStatus;
	}
	public void setMenuStatus(java.lang.Integer menuStatus) {
		this.menuStatus = menuStatus;
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
	public List<MenusVo> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<MenusVo> childMenus) {
		this.childMenus = childMenus;
	}

	public String getMenuNode() {
		return menuNode;
	}

	public void setMenuNode(String menuNode) {
		this.menuNode = menuNode;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getMenuId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MenusVo == false) return false;
		if(this == obj) return true;
		MenusVo other = (MenusVo)obj;
		return new EqualsBuilder()
			.append(getMenuId(),other.getMenuId())
			.isEquals();
	}
	
}

