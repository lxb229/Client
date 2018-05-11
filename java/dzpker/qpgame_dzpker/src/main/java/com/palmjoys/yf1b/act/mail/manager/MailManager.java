package com.palmjoys.yf1b.act.mail.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;
import com.palmjoys.yf1b.act.mail.model.MailVo;
import com.palmjoys.yf1b.act.mail.resource.MailConfig;

@Component
public class MailManager {
	@Inject
	private EntityMemcache<Long, MailEntity> mailEntityCache;
	@Autowired
	private RoleEntityManager roleEntityManager;
	
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
	
	/**
	 * 获取邮件,按未读,接收时间排序
	 * */
	public List<MailVo> getMailList(Long accountId){
		List<MailVo> retVo = new ArrayList<MailVo>();
		MailEntity mailEntity = this.loadOrCreate(accountId);
		List<MailAttrib> mails = mailEntity.getMailList();
		mails.sort(new Comparator<MailAttrib>(){
			@Override
			public int compare(MailAttrib o1, MailAttrib o2) {
				//接收时间较新的排前面				
				if(o1.recvTime > o2.recvTime){
					return -1;
				}else{
					if(o1.recvTime < o2.recvTime){
						return 1; 
					}else{
						return 0;
					}
				}
			}
		});
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		
		List<MailAttrib> dels = new ArrayList<MailAttrib>();
		for(MailAttrib mailAttrib : mails){
			long endTime = mailAttrib.recvTime + getDeleteTime();
			if(currTime >= endTime){
				//此邮件是已过期了的,自动删除
				dels.add(mailAttrib);
				continue;
			}
			
			MailVo obj = new MailVo();
			obj.mailId = mailAttrib.mailId;
			obj.recvTime = String.valueOf(mailAttrib.recvTime);
			obj.senderId = String.valueOf(mailAttrib.sender);
			obj.content = mailAttrib.content;
			obj.title = mailAttrib.title;
			if(mailAttrib.sender <= 0){
				obj.senderNickeName = "系统";
			}else{
				RoleEntity entry = roleEntityManager.findOf_accountId(mailAttrib.sender);
				if(entry != null){
					obj.senderNickeName = entry.getNick();
				}
			}
			retVo.add(obj);
		}
		
		for(MailAttrib mailAttrib : dels){
			mails.remove(mailAttrib);
		}
		dels = null;
		mailEntity.setMailList(mails);
		
		return retVo;
	}
	
	/**
	 * 检查邮件红点
	 * return 0=无,1=有
	 * */
	public int checkHotPrompt(long accountId){
		if(accountId <= 0)
			return 0;
		MailEntity mailEntity = this.loadOrCreate(accountId);
		if(mailEntity.getMailList().isEmpty()){
			return 0;
		}
		return 1;
	}	
}
