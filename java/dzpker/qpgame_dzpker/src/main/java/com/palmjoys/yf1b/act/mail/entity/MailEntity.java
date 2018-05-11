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

	public MailAttrib getMail(int mailId){
		MailAttrib retMail = null;
		for(MailAttrib mail : mailList){
			if(mail.mailId == mailId){
				retMail = mail;
				break;
			}
		}
		
		return retMail;
	}
	
	@Enhance
	public boolean delete(int mailId){
		MailAttrib delObj = null;
		for(MailAttrib theMail : mailList){
			if(theMail.mailId == mailId){
				delObj = theMail;
				break;
			}
		}
		
		if(null != delObj){
			mailList.remove(delObj);
			return true;
		}
		return false;
	}
	
	@Enhance
	public void addNewMail(MailAttrib mail){
		if(mailList.isEmpty()){
			mail.mailId = 1;
			mailList.add(mail);
			return;
		}
		MailAttrib mailAttrib = mailList.get(mailList.size()-1);
		mail.mailId = mailAttrib.mailId + 1;
		mailList.add(mail);
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
