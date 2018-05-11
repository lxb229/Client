package com.palmjoys.yf1b.act.framework.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

@Entity
@Memcached
public class LoginEntity implements IEntity<Long>{
	//帐号唯-Id
	@Id
	private Long accountId;
	
	@Column(nullable = false)
	private long inTime;
	
	@Column(nullable = false)
	private long outTime;
	
	@Column(nullable = false)
	private String loginIP;
	
	@Override
	public Long getId() {
		return accountId;
	}

	public static LoginEntity valueOf(long accountId){
		LoginEntity retEntity = new LoginEntity();
		retEntity.accountId = accountId;
		retEntity.loginIP = "unkown";
		retEntity.inTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		retEntity.outTime = retEntity.inTime;
		return retEntity;
	}

	public String getLoginIP() {
		return loginIP;
	}

	@Enhance
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public long getInTime() {
		return inTime;
	}

	@Enhance
	public void setInTime(long inTime) {
		this.inTime = inTime;
	}

	public long getOutTime() {
		return outTime;
	}

	@Enhance
	public void setOutTime(long outTime) {
		this.outTime = outTime;
	}
	
}
