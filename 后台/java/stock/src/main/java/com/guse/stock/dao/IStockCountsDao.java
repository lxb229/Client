package com.guse.stock.dao;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.stock.dao.model.StockCounts;

/**
 * 库存统计表Dao
 * @author 不能
 *
 */
@Repository
public interface IStockCountsDao {
	
	/**
	 * 获取用户指定游戏面值的库存统计，获取第一条数据
	 * @param userId 用户id
	 * @param gameId 游戏id
	 * @param perId 面值id
	 * @return 符合条件的第一条数据
	 */
	@Select("select * from pl_stock_counts where user_id=#{userId} and game_id=#{gameId} and par_id=#{parId}"
			+ " order by id desc limit 1")
	public StockCounts findByUserGamePer(@Param("userId")Long userId, @Param("gameId")Long gameId, @Param("parId")Long parId);
	
	/**
	 * 修改库存统计
	 * @param stockCounts 库存统计
	 */
	@Update("update pl_stock_counts set total=#{total}, used_cnt=#{used_cnt} where id=#{id}")
	public int updateStockCounts(StockCounts stockCounts);
	
	/** 
	* @Title: accumStockCounts 
	* @Description: 用户库存总量累加 
	* @param @param id
	* @return void 
	* @throws 
	*/
	@Update("update pl_stock_counts set total=total+1 where id=#{id}")
	public void accumStockCounts(@Param("id")long id);
	
	/** 
	* @Title: addStockCounts 
	* @Description: 添加统计信息 
	* @param @param data
	* @return void 
	* @throws 
	*/
	@Insert("insert into pl_stock_counts(game_id,par_id,user_id,total) "
			+ " values(#{game_id},#{par_id},#{user_id},1)")
	@Options(useGeneratedKeys=true, keyProperty="id")//添加该行，id将被自动添加
	public void addStockCounts(StockCounts data);
}
