package com.wangzhixuan.service;

import com.wangzhixuan.model.JoinPartyLog;
import com.wangzhixuan.model.SystemTask;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 玩家入局记录 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public interface IJoinPartyLogService extends IService<JoinPartyLog> {
	/**
	 * 处理任务中的入局数据
	 * @param task
	 */
	void taskJoinPartyLog(SystemTask task);
	/**
	 * 增加一条入局记录
	 * @param joinPartyLog
	 * @return
	 */
	boolean insertJoinPartyLog(JoinPartyLog joinPartyLog);
	
	/**
	 * 增加玩家的牌局次数
	 * @param joinPartyLog
	 */
	boolean addPlayerPartyNum(JoinPartyLog joinPartyLog);
	
	/**
	 * 更新房间的进入牌局人数
	 * @param joinPartyLog
	 */
	boolean addRoomPartyNum(JoinPartyLog joinPartyLog);
	
	/**
	 * 增加玩家在房间中的明细
	 * @param joinRoomLog 
	 */
	boolean addPlayerRoom(JoinPartyLog joinPartyLog);
	
	/**
	 * 获取房间牌局次数；PS：如果playerId不为空，查询玩家在这个房间的入局次数
	 * @param playerId 玩家id
	 * @param houseOwner 房主id
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @return
	 */
	Map<String, Object> getRoomPartyNum(String playerId, String houseOwner, int roomId, Date createTime);

	/**
	 * 获取房间入局人数；
	 * @param houseOwner 房主id
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @return
	 */
	Map<String, Object> getRoomPeopleNum(String houseOwner, int roomId, Date createTime);

	/**
	 * 获取加入房间数据汇总
	 * @return
	 */
	Map<String, Object> selectJoinPartyMap();
	
	/**
	 * 获取今日牌局总数
	 * @return
	 */
	Map<String, Object> selectTodayPartys();
	
}
