package com.palmjoys.yf1b.act.mail.service;

public interface MailService {

	/**
	 * 获取所有邮件列表
	 * accountId 帐号Id
	 * */
	public Object mail_get_mail_List(Long accountId);
	
	/**
	 * 查看邮件
	 * accountId 帐号Id
	 * mailId 邮件Id
	 * */
	public Object mail_view_mail_item(Long accountId, int mailId);
}
