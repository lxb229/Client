package com.palmjoys.yf1b.act.mail.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.mail.service.MailService;

@Component
public class MailFacadeImp implements MailFacade{

	@Autowired
	private MailService mailService;
	
	
	@Override
	public Object mail_MailList(Long accountId) {
		return mailService.mail_MailList(accountId);
	}

	@Override
	public Object mail_MailView(Long accountId, int mailId) {
		return mailService.mail_MailView(accountId, mailId);
	}
}
