package com.wangzhixuan.mapper;

import com.wangzhixuan.model.JoinRoomLog;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 加入房间日志 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
public interface JoinRoomLogMapper extends BaseMapper<JoinRoomLog> {
	JoinRoomLog getJoinRoomLogBy(@Param("playerId")String playerId, @Param("houseOwner")String houseOwner,@Param("roomId") int roomId,@Param("createTime") Date createTime,@Param("roomDays") String roomDays);
	/**
	 * 获取房间加入人数
	 * @param houseOwner 房主id
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @param roomDays 有效天数
	 * @return
	 */
	Map<String, Object> getRoomJoinNum(@Param("houseOwner")String houseOwner,@Param("roomId") int roomId,@Param("createTime") Date createTime,@Param("roomDays") String roomDays);
	
	/**
	 * 获取加入房间数据汇总
	 * @return
	 */
	Map<String, Object> selectJoinRoomData();
}