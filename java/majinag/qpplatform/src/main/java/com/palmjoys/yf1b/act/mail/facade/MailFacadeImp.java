package com.palmjoys.yf1b.act.mail.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.mail.service.MailService;

@Component
public class MailFacadeImp implements MailFacade{

	@Autowired
	private MailService mailService;

	@Override
	public Object mail_maillist(Long accountId) {
		return mailService.mail_maillist(accountId);
	}

	@Override
	public Object mail_mailview(Long accountId, int mailId) {
		return mailService.mail_mailview(accountId, mailId);
	}

	@Override
	public Object mail_maildelete(Long accountId, int[] mailIds) {
		return mailService.mail_maildelete(accountId, mailIds);
	}

	@Override
	public Object mail_mailsend(Long accountId, String title, String content, String starNO, GameObject[] attachments) {
		return mailService.mail_mailsend(accountId, title, content, starNO, attachments);
	}

	@Override
	public Object mail_get_attach(Long accountId, int mailId, String param) {
		return mailService.mail_get_attach(accountId, mailId, param);
	}
	
	
	
}
