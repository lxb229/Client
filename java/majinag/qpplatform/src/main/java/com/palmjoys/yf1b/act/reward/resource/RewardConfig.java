package com.palmjoys.yf1b.act.reward.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

import com.palmjoys.yf1b.act.reward.model.RandAttrib;
import com.palmjoys.yf1b.act.reward.model.RewareGroupAttrib;

@ResourceType("reward")
public class RewardConfig {
	//奖励组编号Id
	@ResourceId
	private String rewardId;
	//奖励物品随机方式
	private RandAttrib []srand;
	//奖励物品组
	private RewareGroupAttrib []rewareGroup;
	
	public String getRewardId() {
		return rewardId;
	}
	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}
	public RandAttrib[] getSrand() {
		return srand;
	}
	public void setSrand(RandAttrib[] srand) {
		this.srand = srand;
	}
	public RewareGroupAttrib[] getRewareGroup() {
		return rewareGroup;
	}
	public void setRewareGroup(RewareGroupAttrib[] rewareGroup) {
		this.rewareGroup = rewareGroup;
	}
	
}
