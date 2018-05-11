package com.palmjoys.yf1b.act.activity.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.activity.entity.ActivityEntity;
import com.palmjoys.yf1b.act.activity.manager.ActivityManager;
import com.palmjoys.yf1b.act.activity.model.ActivityItemAttrib;
import com.palmjoys.yf1b.act.activity.model.ActivityVo;

@Component
@ConsoleBean
public class ActivityCommand {
	@Autowired
	private ActivityManager activityManager;

	@ConsoleCommand(name = "gm_activity_get_activitity_url", description = "获取活动链接URL")
	public Object gm_activity_get_activitity_url(){
		ActivityEntity activityEntity = activityManager.loadOrCreate();
		ActivityVo retVo = new ActivityVo();
		retVo.items.addAll(activityEntity.getActivityList());
		
		return Result.valueOfSuccess(retVo);
	}
	
	@ConsoleCommand(name = "gm_activity_set_activitity_url", description = "设置活动链接URL")
	public Object gm_activity_set_activitity_url(ActivityItemAttrib []items){
		ActivityEntity activityEntity = activityManager.loadOrCreate();
		List<ActivityItemAttrib> activityList = activityEntity.getActivityList();
		activityList.clear();
		for(ActivityItemAttrib item : items){
			activityList.add(item);
		}
		activityEntity.setActivityList(activityList);
		return Result.valueOfSuccess();
	}
	
}
