package com.guse.four_one_nine.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.ServerOrder;

/** 
* @ClassName: ServerOrderDao 
* @Description: 服务订单管理
* @author Fily GUSE
* @date 2018年1月4日11:43:54 
*  
*/
@Repository
public interface ServerOrderDao {
	
	/** 
	* @Title: addServerOrder
	* @Description: 服务订单
	* @param @param serverOrder
	* @return void 
	* @throws 
	*/
	@Insert("insert into server_order(id,server_id,union_id,"
			+ "tip_money,pay_type, buy_user,buy_num,total,buy_time) "
			+ "values(#{id},#{server_id},#{union_id},"
			+ "#{tip_money},#{pay_type},#{buy_user},"
			+ "#{buy_num},#{total},#{buy_time})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public boolean addServerOrder(ServerOrder serverOrder);

	
	/** 
	* @Title: getServerOrder 
	* @Description: 查询服务订单
	* @param Id
	* @return void 
	* @throws 
	*/
	@Select("SELECT * FROM `server_order` "
			+ "WHERE id = #{id}")
	public ServerOrder getServerOrder(@Param("id") long id);
	
	/** 
	* @Title: updateServerOrder 
	* @Description: 服务订单修改
	* @param @param serverOrder
	* @return void 
	* @throws 
	*/
	@Update("update `server_order` set status=#{status}"
			+ " where id=#{id}")
	public void updateServerOrder(ServerOrder serverOrder);

	
	/** 
	* @Title: findByServerId 
	* @Description: 获取服务订单 
	* @param @param serverId
	* @param @return
	* @return List<Map<String,Object>> 
	* @throws 
	*/
	@Select("SELECT t.id, u.nick_name, t.total, t.tip_money, t.`status` gain_status, t.buy_time, t.`status` FROM server_order t "
			+ " LEFT JOIN `user` u ON u.user_id = t.buy_user"
			+ " WHERE t.server_id = #{serverId}")
	public List<Map<String, Object>> findByServerId(@Param("serverId")long serverId);
	
	/** 
	* @Description: 统计工会收益 
	* @param @param unionId
	* @param @return
	* @return Map<String,Object> 
	* @throws 
	*/
	@Select("SELECT IFNULL(SUM(total), 0) total, IFNULL(SUM(IF(status=1,total,0)),0) collected,IFNULL(SUM(IF(status<>1,total,0)),0) uncollected  "
			+ " FROM server_order WHERE union_id = #{unionId}")
	public Map<String, Integer> countByUnion(@Param("unionId") long unionId);
	
	/** 
	* @Description: 统计用户消费 
	* @param @param userId
	* @param @return
	* @return Integer 
	* @throws 
	*/
	@Select("SELECT IFNULL(SUM(total), 0) from server_order WHERE buy_user=#{userId}")
	public Integer countUserExpenditure(@Param("userId")long userId);


	/** 
	* @Description: 买家交易排行榜 
	* @param @return
	* @return List<Map<String,Object>> 
	* @throws 
	*/
	@Select("SELECT u.nick_name, u.head_picture, SUM(t.total) total "
			+ " FROM server_order t LEFT JOIN `user` u ON u.user_id = t.buy_user "
			+ " GROUP BY t.buy_user ORDER BY total DESC LIMIT 10")
	public List<Map<String, Object>> rankingBuy();


	/** 
	* @Description: 卖家交易排行榜 
	* @param @return
	* @return List<Map<String,Object>> 
	* @throws 
	*/
	@Select("SELECT u.nick_name, u.head_picture, SUM(t.total) total "
			+ "FROM server_order t LEFT JOIN `server` s ON s.id = t.server_id "
			+ "LEFT JOIN `user` u ON u.user_id = s.publish_user "
			+ "GROUP BY s.publish_user ORDER BY total DESC LIMIT 10")
	public List<Map<String, Object>> rankingSell();

}
