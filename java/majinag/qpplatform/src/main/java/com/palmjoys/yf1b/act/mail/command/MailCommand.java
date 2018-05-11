package com.palmjoys.yf1b.act.mail.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

@Component
@ConsoleBean
public class MailCommand {
	@Autowired
	private MailManager mailManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private AccountManager accountManager;
	
	/**
	 * GM后台发送邮件给玩家
	 * title 邮件标题
	 * content 邮件内容
	 * recvPlayers 接收的玩家帐号列表
	 * */
	@ConsoleCommand(name = "gm_Mail_SendSystemMail", description = "GM发送邮件给玩家")
	public Object gm_Mail_SendSystemMail(String title, String content, String []recvPlayers){
		if(title.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "邮件标题不能为空", null);
		}
		if(content.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "邮件内容不能为空", null);
		}
		
		if(null == recvPlayers || recvPlayers.length == 0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "邮件接收的玩家不能为空", null);
		}
		try {
			byte []dBytes = Base64.decodeBase64(content);
			content = new String(dBytes, "UTF-8");
		} catch (Exception e) {
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "邮件内容解析错误", null);
		}
		
		List<String> unFindStarNOs = new ArrayList<>();
		List<Long> recvAccountIds = new ArrayList<>();
		for(String starNO : recvPlayers){
			AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
			if(null != accountEntity){
				if(recvAccountIds.contains(accountEntity.getId()) == false){
					recvAccountIds.add(accountEntity.getId());
				}
			}else{
				unFindStarNOs.add(starNO);
			}
		}
		if(unFindStarNOs.isEmpty() == false){
			String err = "未找到指定以下明星号的玩家: ";
			for(String starNO : unFindStarNOs){
				err += starNO;
				err += " ";
			}
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		//检查替换屏蔽词
		for(long theAccountId : recvAccountIds){
			MailEntity entry = mailManager.loadOrCreate(theAccountId);
			entry.lock();
			try{
				MailAttrib mailAttrib = new MailAttrib();
				mailAttrib.sender = 0;
				mailAttrib.title = title;
				mailAttrib.content = content;
				mailAttrib.recvTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				mailAttrib.read = 0;
				
				entry.addNewMail(mailAttrib, mailManager.getMailMaxCfg());
			}finally{
				entry.unLock();
			}
			//检查推送红点消息
			if(hotPromptManager.updateHotPrompt(theAccountId, HotPromptDefine.HOT_KEY_MAIL) > 0){
				hotPromptManager.notifyHotPrompt(theAccountId);
			}
		}
		
		return Result.valueOfSuccess();
	}
}
