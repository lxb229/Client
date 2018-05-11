package com.palmjoys.yf1b.act.mail.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.mail.service.MailService;

@Component
public class MailFacadeImp implements MailFacade{

	@Autowired
	private MailService mailService;
	
	
	@Override
	public Object mail_get_mail_List(Long accountId) {
		return mailService.mail_get_mail_List(accountId);
	}

	@Override
	public Object mail_view_mail_item(Long accountId, int mailId) {
		return mailService.mail_view_mail_item(accountId, mailId);
	}
}
