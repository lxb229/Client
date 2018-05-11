package com.wangzhixuan.service;

import com.wangzhixuan.model.BetLog;
import com.wangzhixuan.model.SystemTask;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 下注记录 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public interface IBetLogService extends IService<BetLog> {
	
	/**
	 * 处理任务中的下注数据
	 * @param task
	 */
	void taskBetLog(SystemTask task);
	/**
	 * 增加一条下注记录
	 * @param joinRoomLog
	 * @return
	 */
	boolean insertBetLog(BetLog betLog);
	
	/**
	 * 增加房间的流水
	 * @param joinRoomLog
	 */
	boolean addRoomFinancialWater(BetLog betLog);
	
	/**
	 * 增加玩家在房间中的明细
	 * @param joinRoomLog 
	 */
	boolean addPlayerRoom(BetLog betLog);
	
	/**
	 * 获取房间内的流水
	 * @param houseOwner 房主id
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @return
	 */
	Map<String, Object> getRoomFinancialWater(String playId, String houseOwner, int roomId, Date createTime);

	/**
	 * 获取流水数据汇总
	 * @return
	 */
	Map<String, Object> selectBetMap();
	
}
