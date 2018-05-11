package com.palmjoys.yf1b.act.mail.model;

public class MailAttrib {
	//邮件Id
	public int mailId;
	//发件Id(-1=系统邮件)
	public long sender;
	//邮件标题
	public String title;
	//邮件内容
	public String content;
	//邮件接收时间
	public long recvTime;
}
