package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.SystemTask;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 系统任务表 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-09
 */
public interface ISystemTaskService extends IService<SystemTask> {
	/**
	 * 处理服务器发送的定时任务
	 * @return
	 */
	Result deal(Integer taskCmd, String jsonStr);
	/**
	 * 处理玩家数据消息
	 * @param content
	 */
	Result insertPlayer(String content);
	/**
	 * 处理房间数据消息
	 * @param content
	 */
	Result insertRoom(String content);
	/**
	 * 处理登录日志消息
	 * @param content
	 */
	Result insertLoginLog(String content);
	/**
	 * 处理进入房间消息
	 * @param content
	 */
	Result insertJoinRoom(String content);
	/**
	 * 处理入局消息
	 * @param content
	 */
	Result insertJoinParty(String content);
	/**
	 * 处理申请筹码消息
	 * @param content
	 */
	Result insertJettonLog(String content);
	/**
	 * 处理下注消息
	 * @param content
	 */
	Result insertBetLog(String content);
	/**
	 * 处理保险消息
	 * @param content
	 */
	Result insertInsuranceLog(String content);
	/**
	 * 处理保险盈亏消息
	 * @param content
	 */
	Result insertInsuranceProfit(String content);
	/**
	 * 处理玩家盈亏消息
	 * @param content
	 */
	Result insertPlayerProfit(String content);
	/**
	 * 处理房间解散消息
	 * @param content
	 */
	Result insertRoomDisappear(String content);
}
