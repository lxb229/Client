package com.guse.four_one_nine.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.DayUserCount;

/** 
* @ClassName: DayUserCountDao 
* @Description: 用户日统计数据接口
* @author Fily GUSE
* @date 2018年1月4日 下午4:23:26 
*  
*/
@Repository
public interface DayUserCountDao {
	
	/** 
	* @Title: getByDate 
	* @Description: 获取指定日期 统计信息
	* @param @return
	* @return DayUserCount 
	* @throws 
	*/
	@Select("select * from day_user_count where TO_DAYS(date) = TO_DAYS(NOW())")
	public DayUserCount getToDay();
	
	
	/** 
	* @Title: add 
	* @Description: 添加统计信息 
	* @param @param count
	* @return void 
	* @throws 
	*/
	@Insert("insert into day_user_count(date) values(NOW())")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addToDay();
	
	/** 
	* @Title: save 
	* @Description: 保存信息 
	* @param 
	* @return void 
	* @throws 
	*/
	@Update("update day_user_count "
			+ "set register_num=#{register_num},appinstaller_num=#{appinstaller_num},real_num=#{real_num},seller_num=#{seller_num},live_num=#{live_num}"
			+ " where id=#{id}")
	public void save(DayUserCount count);
	
	/** 
	* @Description: 获取统计信息 
	* @param @return
	* @return Map<String,Integer> 
	* @throws 
	*/
	@Select("SELECT "
				+ " IFNULL(SUM(register_num),0) register_num, IFNULL(SUM(real_num),0) real_num, "
				+ " IFNULL(SUM(seller_num),0) seller_num, IFNULL(SUM(live_num),0) live_num,"
				+ " IFNULL(SUM(appinstaller_num),0) appinstaller_num "
			+ " FROM `day_user_count`")
	public Map<String, Integer> count();

}
