package com.guse.stock.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.stock.dao.model.StockUseLog;

/** 
* @ClassName: IStockUseLogDao 
* @Description: 库存记录表
* @author Fily GUSE
* @date 2017年9月18日 下午4:23:21 
*  
*/
@Repository
public interface IStockUseLogDao {
	
	/**
	 * 增加一个出库记录
	 * @param stockUseLog 出库记录
	 */
	@Insert("insert into pl_stock_uselog(number, stock_id, user_id, poundage, mark, create_time) "
			+ "values(#{number}, #{stock_id}, #{user_id}, #{poundage}, #{mark}, #{create_time})")
	@Options(useGeneratedKeys=true, keyProperty="id")//添加该行，id将被自动添加
	public int addStockUseLog(StockUseLog stockUseLog);
	
	/**
	 * 根据订单查询出库记录
	 * @param orderId 订单编号
	 * @return 符合条件的出库记录
	 */
	@Select("select * from pl_stock_uselog where number=#{orderId} "
			+ "order by create_time desc limit 1 ")
	public StockUseLog findByOrder(String orderId);
	
	/**
	 * 更新出库记录
	 * @param status 需要更新的状态
	 * @param stockUseLogId 出库记录id
	 */
	@Update("update pl_stock_uselog set status=#{status} where id=#{id}")
	public int updateStockUseLog(StockUseLog stockUseLog);

}
