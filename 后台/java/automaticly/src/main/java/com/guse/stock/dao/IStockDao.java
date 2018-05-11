package com.guse.stock.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.guse.stock.dao.model.GameAutomaticlyOrder;
import com.guse.stock.dao.model.Stock;

/** 
* @ClassName: IStockDao 
* @Description: 库存数据接口
* @author Fily GUSE
* @date 2017年8月31日 上午11:08:28 
*  
*/
@Repository
public interface IStockDao {
	public final static Logger logger = LoggerFactory.getLogger(IStockDao.class);
	
	/** 
	* @Title: getStockById 
	* @Description: 根据id获取库存信息 
	* @param @param stock_id
	* @param @return
	* @return Stock 
	* @throws 
	*/
	@Select("select * from pl_stock where stock_id=#{stock_id}")
	public Stock getStockById(@Param("stock_id")long stock_id);
	
	/** 
	* @Title: getStock 
	* @Description: 查询用户未使用的凭证信息 
	* @param @param gui
	* @param @return
	* @return Stock 
	* @throws 
	*/
	@Select("SELECT ps.* FROM pl_stock ps LEFT JOIN pl_stock_uselog su ON ps.stock_id = su.stock_id "
			+ "WHERE su.id IS NULL AND ps.is_use = 0  "
			+ "and ps.user_id=#{user_id} and ps.game_id=#{game_id} and ps.par_id=#{par_id} order by ps.stock_id limit 1")
	public Stock getStock(GameAutomaticlyOrder autoOrder);
	
	/** 
	* @Title: updateStatus 
	* @Description: 修改状态为已使用 
	* @param @param stock
	* @return void 
	* @throws 
	*/
	@Update("update pl_stock set is_use=1 where stock_id=#{stock_id}")
	public void updateStatus(long stock);
	
	/** 
	* @Title: updateCount 
	* @Description: 更新库存统计信息 
	* @param @param stock
	* @return void 
	* @throws 
	*/
	@Update("update pl_stock_counts t set t.used_cnt=t.used_cnt+1 "
			+ "where user_id=#{user_id} and game_id=#{game_id} and par_id=#{par_id}")
	public void updateCount(Stock stock);
	
}
