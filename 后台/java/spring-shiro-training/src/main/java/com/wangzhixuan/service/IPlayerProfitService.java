package com.wangzhixuan.service;

import com.wangzhixuan.model.PlayerProfit;
import com.wangzhixuan.model.SystemTask;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 玩家盈亏记录 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
public interface IPlayerProfitService extends IService<PlayerProfit> {
	/**
	 * 处理任务中的玩家盈利数据
	 * @param task
	 */
	void taskPlayerProfit(SystemTask task);
	/**
	 * 增加一条玩家盈利记录
	 * @param joinRoomLog
	 * @return
	 */
	boolean insertPlayerProfit(PlayerProfit playerProfit);
	/**
	 * 增加玩家的盈利
	 * @param joinPartyLog
	 */
	boolean addPlayerProfit(PlayerProfit playerProfit);
	/**
	 * 增加玩家在房间中的明细
	 * @param joinRoomLog 
	 */
	boolean addPlayerRoom(PlayerProfit playerProfit);
}
