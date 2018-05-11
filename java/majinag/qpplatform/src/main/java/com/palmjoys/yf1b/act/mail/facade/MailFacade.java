package com.palmjoys.yf1b.act.mail.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.mail.model.MailDefine;

@NetworkFacade
public interface MailFacade {

	@NetworkApi(value = MailDefine.MAIL_COMMAND_MAILLIST,
			desc="获取所有邮件列表")
	Object mail_maillist(@InSession Long accountId);
	
	@NetworkApi(value = MailDefine.MAIL_COMMAND_VIEW,
			desc="查看邮件")
	Object mail_mailview(@InSession Long accountId,
			@InBody(value = "mailId", desc = "邮件唯一Id") int mailId);	
	
	@NetworkApi(value = MailDefine.MAIL_COMMAND_DELETE,
			desc="删除邮件")
	Object mail_maildelete(@InSession Long accountId,
			@InBody(value = "mailIds", desc = "邮件Id列表") int []mailIds);
	
	@NetworkApi(value = MailDefine.MAIL_COMMAND_SEND,
			desc="发送邮件")
	Object mail_mailsend(@InSession Long accountId,
			@InBody(value = "title", desc = "邮件标题") String title,
			@InBody(value = "content", desc = "邮件内容") String content,
			@InBody(value = "starNO", desc = "邮件接收人") String starNO,
			@InBody(value = "attachments", desc = "邮件附件列表") GameObject []attachments);
	
	@NetworkApi(value = MailDefine.MAIL_COMMAND_GET_ATTACH,
			desc="领取邮件附件")
	Object mail_get_attach(@InSession Long accountId,
			@InBody(value = "mailId", desc = "邮件唯一Id") int mailId,
			@InBody(value = "param", desc = "领取参数") String param);	
}
