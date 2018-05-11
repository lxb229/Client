package com.palmjoys.yf1b.act.mail.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;
import com.palmjoys.yf1b.act.mail.model.MailDetailVo;
import com.palmjoys.yf1b.act.mail.model.MailVo;

@Service
public class MailServiceImp implements MailService{
	@Autowired
	private MailManager mailManager;
	@Autowired
	private HotPromptManager htoPromptManager;
	@Autowired
	private AccountManager accountManager;
	
	@Override
	public Object mail_maillist(Long accountId) {
		MailVo retVo = new MailVo();
		MailEntity mailEntity = mailManager.loadOrCreate(accountId);
		int limitType = mailManager.getLimitType();
		long vaildTime = mailManager.getDeleteTime();
		int limitMaxNum = mailManager.getMailMaxCfg();
		mailEntity.lock();
		try{
			long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
			List<MailAttrib> mails = mailEntity.getMailList();
			int N = mails.size() - limitMaxNum;
			if(limitType == 1){
				//数量条件限制,删除已查看
				for(int i=0; i<N; i++){
					MailAttrib findMail = null;
					for(MailAttrib mailAttrib : mails){
						if(mailAttrib.read == 1){
							findMail = mailAttrib;
							break;
						}
					}
					if(null != findMail){
						mails.remove(findMail);
					}else{
						mails.remove(0);
					}
				}
			}else if(limitType == 2){
				//过期时间限制
				for(int i=0; i<N; i++){
					MailAttrib findMail = null;
					for(MailAttrib mailAttrib : mails){
						long endTime = mailAttrib.recvTime + vaildTime;
						if(currTime >= endTime){
							//此邮件是已过期了的,自动删除
							findMail = mailAttrib;
							break;
						}
					}
					if(null != findMail){
						mails.remove(findMail);
					}else{
						mails.remove(0);
					}
				}
			}else{
				for(int i=0; i<N; i++){
					MailAttrib findMail = null;
					for(MailAttrib mailAttrib : mails){
						long endTime = mailAttrib.recvTime + vaildTime;
						if(mailAttrib.read == 1 && currTime >= endTime){
							findMail = mailAttrib;
							break;
						}
					}
					if(null != findMail){
						mails.remove(findMail);
					}else{
						mails.remove(0);
					}
				}
			}
			for(MailAttrib mailAttrib : mails){
				retVo.addItem(mailAttrib);
			}			
			mailEntity.setMailList(mails);
		}finally{
			mailEntity.unLock();
		}
		
		retVo.sort();
		return Result.valueOfSuccess(retVo);
	}
	@Override
	public Object mail_mailview(Long accountId, int mailId) {
		String err = "接口参数错误";
		MailDetailVo retVo = new MailDetailVo();
		MailEntity mailEntity = mailManager.loadOrCreate(accountId);
		mailEntity.lock();
		try{
			do{
				MailAttrib findMail = null;
				List<MailAttrib> mailList = mailEntity.getMailList();
				for(MailAttrib mailAttrib : mailList){
					if(mailAttrib.mailId == mailId){
						findMail = mailAttrib;
						break;
					}
				}
				if(null == findMail){
					err = "邮件不存在";
					break;
				}
				retVo.attachmentState = 0;
				long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				if(currTime >= findMail.attachmentVaildTime){
					retVo.attachmentState = -1;
				}
				
				retVo.mailId = findMail.mailId;
				retVo.content = findMail.content;
				retVo.attachment = findMail.attachment;
				retVo.attachmentData = findMail.attachmentData;
				retVo.attachmentGetState = findMail.attachmentGetState;
				
				findMail.read = 1;
				mailEntity.setMailList(mailList);
				
				err = "";
			}while(false);
		}finally{
			mailEntity.unLock();
		}		
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		if(htoPromptManager.updateHotPrompt(accountId, HotPromptDefine.HOT_KEY_MAIL) > 0){
			htoPromptManager.notifyHotPrompt(accountId);
		}
		return Result.valueOfSuccess(retVo);
	}
	@Override
	public Object mail_maildelete(Long accountId, int[] mailIds) {
		if(null == mailIds || mailIds.length == 0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		MailEntity mailEntity = mailManager.loadOrCreate(accountId);
		mailEntity.lock();
		try{
			List<MailAttrib> findMails = new ArrayList<>();
			List<MailAttrib> mailList = mailEntity.getMailList();
			for(int mailId : mailIds){
				for(MailAttrib mailAttrib : mailList){
					if(mailAttrib.mailId == mailId){
						findMails.add(mailAttrib);
					}
				}
			}
			
			//删除找到的邮件
			for(MailAttrib mailAttrib : findMails){
				mailList.remove(mailAttrib);
			}
			mailEntity.setMailList(mailList);
			
		}finally{
			mailEntity.unLock();
		}
				
		if(htoPromptManager.updateHotPrompt(accountId, HotPromptDefine.HOT_KEY_MAIL) > 0){
			htoPromptManager.notifyHotPrompt(accountId);
		}
		
		return this.mail_maillist(accountId);
	}
	
	
	@Override
	public Object mail_mailsend(Long accountId, String title, String content, String starNO, GameObject[] attachments) {
		if(title == null || title.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "邮件标题不能为空", null);
		}
		if(content==null || content.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "邮件内容不能为空", null);
		}
		
		if(null == starNO || starNO.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "未找到指定邮件接收玩家", null);
		}
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "未找到指定邮件接收玩家", null);
		}
						
		MailEntity mailEntity = mailManager.loadOrCreate(accountEntity.getId());
		mailEntity.lock();
		try{
			MailAttrib mailAttrib = new MailAttrib();
			mailAttrib.sender = accountId;
			mailAttrib.title = title;
			mailAttrib.content = content;
			mailAttrib.recvTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
			mailAttrib.read = 0;
			if(null != attachments){
				for(GameObject gameObject : attachments){
					mailAttrib.attachment.add(gameObject);
				}
			}
			
			mailEntity.addNewMail(mailAttrib, mailManager.getMailMaxCfg());
			
		}finally{
			mailEntity.unLock();
		}
		
		if(htoPromptManager.updateHotPrompt(accountEntity.getId(), HotPromptDefine.HOT_KEY_MAIL) > 0){
			htoPromptManager.notifyHotPrompt(accountEntity.getId());
		}
		return Result.valueOfSuccess();
	}
	@Override
	public Object mail_get_attach(Long accountId, int mailId, String param) {
		if(null == param || param.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		MailEntity mailEntity = mailManager.loadOrCreate(accountId);
		mailEntity.lock();
		try{
			do{
				MailAttrib findmail = null;
				
				List<MailAttrib> mailList = mailEntity.getMailList();
				for(MailAttrib mail : mailList){
					if(mail.mailId == mailId){
						findmail = mail;
						break;
					}
				}
				if(null == findmail){
					err = "邮件不存在";
					break;
				}
				if(null == findmail.attachment || findmail.attachment.isEmpty()){
					err = "邮件无附件可领取";
					break;
				}
				long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				if(currTime >= findmail.attachmentVaildTime){
					err = "邮件附件已失效,无法领取";
					break;
				}
				
				if(findmail.attachmentGetState != 0){
					err = "邮件附件已领取";
					break;
				}
				if(null == findmail.attachmentData){
					err = "邮件无附件可领取";
					break;
				}
				
				//邮件附件物品一定是游戏外物品,调用后台接口领取
				String url = mailManager.getAttachRewardAddr();
				Object []params = new Object[2];
				params[0] = findmail.attachmentData;
				params[1] = param;
				
				String sParams = JsonUtils.object2String(params);
				String retStr = HttpClientUtils.executeByPost(url, sParams);
				if(null == retStr || retStr.isEmpty()){
					err = "邮件物品领取失败";
					break;
				}
				
				//领取成功
				findmail.attachmentGetState = 1;
				mailEntity.setMailList(mailList);
				
				err = "";
			}while(false);
		}finally{
			mailEntity.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}				
		
		return Result.valueOfSuccess();
	}
		
	
}
