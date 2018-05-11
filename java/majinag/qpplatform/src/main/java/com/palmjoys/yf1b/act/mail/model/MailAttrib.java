package com.palmjoys.yf1b.act.mail.model;

import java.util.ArrayList;
import java.util.List;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;

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
	//邮件是否已读(0=未读,1=已读)
	public int read;
	//邮件附件
	public List<GameObject> attachment;
	//邮件附件相关数据
	public String attachmentData;
	//邮件附件过期时间
	public long attachmentVaildTime;
	//邮件附件是否已领取(0=未领取,1=已领取)
	public int attachmentGetState;
	
	public MailAttrib(){
		this.mailId = 0;
		this.sender = 0;
		this.title = "";
		this.content = "";
		this.recvTime = 0;
		this.read = 0;
		this.attachment = new ArrayList<>();
		this.attachmentData = "";
		this.attachmentVaildTime = 0;
		this.attachmentGetState = 0;
	}
}
