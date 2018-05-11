package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.ActivityApplyDao;
import com.guse.four_one_nine.dao.ActivityDao;
import com.guse.four_one_nine.dao.model.Activity;
import com.guse.four_one_nine.dao.model.ActivityApply;

/**
 * @ClassName: ActivityApplyService
 * @Description:
 * @author: wangkai
 * @date: 2018年1月9日 下午3:17:45
 * 
 */
@Service
public class ActivityApplyService {

	@Autowired
	ActivityApplyDao activityApplyDao;

	@Autowired
	ActivityDao activityDao;

	/**
	 * 新增用户报名
	 * 
	 * @param user
	 */
	public void addActivityApply(ActivityApply activityApply) {
		Boolean l =activityApplyDao.addActivityApply(activityApply);
		System.out.println("===========>"+l);
		Activity activity = activityDao.getActivity(activityApply.getActivity_id());
		if (activity != null) {
			activity.setParticipants_no(activity.getParticipants_no() + 1);
			activityDao.save(activity);
		}
	}
}
