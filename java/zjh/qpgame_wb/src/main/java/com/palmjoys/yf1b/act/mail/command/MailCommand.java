package com.palmjoys.yf1b.act.mail.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;
import com.palmjoys.yf1b.act.mail.model.MailDefine;
import org.apache.commons.codec.binary.Base64;

@Component
@ConsoleBean
public class MailCommand {
	@Autowired
	private MailManager mailManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	
	/**
	 * GM后台发送邮件给玩家
	 * title 邮件标题
	 * content 邮件内容
	 * recvPlayers 接收的玩家帐号列表
	 * */
	@ConsoleCommand(name = "gm_Mail_SendSystemMail", description = "GM发送邮件给玩家")
	public Object gm_Mail_SendSystemMail(String title, String content, String []recvPlayers){
		if(title.isEmpty()){
			return Result.valueOfError(MailDefine.MAIL_ERROR_TITLE_EMPTY);
		}
		if(content.isEmpty()){
			return Result.valueOfError(MailDefine.MAIL_ERROR_COMMENT_EMPTY);
		}
		
		if(null == recvPlayers || recvPlayers.length == 0){
			return Result.valueOfError(MailDefine.MAIL_ERROR_RECVUSER_UNEXIST);
		}
		try {
			byte []dBytes = Base64.decodeBase64(content);
			content = new String(dBytes, "UTF-8");
		} catch (Exception e) {
		}
		//检查替换屏蔽词
		for(String sAccountId : recvPlayers){
			Long theAccountId = Long.valueOf(sAccountId);
			MailEntity entry = mailManager.loadOrCreate(theAccountId);
			entry.lock();
			try{
				MailAttrib mailAttrib = new MailAttrib();
				mailAttrib.content = content;
				mailAttrib.recvTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				mailAttrib.sender = 0;
				mailAttrib.title = title;
				entry.addNewMail(mailAttrib);
			}finally{
				entry.unLock();
			}
			//检查推送红点消息
			hotPromptManager.checkNotifyHotPrompt(theAccountId, false);
		}
		
		return Result.valueOfSuccess();
	}
}
