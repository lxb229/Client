package com.palmjoys.yf1b.act.mail.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.mail.model.MailDefine;

@NetworkFacade
public interface MailFacade {

	@NetworkApi(value = MailDefine.MAIL_COMMAND_MAILLIST,
			desc="获取所有邮件列表")
	Object mail_get_mail_List(@InSession Long accountId);
	
	@NetworkApi(value = MailDefine.MAIL_COMMAND_VIEW,
			desc="查看邮件")
	Object mail_view_mail_item(@InSession Long accountId,
			@InBody(value = "mailId", desc = "邮件唯一Id") int mailId);	
}
