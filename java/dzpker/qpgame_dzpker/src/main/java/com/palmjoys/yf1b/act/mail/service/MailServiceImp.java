package com.palmjoys.yf1b.act.mail.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.framework.common.manager.ErrorCodeManager;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;
import com.palmjoys.yf1b.act.mail.model.MailDefine;
import com.palmjoys.yf1b.act.mail.model.MailVo;

@Service
public class MailServiceImp implements MailService{
	@Autowired
	private MailManager mailManager;
	@Autowired
	private ErrorCodeManager errorCodeManager;
	@Autowired
	private HotPromptManager htoPromptManager;
		
	@Override
	public Object mail_get_mail_List(Long accountId) {
		List<MailVo> retVo = mailManager.getMailList(accountId);
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object mail_view_mail_item(Long accountId, int mailId) {
		MailEntity entry = mailManager.loadOrCreate(accountId);
		MailAttrib mailAttrib = entry.getMail(mailId);
		if(mailAttrib == null){
			return Result.valueOfError(MailDefine.MAIL_ERROR_UNEXIST,
					errorCodeManager.Error2Desc(MailDefine.MAIL_ERROR_UNEXIST), null);
		}
		entry.delete(mailId);
		
		//检查推送红点消息
		htoPromptManager.updateHotPrompt(accountId, HotPromptManager.HOT_KEY_MAIL);
		htoPromptManager.hotPromptNotity(accountId);
		
		return mail_get_mail_List(accountId);
	}	
}
