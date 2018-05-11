package com.guse.stock.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

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
	
	/**
	 * 
	 * @param userId 
	 * @param gameId 
	 * @param perId 
	 * @param priority 凭证是否先进先出标识 1：凭证先进先出 2：凭证库存编号升序 
	 * @return 符合条件的第一条数据
	 */
	
	/** 
	* @Title: findByUserGamePer 
	* @Description: 获取用户指定游戏面值的库存，根据买入降序，千手降序，库存编号升序，游戏id升序，面值id升序获取第一条数据 
	* @param @param userId 用户id
	* @param @param gameId 游戏id
	* @param @param parId 面值id
	* @param @param orderbys 其他排序条件(不用加逗号)
	* 		元参数：priority
	* 		原sql:	<if test="priority != null and priority == 1 ">
						stock_id desc,
					</if>
					<if test="priority == null or priority != 1 ">
						stock_id asc,
					</if>
	* @param @return
	* @return Stock 
	* @throws 
	*/
	@Select("select * from pl_stock "
			+ "where user_id=#{userId} and game_id=#{gameId} and par_id=#{parId} "
			+ "order by is_buy desc,is_qs desc, ${orderbys} ,game_id asc,par_id asc  limit 1")
	public Stock findByUserGamePer(@Param("userId")Long userId, @Param("gameId")Long gameId, 
			@Param("parId")Long parId, @Param("orderbys")String orderbys);
	
	/** 
	* @Title: findByStock 
	* @Description: 根据库存id获取库存信息 
	* @param @param stock_id
	* @param @return
	* @return Stock 
	* @throws 
	*/
	@Select("select * from pl_stock t where t.stock_id=#{stock_id}")
	public Stock findByStock(long stock_id);
	
	/** 
	* @Title: addStock 
	* @Description: 添加库存信息 
	* @param @param stock
	* @param @return
	* @return int 
	* @throws 
	*/
	@Insert("insert into pl_stock(user_id, producer_id, game_id, par_id, cer_table_num, create_time) "
			+ "values(#{user_id}, #{producer_id}, #{game_id}, #{par_id}, #{cer_table_num}, #{create_time})")
	@Options(useGeneratedKeys=true, keyProperty="stock_id")//添加该行，id将被自动添加
	public int addStock(Stock stock);
	
	/**
	 * 修改库存信息
	 * @param stock 最新库存信息
	 */
	@Update("update pl_stock set is_use=#{is_use}, back_rolling_num=#{back_rolling_num} where stock_id=#{stock_id}")
	public int updateStock(Stock stock);
	
	/** 
	* @Title: countStock 
	* @Description: 查询用户库存量 
	* @param @param userId 用户id
	* @param @param gameId 游戏id
	* @param @param parId 档位id
	* @param @return
	* @return int 
	* @throws 
	*/
	@Select("select count(1) from pl_stock where is_use=0 and user_id=#{0} and game_id=#{1} and par_id=#{2}")
	public int countStock(Long userId, Long gameId, Long parId);
	
}
