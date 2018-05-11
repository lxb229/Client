package com.palmjoys.yf1b.act.framework.common.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("common")
public class GameObjectConfig {
	@ResourceId
	private int id;
	//物件类型
	private String type;
	//物件名称
	private String name;
	//奖励发放方式(1=游戏内直接领取,2=邮件)
	private int rewardGiveType;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRewardGiveType() {
		return rewardGiveType;
	}
	public void setRewardGiveType(int rewardGiveType) {
		this.rewardGiveType = rewardGiveType;
	}
}
