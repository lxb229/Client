package com.guse.stock.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.stock.dao.model.GameAutomaticlyOrder;

/** 
* @ClassName: IGameAutomaticlyOrderDao 
* @Description: 用户购买订单信息
* @author Fily GUSE
* @date 2017年9月28日 上午11:50:09 
*  
*/
@Repository
public interface IGameAutomaticlyOrderDao {
	
	/** 
	* @Title: getByOrderId 
	* @Description: 根据订单id获取自动购买信息 
	* @param @return
	* @return GameAutomaticlyOrder 
	* @throws 
	*/
	@Select("select * from pl_game_automaticly_order t where id=#{id}")
	public GameAutomaticlyOrder getByOrderId(@Param("id")long id);
	
	/** 
	* @Title: updateOrderStatus 
	* @Description: 修改订单状态 
	* @param @param order
	* @return void 
	* @throws 
	*/
	@Update("update pl_game_automaticly_order set status=#{status}, status_coment=#{status_coment},success_time=#{success_time} "
			+ " where id=#{id}")
	public void updateOrderStatus(GameAutomaticlyOrder order);
	
	/** 
	* @Title: updateStock 
	* @Description: 更新订单库存信息 
	* @param 
	* @return void 
	* @throws 
	*/
	@Update("update pl_game_automaticly_order set stock_id=#{stock_id} where id=#{id}")
	public void updateStock(@Param("stock_id")long stock_id, @Param("id")long id);

}
