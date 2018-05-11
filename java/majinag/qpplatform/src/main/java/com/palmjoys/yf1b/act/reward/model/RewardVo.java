package com.palmjoys.yf1b.act.reward.model;

import java.util.ArrayList;
import java.util.List;

public class RewardVo {
	//奖励列表
	public List<RewareItemVo> rewardItems = new ArrayList<>();

	//获取指定类型代码的奖励数量
	public int getRewardNum(String type, int code){
		int retNum = 0;
		
		for(RewareItemVo rewardItem : rewardItems){
			if(rewardItem.type.equalsIgnoreCase(type) && rewardItem.code == code){
				retNum += rewardItem.amount;
			}
		}
		
		return retNum;
	}
}
