package com.guse.four_one_nine.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.Activity;

/** 
* @ClassName: ActivityDao 
* @Description:活动管理
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface ActivityDao {
	
	/** 
	* @Title: addActivity 
	* @Description: 新增活动
	* @param @param activity
	* @return void 
	* @throws 
	*/
	@Insert("insert into activity(activity_name,activity_cover,mark_top,type,start_time, end_time,restrict_no,activity_content,creater) "
			+ "values(#{activity_name},#{activity_cover},#{mark_top},#{type},#{start_time},#{end_time},#{restrict_no},#{activity_content},#{creater})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addActivity(Activity activity);
	
	/** 
	* @Title: delete 
	* @Description: 逻辑删除活动 
	* @param @param ids
	* @param @return
	* @return int 
	* @throws 
	*/
	@Update("update activity set delete_mark=0 where id in(${ids})")
	public int delete(@Param("ids")String ids);
	
	
	/** 
	* @Title: findAttract 
	* @Description: 总参与人数和访问量 
	* @param @return
	* @return int 
	* @throws 
	*/
	@Select("select sum(IFNULL(participants_no, 0)) participants, sum(IFNULL(interview_no, 0)) interview from activity")
	public Map<String, Integer> findAttract();
	
	/** 
	* @Title: getById 
	* @Description: 根据id获取信息 
	* @param @param id
	* @param @return
	* @return Activity 
	* @throws 
	*/
	@Select("select * from activity where id=#{id}")
	public Activity getById(@Param("id")int id);

	/** 
	* @Title: getActivity
	* @Description: 查询活动信息
	* @param Id
	* @return void 
	* @throws 
	*/
	@Select("SELECT * FROM `activity` "
			+ "WHERE id = #{id}")
	public Activity getActivity(@Param("id") long id);
	
	/** 
	* @Title: save 
	* @Description: 保存活动信息 
	* @param 
	* @return void 
	* @throws 
	*/
	@Update("update activity set participants_no=#{participants_no},interview_no=#{interview_no},"
			+ "criticism_no=#{criticism_no}"
			+ " where id=#{id}")
	public void save(Activity activity);
}
