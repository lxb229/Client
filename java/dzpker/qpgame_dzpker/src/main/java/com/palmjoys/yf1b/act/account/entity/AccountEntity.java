package com.palmjoys.yf1b.act.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

/**
 * 平台帐号实体类
 * */
@Entity
@Memcached
@NamedQueries({
	@NamedQuery(name = AccountEntity.ACCOUNT_MAXID, query = "SELECT MAX(A.accountId)"
			+ " FROM AccountEntity AS A WHERE A.accountId BETWEEN ? AND ?")
	})
public class AccountEntity implements IEntity<Long>{
	//帐号表最大Id号
	public static final String ACCOUNT_MAXID = "account_maxid";	
	
	//帐号唯-Id
	@Id
	private Long accountId;
	//微信或游客身份唯一Id(密码登录就是帐号)
	@Column(nullable = false)
	private String uuid;
	//帐号密码
	@Column(nullable = false)
	private String password;
	//帐号状态(0=正常,-1=冻结)
	@Column(nullable = false)
	private int state;
	//帐号注册时间
	@Column(nullable = false)
	private long createTime;
	//注册IP
	@Column(nullable = false)
	private String createIP;
	//帐号类型(0=一般玩家,1=代理)
	@Column(nullable = false)
	private int accountType;	
	//登录时间
	@Column(nullable = false)
	private long inTime;
	//登出时间
	@Column(nullable = false)
	private long outTime;
	//登录IP
	@Column(nullable = false)
	private String loginIP;
	//注册方式()
	@Column(nullable = false)
	private int createType;
	
	public static AccountEntity valueOf(Long accountId, String uuid, String password,
			String createIP, int accountType, int createType){
		AccountEntity retEntity = new AccountEntity();
		retEntity.accountId = accountId;
		retEntity.uuid = uuid;
		retEntity.password = password;
		retEntity.state = 0;
		retEntity.createTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		retEntity.createIP = createIP;
		retEntity.accountType = accountType;
		retEntity.inTime = retEntity.createTime;
		retEntity.outTime = retEntity.createTime;
		retEntity.loginIP = createIP;
		retEntity.createType = createType;
		
		return retEntity;
	}
	
	@Override
	public Long getId() {
		return accountId;
	}

	public String getUuid() {
		return uuid;
	}

	@Enhance
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getAccountId() {
		return accountId;
	}

	public String getPassword() {
		return password;
	}

	@Enhance
	public void setPassword(String password) {
		this.password = password;
	}

	public int getState() {
		return state;
	}

	@Enhance
	public void setState(int state) {
		this.state = state;
	}

	public long getCreateTime() {
		return createTime;
	}

	@Enhance
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getCreateIP() {
		return createIP;
	}

	@Enhance
	public void setCreateIP(String createIP) {
		this.createIP = createIP;
	}

	public int getAccountType() {
		return accountType;
	}

	@Enhance
	public void setAccountType(int accountType) {
		this.accountType = accountType;
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

	public String getLoginIP() {
		return loginIP;
	}

	@Enhance
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public int getCreateType() {
		return createType;
	}

	@Enhance
	public void setCreateType(int createType) {
		this.createType = createType;
	}	
	
}
