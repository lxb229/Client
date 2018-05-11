package com.guse.stock.dao;

import org.apache.ibatis.annotations.Insert;

import com.guse.stock.dao.model.StockLog;

/** 
* @ClassName: IStockLogDao 
* @Description: 入库记录
* @author Fily GUSE
* @date 2017年8月31日 上午11:32:28 
*  
*/
public interface IStockLogDao {
	
	/** 
	* @Title: addStockLog 
	* @Description: 添加入库记录 
	* @param @param stockLog
	* @param @return
	* @return int 
	* @throws 
	*/
	@Insert("insert into pl_stock_log(stock_id, producer_id, game_id, par_id, create_time) "
			+ "values(#{stock_id}, #{producer_id}, #{game_id}, #{par_id}, #{create_time})")
	public int addStockLog(StockLog stockLog);

}
