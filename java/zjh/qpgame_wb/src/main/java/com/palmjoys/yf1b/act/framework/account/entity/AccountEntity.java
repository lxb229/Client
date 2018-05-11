package com.palmjoys.yf1b.act.framework.account.entity;

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
	//用户名
	@Column(nullable = false)
	private String uuid;
	//密码
	@Column(nullable = false)
	private String password;
	//明星号(唯-9位字符串)
	@Column(nullable = false)
	private String starNO;
	//角色头像
	@Column(nullable = false)
	private String headImg;
	//性别(0=保密码,1=男,2=女)
	@Column(nullable = false)
	private int sex;
	//角色呢称
	@Column(nullable = false)
	private String nick;
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
	//机器人标识(1=机器人)
	@Column(nullable = false)
	private int robot;
	
	public static AccountEntity valueOf(Long accountId, String uuid, String password, String starNO,
			String headImg, int sex, String nick, String createIP, int robot){
		AccountEntity retEntity = new AccountEntity();
		retEntity.accountId = accountId;
		retEntity.uuid = uuid;
		retEntity.password = password;
		retEntity.starNO = starNO;
		retEntity.headImg = headImg;
		retEntity.sex = sex;
		retEntity.nick = nick;
		retEntity.state = 0;
		retEntity.createTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		retEntity.createIP = createIP;
		retEntity.accountType = 0;
		retEntity.robot = robot;
		
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

	public String getStarNO() {
		return starNO;
	}

	@Enhance
	public void setStarNO(String starNO) {
		this.starNO = starNO;
	}

	public String getHeadImg() {
		return headImg;
	}

	@Enhance
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getNick() {
		return nick;
	}

	@Enhance
	public void setNick(String nick) {
		this.nick = nick;
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

	public int getSex() {
		return sex;
	}

	@Enhance
	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getRobot() {
		return robot;
	}

	@Enhance
	public void setRobot(int robot) {
		this.robot = robot;
	}

	public String getPassword() {
		return password;
	}

	@Enhance
	public void setPassword(String password) {
		this.password = password;
	}

	
	
}
