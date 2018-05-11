package com.guse.platform.entity.system;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.entity.doudou.City;
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
	/** createTime */
	private java.util.Date createTime;
	/** updateTime */
	private java.util.Date updateTime;
	/**帐号唯-Id*/
	private java.lang.Long accountId;
	/** star_no */
	private java.lang.String starNo;
	/** 角色头像 */
	private java.lang.String headImg;
	/** 性别(0=保密码,1=男,2=女) */
	private java.lang.Integer sex;
	/** 角色呢称 */
	private java.lang.String nick;
	/** 帐号状态(1正常,0=冻结) */
	private java.lang.Integer userStatus;
	/** 注册IP */
	private java.lang.String createIp;
	/** 帐号类型(0=一般玩家,1=代理)  */
	private java.lang.Integer accountType;
	/** 地区 */
	private java.lang.Integer area;
	/**
	 * 电话
	 */
	private java.lang.String phone;
	/**
	 * 真实姓名
	 */
	private java.lang.String realName;
	/**
	 * 身份证号码
	 */
	private java.lang.String cardNo;
	/** 验证码 */
	private java.lang.String verCode;
	/** 微信号码*/
	private java.lang.String wxNo;
	/** 验证码发送时间 */
	private java.util.Date sendTime;
	//columns END
	
	/** 菜单*/
	private List<MenusVo> menuList;	
	/** 用户菜单列表 */
	private List<Menus> menusList;
	/** 用户资源列表 */
	private List<Resource> resourceList;
	/** 所属区域 */
	private City city;
	
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
	
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		if(userId == null){
			 if(StringUtils.isBlank(nick)){
				 vb.setMsg("请填写昵称！");
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
	
	public java.lang.String getStarNo() {
		return starNo;
	}
	public void setStarNo(java.lang.String starNo) {
		this.starNo = starNo;
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

	
	public java.lang.String getPhone() {
		return phone;
	}
	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}
	public java.lang.String getRealName() {
		return realName;
	}
	public void setRealName(java.lang.String realName) {
		this.realName = realName;
	}
	public java.lang.String getCardNo() {
		return cardNo;
	}
	public void setCardNo(java.lang.String cardNo) {
		this.cardNo = cardNo;
	}
	
	public java.lang.Long getAccountId() {
		return accountId;
	}
	public void setAccountId(java.lang.Long accountId) {
		this.accountId = accountId;
	}
	public java.lang.String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(java.lang.String headImg) {
		this.headImg = headImg;
	}
	public java.lang.Integer getSex() {
		return sex;
	}
	public void setSex(java.lang.Integer sex) {
		this.sex = sex;
	}
	public java.lang.String getNick() {
		return nick;
	}
	public void setNick(java.lang.String nick) {
		this.nick = nick;
	}
	public java.lang.String getCreateIp() {
		return createIp;
	}
	public void setCreateIp(java.lang.String createIp) {
		this.createIp = createIp;
	}
	public java.lang.Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(java.lang.Integer accountType) {
		this.accountType = accountType;
	}
	public java.lang.Integer getArea() {
		return area;
	}
	public void setArea(java.lang.Integer area) {
		this.area = area;
	}
	
	public java.lang.String getVerCode() {
		return verCode;
	}
	public void setVerCode(java.lang.String verCode) {
		this.verCode = verCode;
	}
	public java.lang.String getWxNo() {
		return wxNo;
	}
	public void setWxNo(java.lang.String wxNo) {
		this.wxNo = wxNo;
	}
	public java.util.Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(java.util.Date sendTime) {
		this.sendTime = sendTime;
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

