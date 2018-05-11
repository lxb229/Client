package com.wangzhixuan.service;

import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.SystemTask;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 加入房间日志 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
public interface IJoinRoomLogService extends IService<JoinRoomLog> {
	/**
	 * 获取玩家最初的进入房间记录
	 * @param playerId
	 * @param roomId
	 * @param houseOwner
	 * @param createTime
	 * @return
	 */
	JoinRoomLog getJoinRoomLogBy(String playerId, int roomId, String houseOwner, Date createTime);
	
	/**
	 * 处理任务中的加入房间数据
	 * @param task
	 */
	void taskJoinRoomLog(SystemTask task);
	/**
	 * 增加一条进入房间记录
	 * @param joinRoomLog
	 * @return
	 */
	boolean insertJoinRoomLog(JoinRoomLog joinRoomLog);
	/**
	 * 增加玩家的加入房间次数
	 * @param joinRoomLog
	 */
	boolean addPlayerJoinNum(JoinRoomLog joinRoomLog);
	/**
	 * 增加房间的加入人数
	 * @param joinRoomLog
	 */
	boolean addRoomJoinNum(JoinRoomLog joinRoomLog);
	/**
	 * 增加玩家在房间中的明细
	 * @param joinRoomLog 
	 */
	boolean addPlayerRoom(JoinRoomLog joinRoomLog);
	/**
	 * 获取加入人数
	 * @param houseOwner
	 * @param roomId
	 * @param createTime
	 * @return
	 */
	Map<String, Object> getRoomJoinNum(String houseOwner, int roomId, Date createTime);
	
	/**
	 * 获取加入房间数据汇总
	 * @return
	 */
	Map<String, Object> selectJoinRoomMap();
	
}
