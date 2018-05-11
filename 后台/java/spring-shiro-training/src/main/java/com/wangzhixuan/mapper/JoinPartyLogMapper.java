package com.wangzhixuan.mapper;

import com.wangzhixuan.model.JoinPartyLog;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 玩家入局记录 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public interface JoinPartyLogMapper extends BaseMapper<JoinPartyLog> {
	/**
	 * 获取房间牌局次数
	 * @param 
	 * @param houseOwner 房主id
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @param roomDays 有效天数
	 * @return
	 */
	Map<String, Object> getRoomPartyNum(@Param("playId")String playId, @Param("houseOwner")String houseOwner, @Param("roomId") int roomId, @Param("createTime") Date createTime, @Param("roomDays") String roomDays);
	
	/**
	 * 获取房间入局人数
	 * @param houseOwner 房主id
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @param roomDays 有效天数
	 * @return
	 */
	Map<String, Object> getPartyPeopleNum(@Param("houseOwner")String houseOwner, @Param("roomId") int roomId, @Param("createTime") Date createTime, @Param("roomDays") String roomDays);
	
	/**
	 * 获取入局数据汇总
	 * @return
	 */
	Map<String, Object> selectJoinPartyData();
	
	/**
	 * 获取今日牌局总数
	 * @return
	 */
	Map<String, Object> selectTodayPartys();
	
}