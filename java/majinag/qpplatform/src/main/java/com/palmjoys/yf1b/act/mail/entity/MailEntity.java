package com.palmjoys.yf1b.act.mail.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

import com.palmjoys.yf1b.act.mail.model.MailAttrib;

@Entity
@Memcached
public class MailEntity implements IEntity<Long>, Lifecycle{	
	@Id
	private Long accountId;
	//邮件数据Json
	@Lob
	@Column(nullable = false)
	private String mailListJson;
	@Transient
	private List<MailAttrib> mailList;
	//邮件同步数据锁
	@Transient
	private Lock _mailLock = new ReentrantLock();
	
	
	public static MailEntity valueOf(Long accountId){
		MailEntity retEntity = new MailEntity();
		retEntity.accountId = accountId;
		retEntity.mailListJson = null;
		retEntity.mailList = new ArrayList<MailAttrib>();
		
		return retEntity;
	}	
	
	public List<MailAttrib> getMailList() {
		return mailList;
	}

	@Enhance
	public void setMailList(List<MailAttrib> mailList) {
		this.mailList = mailList;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
		
	@Enhance
	public void addNewMail(MailAttrib mail, int maxNum){
		if(this.mailList.size()+1 > maxNum && this.mailList.size() > 0 && maxNum > 0){
			//邮件列表大于最大数量,先删除Id最小的
			int N = this.mailList.size()+1 - maxNum;
			for(int i=0; i<N; i++){
				MailAttrib findMail = null;
				for(MailAttrib mailAttrib : this.mailList){
					if(mailAttrib.read == 1){
						findMail = mailAttrib;
						break;
					}
				}
				if(null != findMail){
					this.mailList.remove(findMail);
				}else{
					this.mailList.remove(0);
				}
			}
		}
		
		int maxMailId = 0;
		for(MailAttrib mailAttrib : this.mailList){
			if(mailAttrib.mailId > maxMailId){
				maxMailId = mailAttrib.mailId;
			}
		}
		
		mail.mailId = maxMailId + 1;
		this.mailList.add(mail);
	}	
	
	@Enhance
	public void deleteAll(){
		mailList.clear();
	}
	
	public void lock(){
		_mailLock.lock();
	}
	
	public void unLock(){
		_mailLock.unlock();
	}
	
	@Override
	public Long getId() {
		return accountId;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(mailListJson)){
			mailList = new ArrayList<MailAttrib>();
		}else{
			mailList = JsonUtils.string2Collection(mailListJson, List.class, MailAttrib.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		mailListJson = JsonUtils.object2String(mailList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}
}
