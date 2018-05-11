package com.guse.four_one_nine.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.ActivityInfo;

/** 
* @ClassName: ActivityApplyDao 
* @Description: 活动报名接口
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface ActivityInfoDao {
	
	/** 
	* @Title: addActivityApply 
	* @Description: 新增报名
	* @param @param activityApply
	* @return void 
	* @throws 
	*/
	@Insert("insert into activity_info(activty_id,channel_name,channel_url) values(#{activty_id},#{channel_name},#{channel_url})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addActivityInfo(ActivityInfo activityInfo);
	
	
	/** 
	* @Title: findByActivityId 
	* @Description: 获取渠道列表 
	* @param @param id
	* @param @return
	* @return List<ActivityInfo> 
	* @throws 
	*/
	@Select("select * from activity_info where activty_id = #{id}")
	public List<ActivityInfo> findByActivityId(@Param("id")int id);

}
