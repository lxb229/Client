package com.palmjoys.yf1b.act.mail.model;

public class MailVo {
	//邮件Id
	public int mailId;
	//发送人帐号Id(0=系统邮件)
	public String senderId;
	//发送人呢称
	public String senderNickeName;
	//邮件标题
	public String title;
	//邮件内容
	public String content;
	//接收时间
	public String recvTime;
	
	public MailVo(){
		this.mailId = 0;
		this.senderId = "0";
		this.senderNickeName = "系统";
		this.title = "系统消息";
		this.content = "";
		this.recvTime = "0";
	}
}
