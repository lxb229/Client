package com.palmjoys.yf1b.act.mail.service;

public interface MailService {

	/**
	 * 获取所有邮件列表
	 * accountId 帐号Id
	 * */
	public Object mail_MailList(Long accountId);
	
	/**
	 * 查看邮件
	 * accountId 帐号Id
	 * mailId 邮件Id
	 * */
	public Object mail_MailView(Long accountId, int mailId);
}
