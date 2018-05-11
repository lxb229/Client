package com.guse.four_one_nine.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.ActivityVisit;;

/** 
* @ClassName: ActivtyCommentDao 
* @Description: 活动访问
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface ActivityVisitDao {
	
	/** 
	* @Title: addActivityVisit 
	* @Description: 新增访问
	* @param @param activityVisit
	* @return void 
	* @throws 
	*/
	@Insert("insert into activity_visit(activity_id,user_id,visit_time) values"+
	"(#{activity_id},#{user_id},#{visit_time})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addActivityVisit(ActivityVisit activityVisit);

	/** 
	* @Title: findFiveActive 
	* @Description: 一周人员活跃统计 
	* @param @param id
	* @param @return
	* @return List<Map<String,Integer>> 
	* @throws 
	*/
	@Select("SELECT COUNT(DISTINCT user_id) users FROM activity_visit "
			+ "WHERE week(visit_time) = week(NOW()) AND activity_id = #{id} GROUP BY TO_DAYS(visit_time) ORDER BY TO_DAYS(visit_time)")
	public List<Map<String, Integer>>findWeekActive(@Param("id")int id);

}
