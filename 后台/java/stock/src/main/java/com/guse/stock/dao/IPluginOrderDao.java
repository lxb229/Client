package com.guse.stock.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.guse.stock.dao.model.PluginOrder;

public interface IPluginOrderDao {

	/**
	 * 
	 * @param userId 
	 * @param endTime 截止时间戳 
	 * @param type 类型 1：插件商城插件，2，库存管理里的订购服务
	 * @return
	 */
	/** 
	* @Title: findByUserId 
	* @Description: 根据用户id、截止时间戳、插件类型获取该用户是否拥有有效插件 
	* @param @param userId 用户id
	* @param @param params 其他动态sql条件（需要自己拼装	）
	* 	原参数：@Param("endTime")long endTime, @Param("type")int type
	* 	原sql:	<if test="endTime != null and endTime != '' and endTime != 0 ">
					and end_time&gt;=#{endTime} 
				</if>
				<if test="type != null and type != '' and type != 0 ">
					and type=#{type} 
				</if>
	* @param @return
	* @return PluginOrder 
	* @throws 
	*/
	@Select("select * from rp_plugin_order where user_id=#{userId} #{params} "
			+ "order by create_time desc limit 1")
	public PluginOrder findByUserId(@Param("userId")String userId,@Param("params") String params);
}
