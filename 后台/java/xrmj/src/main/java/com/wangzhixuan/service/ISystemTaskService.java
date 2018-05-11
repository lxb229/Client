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
	 * 处理订单数据消息
	 * @param content
	 */
	Result insertOrder(String content);
	/**
	 * 处理游戏数据消息
	 * @param content
	 */
	Result insertGamePlayer(String content);
	
	/**
	 * 处理玩家祝福值数据消息
	 * @param content
	 */
	Result insertPlayerWish(String content);
	
	/**
	 * 处理麻将馆数据消息
	 * @param content
	 */
	Result insertMahjong(String content);
	
	/**
	 * 处理麻将馆成员数据消息
	 * @param content
	 * @return
	 */
	Result insertMahjongPlayer(String content);
	
	/**
	 * 处理麻将馆房卡数据消息
	 * @param content
	 * @return
	 */
	Result insertMahjongCard(String content);
	
	/**
	 * 处理麻将馆战斗数据消息
	 * @param content
	 * @return
	 */
	Result insertMahjongCombat(String content);
	
	/**
	 * 处理玩家消耗幸运值数据消息
	 * @param content
	 * @return
	 */
	Result insertUserLuck(String content);
	
	
}
