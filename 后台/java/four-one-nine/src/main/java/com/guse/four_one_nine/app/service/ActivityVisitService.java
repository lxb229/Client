package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.ActivityDao;
import com.guse.four_one_nine.dao.ActivityVisitDao;
import com.guse.four_one_nine.dao.model.Activity;
import com.guse.four_one_nine.dao.model.ActivityVisit;

/** 
* @ClassName: ActivityVisitService
* @Description: 
* @author: wangkai
* @date: 2018年1月9日 下午3:17:45 
*  
*/
@Service
public class ActivityVisitService {

	@Autowired
	ActivityVisitDao activityVisitDao;
	
	@Autowired
	ActivityDao activityDao;

	/**
	 * 新增活动访问
	 * 
	 * @param user
	 */
	public void addActivityVisit(ActivityVisit activityVisit) {
		
		activityVisitDao.addActivityVisit(activityVisit);
		Activity activity=activityDao.getActivity(activityVisit.getActivity_id());
		if(activity!=null){
			activity.setInterview_no(activity.getInterview_no()+1);
			activityDao.save(activity);
			
		}
	}
}
