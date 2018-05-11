package com.palmjoys.yf1b.act.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

/**
 * 游戏角色属性实体类
 * */
@Entity
@Memcached
public class RoleEntity implements IEntity<String>{
	//角色游戏展示唯一Id
	@Id
	private String starNO;
	//所属帐号
	@Column(nullable = false)
	private long accountId;
	//角色头像
	@Column(nullable = false)
	private String headImg;
	//性别(0=保密码,1=男,2=女)
	@Column(nullable = false)
	private int sex;
	//角色呢称
	@Column(nullable = false)
	private String nick;

	public static RoleEntity valueOf(String starNO, long accountId, String headImg, int sex, String nick){
		RoleEntity retEntity = new RoleEntity();
		retEntity.starNO = starNO;
		retEntity.accountId = accountId;
		retEntity.headImg = headImg;
		retEntity.sex = sex;
		retEntity.nick = nick;
		return retEntity;
	}
	
	@Override
	public String getId() {
		return starNO;
	}

	public String getStarNO() {
		return starNO;
	}

	public long getAccountId() {
		return accountId;
	}

	@Enhance
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getHeadImg() {
		return headImg;
	}

	@Enhance
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public int getSex() {
		return sex;
	}

	@Enhance
	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getNick() {
		return nick;
	}

	@Enhance
	public void setNick(String nick) {
		this.nick = nick;
	}	
	
}
