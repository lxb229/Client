package com.wangzhixuan.mapper;

import com.wangzhixuan.model.PlayRoom;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 玩家在房间中的明细 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
public interface PlayRoomMapper extends BaseMapper<PlayRoom> {
	
	List<PlayRoom> selectPlayRoomPage(Pagination page, Map<String, Object> params);
	
	/**
	 * 获取玩家在房间中的明细数据
	 * @param playerId 玩家id
	 * @param houseOwner 房主
	 * @param roomId 房间号
	 * @param joinTime 玩家加入房间时间
	 * @param roomDays
	 * @return
	 */
	PlayRoom getPlayRoomBy(@Param("playerId") String playerId, @Param("houseOwner") String houseOwner, @Param("roomId") int roomId, @Param("joinTime") Date joinTime, @Param("roomDays") String roomDays);
	
	/**
	 * 更新参加该房间的玩家明细——修改房间总局数
	 * @param partyNum 房间局数
	 * @param houseOwner 房主
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @return
	 */
	int updateRoomPartyNum(@Param("partyNum")int partyNum, @Param("houseOwner")String houseOwner, @Param("roomId")int roomId, @Param("createTime")Date createTime, @Param("roomDays") String roomDays);

	/**
	 * 更新参加该房间的玩家明细——修改房间流水
	 * @param partyNum 房间局数
	 * @param houseOwner 房主
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @return
	 */
	int updateRoomFinancialWater(@Param("financialWater")int financialWater, @Param("houseOwner")String houseOwner, @Param("roomId")int roomId, @Param("createTime")Date createTime, @Param("roomDays") String roomDays);
	
	/**
	 * 更新参加该房间的玩家明细——修改房间保险流水
	 * @param partyNum 房间局数
	 * @param houseOwner 房主
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @return
	 */
	int updateRoomInsuranceWater(@Param("insuranceWater")int insuranceWater, @Param("houseOwner")String houseOwner, @Param("roomId")int roomId, @Param("createTime")Date createTime, @Param("roomDays") String roomDays);
	
	/**
	 * 更新参加该房间的玩家明细——修改房间解散时间
	 * @param disappaearTime 解散时间
	 * @param houseOwner 房主id
	 * @param roomId 房间id
	 * @param createTime 创建时间
	 * @param roomDays
	 * @return
	 */
	int updateRoomDisappaearTime(@Param("disappaearTime")Date disappaearTime, @Param("houseOwner")String houseOwner, @Param("roomId")int roomId, @Param("createTime")Date createTime, @Param("roomDays") String roomDays); 
}