package com.guse.four_one_nine.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.app.model.IndexVisits;

/** 
* @ClassName: IndexVisitsDao 
* @Description: 活动板块统计接口
* @author wangk
* @date 2018年2月4日 下午2:20:13 
*  
*/
@Repository
public interface IndexVisitsDao {

	/** 
	* @Title: getIndexVisits
	* @Description:  
	* @param @return
	* @return DayDealCount 
	* @throws 
	*/
	@Select("select * from index_visits ")
	public IndexVisits getToIndexVisits();
	/** 
	* @Title: save 
	* @Description: 保存统计信息 
	* @param @param count
	* @return void 
	* @throws 
	*/
	@Update("update index_visits set "
			+ "story_solitaire_visits=#{story_solitaire_visits},transaction_visits=#{transaction_visits},"
			+ "graffiti_visits=#{graffiti_visits},day_sign_visits=#{day_sign_visits}"
			+ ",activities_visits=#{activities_visits},posters_visits=#{posters_visits}"
			+ "where id=1")
	public void save(IndexVisits indexVisits);
	
	/** 
	* @Description: 活动板块统计 
	* @param @return
	* @return Map<String,Integer> 
	* @throws 
	*/
	@Select("select "
			+ "IFNULL(SUM(story_solitaire_visits),0) story_solitaire_visits, "
			+ "IFNULL(SUM(transaction_visits),0) transaction_visits, "
			+ "IFNULL(SUM(graffiti_visits),0) graffiti_visits, "
			+ "IFNULL(SUM(day_sign_visits),0) day_sign_visits, "
			+ "IFNULL(SUM(activities_visits),0) activities_visits, "
			+ "IFNULL(SUM(posters_visits),0) posters_visits"
			+ " FROM index_visits ")
	public Map<String, Integer> indexVisitsCount();
}
