package com.palmjoys.yf1b.act.mail.service;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;

public interface MailService {

	//获取所有邮件列表
	public Object mail_maillist(Long accountId);
	//查看邮件
	public Object mail_mailview(Long accountId, int mailId);
	//删除邮件
	public Object mail_maildelete(Long accountId, int[] mailIds);
	//发送邮件
	public Object mail_mailsend(Long accountId, String title, String content, String starNO, GameObject[] attachments);
	//领取邮件附件
	public Object mail_get_attach(Long accountId, int mailId, String param);
}
