package com.palmjoys.yf1b.act.corps.model;

public class CorpsTablePlayerAttribVo {
	//帐号Id(0=无人座位)
	public String accountId;
	//呢称
	public String nick;
	//头像
	public String headImg;
	//网络ping值
	public int pingVal;
	
	public CorpsTablePlayerAttribVo(){
		this.accountId = "0";
		this.nick = "";
		this.headImg = "";
		this.pingVal = 0;
	}
}
