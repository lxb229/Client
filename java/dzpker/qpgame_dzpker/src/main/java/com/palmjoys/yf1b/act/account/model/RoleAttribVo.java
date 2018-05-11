package com.palmjoys.yf1b.act.account.model;

/**
 * 角色以属性数据
 * */
public class RoleAttribVo {
	//明星号
	public String starNO;
	//角色头像
	public String headImg;
	//性别(0=保密码,1=男,2=女)
	public int sex;
	//角色呢称
	public String nick;
	//帐号状态(0=正常,-1=冻结)
	public int state;
	//帐号注册时间
	public String createTime;
	//注册IP
	public String createIP;
	//最后在线时间
	public String inTime;
	//最后登录IP
	public String loginIP;
}
