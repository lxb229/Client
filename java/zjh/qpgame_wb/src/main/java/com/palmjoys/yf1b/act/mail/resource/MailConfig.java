package com.palmjoys.yf1b.act.mail.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("mail")
public class MailConfig {
	@ResourceId
	private int id;
	//最大邮件上限
	private int maxMail;
	//邮件保留时间(小时)
	private int deleteTime;
	
	public int getMaxMail() {
		return maxMail;
	}
	public void setMaxMail(int maxMail) {
		this.maxMail = maxMail;
	}
	public int getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(int deleteTime) {
		this.deleteTime = deleteTime;
	}

	
}
