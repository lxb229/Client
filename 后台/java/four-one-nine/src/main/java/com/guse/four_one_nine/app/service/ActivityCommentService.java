package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.ActivityCommentDao;
import com.guse.four_one_nine.dao.model.ActivityComment;

/** 
* @ClassName: ActivityCommentService
* @Description: 活动评论管理
* @author: wangkai
* @date: 2018年1月9日 下午3:17:45 
*  
*/
@Service
public class ActivityCommentService {

	
	@Autowired
	ActivityCommentDao activityCommentDao;

	/**
	 * 新增用户评论
	 * 
	 * @param user
	 */
	public void addActivityApply(ActivityComment activityComment ) {
		activityCommentDao.addActivityComment(activityComment);
	}
}
