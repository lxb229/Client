package com.guse.four_one_nine.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.ActivityComment;;

/** 
* @ClassName: ActivityCommentDao
* @Description: 活动评价
* @author: wangkai
* @date: 2018年1月15日 下午8:17:46
*  
*/
@Repository
public interface ActivityCommentDao {
	
	/** 
	* @Title: addActivityComment 
	* @Description: 活动评价 
	* @param @param activityComment
	* @return void 
	* @throws 
	*/
	@Insert("insert into activity_comment(activity_id,user_id,content,comment_time) values"+
	"(#{activity_id},#{user_id},#{content},#{comment_time})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addActivityComment(ActivityComment activityComment);


}
