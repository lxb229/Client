package com.guse.platform.entity.system;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import com.guse.platform.common.base.ValidataBean;


/**
 * 
 * tableName : system_menus
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class Menus implements java.io.Serializable {
	
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
	
	private Menus childMenus;//下级菜单
	
	
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(pid == null){
			 vb.setMsg("请选择父级菜单！");
	         return vb;
		 }
		 if(StringUtils.isBlank(menuTitle)){
			 vb.setMsg("请填写菜单标题！");
	         return vb;
		 }
		 if(menuLevel.intValue() == 3){
			 if(StringUtils.isBlank(menuIcon)){
				 vb.setMsg("请设置菜单ICON！");
				 return vb;
			 }
			 if(StringUtils.isBlank(menuUrl)){
				 vb.setMsg("请设置菜单URL！");
				 return vb;
			 }
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	
	
	public Menus(){}
	
	public Menus(Integer menuId,Integer pid,Integer sort){
		this.menuId = menuId;
		this.pid = pid;
		this.menuSort = sort;
	}
	
	public Menus getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(Menus childMenus) {
		this.childMenus = childMenus;
	}

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
		if(obj instanceof Menus == false) return false;
		if(this == obj) return true;
		Menus other = (Menus)obj;
		return new EqualsBuilder()
			.append(getMenuId(),other.getMenuId())
			.isEquals();
	}
	
}

