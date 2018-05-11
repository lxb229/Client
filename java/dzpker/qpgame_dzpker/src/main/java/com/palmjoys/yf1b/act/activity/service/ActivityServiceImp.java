package com.palmjoys.yf1b.act.activity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.activity.entity.ActivityEntity;
import com.palmjoys.yf1b.act.activity.manager.ActivityManager;
import com.palmjoys.yf1b.act.activity.model.ActivityVo;

@Service
public class ActivityServiceImp implements ActivityService{
	@Autowired
	private ActivityManager activityManager;

	@Override
	public Object activity_get_activity_list(Long accountId) {
		ActivityEntity activityEntity = activityManager.loadOrCreate();
		ActivityVo retVo = new ActivityVo();
		retVo.items.addAll(activityEntity.getActivityList());
		
		return Result.valueOfSuccess(retVo);
	}

}
