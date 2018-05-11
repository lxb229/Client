package com.palmjoys.yf1b.act.mail.manager;

import java.util.List;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;
import com.palmjoys.yf1b.act.mail.resource.MailConfig;

@Component
public class MailManager {
	@Inject
	private EntityMemcache<Long, MailEntity> mailEntityCache;
	@Static
	private Storage<Integer, MailConfig> mailCfgs;
		
	public MailEntity loadOrCreate(Long accountId){
		return mailEntityCache.loadOrCreate(accountId, new EntityBuilder<Long, MailEntity>(){

			@Override
			public MailEntity createInstance(Long pk) {
				return MailEntity.valueOf(accountId);
			}
		});
	}
	
	public void clearOfCache(long accountId){
		mailEntityCache.clear(accountId);
	}
	
	public int getMailMaxCfg(){
		return mailCfgs.get(1, false).getMaxMail();
	}
	
	public long getDeleteTime(){
		int time = mailCfgs.get(1, false).getDeleteTime();
		long retTime = time*60*60;
		retTime = retTime*1000;
		return retTime;
	}
	
	public int getLimitType(){
		return mailCfgs.get(1, false).getLimitType();
	}
	
	public String getAttachRewardAddr(){
		return mailCfgs.get(1, false).getAttachRewardAddr();
	}
	
	/**
	 * 检查邮件红点
	 * return 0=无,1=有
	 * */
	public int checkHotPrompt(long accountId){
		if(accountId <= 0)
			return 0;
		int ret = 0;
		MailEntity mailEntity = this.loadOrCreate(accountId);
		mailEntity.lock();
		try{
			List<MailAttrib> mails = mailEntity.getMailList();
			for(MailAttrib mail : mails){
				if(mail.read == 0){
					ret = 1;
				}
			}
		}finally{
			mailEntity.unLock();
		}
		
		return ret;
	}	
}
