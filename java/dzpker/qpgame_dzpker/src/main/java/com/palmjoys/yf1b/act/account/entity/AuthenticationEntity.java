package com.palmjoys.yf1b.act.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

@Entity
@Memcached
public class AuthenticationEntity implements IEntity<Long>{
	@Id
	private long accountId;
	//姓名
	@Column(nullable = false)
	private String name;
	//身份证号
	@Column(nullable = false)
	private String cardId;
	//帮定的手机号
	@Column(nullable = false)
	private String phone;
	
	public static AuthenticationEntity valueOf(long accountId){
		AuthenticationEntity retEntity = new AuthenticationEntity();
		retEntity.accountId = accountId;
		retEntity.name = "";
		retEntity.cardId = "";
		retEntity.phone = "";
		return retEntity;
	}
		
	@Override
	public Long getId() {
		return accountId;
	}

	public String getName() {
		return name;
	}

	@Enhance
	public void setName(String name) {
		this.name = name;
	}

	public String getCardId() {
		return cardId;
	}

	@Enhance
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
	public String getPhone() {
		return phone;
	}

	@Enhance
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
