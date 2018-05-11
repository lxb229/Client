package com.wangzhixuan.service;

import com.wangzhixuan.model.PlayerInsuranceProfit;
import com.wangzhixuan.model.SystemTask;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 玩家保险盈亏 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
public interface IPlayerInsuranceProfitService extends IService<PlayerInsuranceProfit> {
	/**
	 * 处理任务中的保险盈利数据
	 * @param task
	 */
	void taskPlayerInsuranceProfit(SystemTask task);
	/**
	 * 增加一条保险盈利记录
	 * @param joinRoomLog
	 * @return
	 */
	boolean insertPlayerInsuranceProfit(PlayerInsuranceProfit insuranceProfit);
	/**
	 * 增加玩家在房间中的明细
	 * @param joinRoomLog 
	 */
	boolean addPlayerRoom(PlayerInsuranceProfit insuranceProfit);
}
