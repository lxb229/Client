package com.palmjoys.yf1b.act.activity.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.activity.service.ActivityService;

@Component
public class ActivityFacadeImp implements ActivityFacade{
	@Autowired
	private ActivityService activityService;

	@Override
	public Object activity_get_activity_list(Long accountId) {
		return activityService.activity_get_activity_list(accountId);
	}

}
