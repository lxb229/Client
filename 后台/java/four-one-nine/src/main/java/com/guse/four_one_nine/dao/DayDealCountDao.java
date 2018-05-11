package com.guse.four_one_nine.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.DayDealCount;

/** 
* @ClassName: DayDealCountDao 
* @Description: 交易信息统计接口
* @author Fily GUSE
* @date 2018年1月4日 下午5:20:13 
*  
*/
@Repository
public interface DayDealCountDao {

	/** 
	* @Title: getToDay 
	* @Description:  
	* @param @return
	* @return DayDealCount 
	* @throws 
	*/
	@Select("select * from day_deal_count where TO_DAYS(date) = TO_DAYS(NOW())")
	public DayDealCount getToDay();
	
	/** 
	* @Title: addToDay 
	* @Description: 新增今日统计信息 
	* @param 
	* @return void 
	* @throws 
	*/
	@Insert("insert into day_deal_count(date) values(NOW())")
	public void addToDay();
	
	/** 
	* @Title: save 
	* @Description: 保存统计信息 
	* @param @param count
	* @return void 
	* @throws 
	*/
	@Update("update day_deal_count set platform_statements_count=#{platform_statements_count},newly_server_count=#{newly_server_count},=#{newly_server_count}"
			+ ",order_num=#{order_num},order_count=#{order_count},order_max=#{order_max},cash_num=#{cash_num},cash_count=#{cash_count}"
			+ ",cash_max=#{cash_max},remit_num=#{remit_num},remit_count=#{remit_count},remit_max=#{remit_max} "
			+ "where id=#{id}")
	public void save(DayDealCount count);
	
	/** 
	* @Title: countCash 
	* @Description: 提现信息统计 
	* @param 
	* @return void 
	* @throws 
	*/
	@Select("select IFNULL(SUM(platform_statements_count),0) statements, IFNULL(SUM(cash_count),0) cash_count"
			+ ", IFNULL(SUM(cash_num),0) cash_num,(IFNULL(SUM(platform_statements_count),0)-IFNULL(SUM(cash_count),0)) balance "
			+ "from day_deal_count ")
	public Map<String, Integer> countCash();
	
	/** 
	* @Description: 服务信息统计 
	* @param @return
	* @return Map<String,Integer> 
	* @throws 
	*/
	@Select("select "
			+ "IFNULL(SUM(platform_statements_count),0) platform_statements_count, "
			+ "IFNULL(SUM(newly_server_count),0) newly_server_count, "
			+ "IFNULL(SUM(order_num),0) order_num, "
			+ "IFNULL(SUM(order_count),0) order_count, "
			+ "IFNULL(MAX(order_max),0) order_max, "
			+ "IFNULL(SUM(cash_num),0) cash_num, "
			+ "IFNULL(SUM(cash_count),0) cash_count, "
			+ "IFNULL(MAX(cash_max),0)	cash_max, "
			+ "IFNULL(SUM(remit_num),0) remit_num, "
			+ "IFNULL(SUM(remit_count),0) remit_count, "
			+ "IFNULL(MAX(remit_max),0) remit_max "
			+ " FROM day_deal_count ")
	public Map<String, Integer> count();
}
