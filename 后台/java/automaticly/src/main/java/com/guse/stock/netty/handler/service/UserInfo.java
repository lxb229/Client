package com.guse.stock.netty.handler.service;

public class UserInfo {
	
	// 用户编号
	public Long uid;
	// 用户名称
	public String name;
	// 父级用户编号
	public Long pid;
	// 用户hash
	public String hash;
	// 用户状态，默认空闲
	public int status = 0;
	// 设备号
	public String device_number;
	// 其他信息
	public Object other;
	
	public UserInfo(){}
	public UserInfo(long uid, String name, String hash) {
		this.uid = uid;
		this.name = name;
		this.hash = hash;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}
