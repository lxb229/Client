package com.guse.four_one_nine.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.OrderSettlement;

/** 
* @ClassName: OrderSettlementDao 
* @Description: 订单结算
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface OrderSettlementDao {
	
	/** 
	* @Title: addOrderSettlement 
	* @Description: 订单结算
	* @param @param orderSettlement
	* @return void 
	* @throws 
	*/
	@Insert("insert into order_settlement values(order_id,seller_income,union_id,clo_user,clo_income,settlement_time)"+
			"#{order_id},#{seller_income},#{union_id},#{clo_user},#{clo_income},#{settlement_time})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addOrderSettlement(OrderSettlement orderSettlement);
}
