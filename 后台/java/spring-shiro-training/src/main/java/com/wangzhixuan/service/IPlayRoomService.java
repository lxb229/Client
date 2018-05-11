package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.Room;

import java.util.Date;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 玩家在房间中的明细 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
public interface IPlayRoomService extends IService<PlayRoom> {
	
	/**
	 * 根据玩家、房间、房主、进入房间记录生成一条玩家在房间中的明细
	 * @param play
	 * @param room
	 * @param houseOwner
	 * @return
	 */
	Integer insertPlayRoom(Player player, Room room, Player houseOwner, JoinRoomLog joinRoomLog);
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	/**
	 * 获取玩家房间明细
	 * @param playerId
	 * @param houseOwner
	 * @param roomId
	 * @param joinTime
	 * @return
	 */
	PlayRoom getPlayRoomBy(String playerId, String houseOwner, int roomId, Date joinTime);
	
	/**
	 * 更新所有玩家在这个房间中明细里面的房间局数
	 * @param partyNum 房间的局数
	 * @param houseOwner
	 * @param roomId
	 * @param createTime
	 */
	int updateRoomPartyNum(int partyNum, String houseOwner, int roomId, Date createTime);
	
	/**
	 * 更新所有玩家在这个房间中明细里面的房间流水
	 * @param financialWater 房间流水
	 * @param houseOwner
	 * @param roomId
	 * @param createTime
	 * @return
	 */
	int updateRoomFinancialWater(int financialWater, String houseOwner, int roomId, Date createTime);

	/**
	 * 更新所有玩家在这个房间中明细里面的房间保险流水
	 * @param insuranceWater 保险流水
	 * @param houseOwner
	 * @param roomId
	 * @param createTime
	 * @return
	 */
	int updateRoomInsuranceWater(int insuranceWater, String houseOwner, int roomId, Date createTime);
	
	/**
	 * 更新所有玩家在这个房间中明细里面的房间解散时间
	 * @param disappaearTime 解散时间
	 * @param houseOwner
	 * @param roomId
	 * @param createTime
	 * @return
	 */
	int updateRoomDisappaearTime(Date disappaearTime, String houseOwner, int roomId, Date createTime);
}
