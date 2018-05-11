package com.wangzhixuan.mapper;

import com.wangzhixuan.model.Room;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 房间表 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public interface RoomMapper extends BaseMapper<Room> {
	
	List<Room> selectRoomPage(Pagination page, Map<String, Object> params);
	
	Room getRoomBy(@Param("houseOwner") String houseOwner,@Param("roomId") int roomId,@Param("createTime") Date createTime,@Param("roomDays") String roomDays);
	/**
	 * 获取房间创建数据汇总
	 * @return
	 */
	Map<String, Object> selectRoomData();
	
	/**
	 * 获取牌局总数
	 * @return
	 */
	Map<String, Object> selectAllPartys();
	
	
}