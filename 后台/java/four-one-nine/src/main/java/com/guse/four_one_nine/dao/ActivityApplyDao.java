package com.guse.four_one_nine.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.ActivityApply;
import com.guse.four_one_nine.dao.model.User;

/** 
* @ClassName: ActivityApplyDao
* @Description: 活动报名接口
* @author: wangkai
* @date: 2018年1月4日 下午8:13:56
*  
*/
@Repository
public interface ActivityApplyDao {
	
	/** 
	* @Title: addActivityApply 
	* @Description: 新增报名
	* @param @param activityApply
	* @return void 
	* @throws 
	*/
	@Insert("insert into activity_apply(activity_id,user_id,apply_time) values"+
	"(#{activity_id},#{user_id},#{apply_time})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public boolean addActivityApply(ActivityApply activityApply);
	
	/** 
	* @Title: findByActivityId 
	* @Description: 获取活动参与人员 
	* @param @param id
	* @param @return
	* @return List<ActivityApply> 
	* @throws 
	*/
	@Select("SELECT u.* FROM activity_apply t LEFT JOIN `user` u ON u.user_id = t.user_id where activity_id=#{id}")
	public List<User> findByActivityId(@Param("id")int id);

	
	/** 
	* @Title: ratio 
	* @Description: 人员分布 
	* @param @param id
	* @param @return
	* @return List<Map<String,Integer>> 
	* @throws 
	*/
	@Select("SELECT u.user_source, COUNT(t.id) num "
			+ "FROM activity_apply t  LEFT JOIN `user` u ON t.user_id = u.user_id WHERE t.activity_id = #{id} "
			+ "GROUP BY u.user_source")
	public List<Map<String, String>> ratio(@Param("id")int id);

}
