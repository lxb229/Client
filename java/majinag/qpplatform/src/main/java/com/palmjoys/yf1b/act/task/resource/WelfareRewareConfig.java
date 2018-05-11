package com.palmjoys.yf1b.act.task.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("task")
public class WelfareRewareConfig {
	@ResourceId
	private int id;
	//红包领取地址
	private String welfareAddr;
	//银币抽奖地址
	private String silverMoneyAddr;
	//金币兑换奖品地址
	private String goldMoneyAddr;
	//获取金币奖励后发公告限制数量
	private int noticeLimit;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWelfareAddr() {
		return welfareAddr;
	}
	public void setWelfareAddr(String welfareAddr) {
		this.welfareAddr = welfareAddr;
	}
	public String getSilverMoneyAddr() {
		return silverMoneyAddr;
	}
	public void setSilverMoneyAddr(String silverMoneyAddr) {
		this.silverMoneyAddr = silverMoneyAddr;
	}
	public String getGoldMoneyAddr() {
		return goldMoneyAddr;
	}
	public void setGoldMoneyAddr(String goldMoneyAddr) {
		this.goldMoneyAddr = goldMoneyAddr;
	}
	public int getNoticeLimit() {
		return noticeLimit;
	}
	public void setNoticeLimit(int noticeLimit) {
		this.noticeLimit = noticeLimit;
	}
	
}
