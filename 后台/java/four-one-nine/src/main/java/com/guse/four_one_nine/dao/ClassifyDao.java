package com.guse.four_one_nine.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.Classify;


/** 
* @ClassName: ClassifyDao
* @Description: 服务分类接口
* @author: wangkai
* @date: 2018年1月8日 下午1:54:08 
*  
*/
@Repository
public interface ClassifyDao {
	
	/** 
	* @Title: addClassify 
	* @Description: 服务分类新增
	* @param @param classify
	* @return void 
	* @throws 
	*/
	@Insert("insert into classify(classify_name,classify_code,creater,create_time) values(#{classify_name},#{classify_code},#{creater},#{create_time})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addClassify(Classify classify);
	
	/** 
	* @Title: findAll 
	* @Description: 获取全部分类 
	* @param @return
	* @return List<Classify> 
	* @throws 
	*/
	@Select("select * from classify")
	public List<Classify> findAll();
	
	/** 
	* @Title: getByName 
	* @Description: 根据名称查询分类 
	* @param @param name
	* @param @return
	* @return Classify 
	* @throws 
	*/
	@Select("select * from classify where classify_name=#{name} or classify_code=#{name}")
	public Classify getByName(@Param("name")String name);
	
	/** 
	* @Description: 服务分类统计 
	* @param @return
	* @return List<Map<String,Object>> 
	* @throws 
	*/
	@Select("SELECT "
			+ " t.id id, t.classify_name name, COUNT(DISTINCT s.publish_user) userNum, COUNT(DISTINCT s.id) serverNum"
			+ " ,IFNULL(SUM(o.server_money), 0) serverMoney "
			+ " FROM classify t LEFT JOIN `server` s ON t.id = s.classify_id"
			+ " LEFT JOIN server_order o ON o.server_id = s.id"
			+ " GROUP BY t.id")
	public List<Map<String, Object>> findClassifyCount();
	
	/** 
	* @Description: 本周销量 
	* @param @param id
	* @param @return
	* @return List<Map<String,Integer>> 
	* @throws 
	*/
	@Select("SELECT WEEKDAY(buy_time) weekNum,IFNULL(SUM(o.server_money), 0) serverMoney "
			+ " FROM classify t LEFT JOIN `server` s ON t.id = s.classify_id "
			+ " LEFT JOIN server_order o ON o.server_id = s.id"
			+ " WHERE t.id = #{id} AND WEEK(NOW(), 0) = WEEK(o.buy_time, 0) GROUP BY TO_DAYS(buy_time)")
	public List<Map<String, Integer>> findWeekSales(@Param("id")int id);

}
