package com.palmjoys.yf1b.act.task.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "任务模块")
public interface TaskDefine {
	@SocketModule("任务模块")
	int TASK = 10;
	
	static final int TASK_COMMAND_BASE = TASK * 100;
	static final int TASK_ERROR_BASE = (0 - TASK) * 1000;
	static final int TASK_COMMAND_BASE_NOTIFY = TASK * 10000;
	
	//获取红包福利任务列表
	int TASK_COMMAND_GET_WELFARE_LIST = TASK_COMMAND_BASE + 1;
	//获取每日任务列表
	int TASK_COMMAND_GET_DAYTASK_LIST = TASK_COMMAND_BASE + 2;
	//领取福利奖励
	int TASK_COMMAND_GET_WELFARE_REWARE = TASK_COMMAND_BASE + 3;
	//领取每日任务奖励
	int TASK_COMMAND_GET_TASK_REWARE = TASK_COMMAND_BASE + 4;
	//分享战绩
	int TASK_COMMAND_SHAR_FIGHT_SCORE = TASK_COMMAND_BASE + 5;
	//获取银币抽奖界面信息
	int TASK_COMMAND_SILVER_REWARE_INFO = TASK_COMMAND_BASE + 6;
	//银币抽奖刷新物品列表
	int TASK_COMMAND_SILVER_REWARE_REFSH = TASK_COMMAND_BASE + 7;
	//银币抽奖抽取奖品
	int TASK_COMMAND_SILVER_REWARE_GET = TASK_COMMAND_BASE + 8;
	//获取银币抽奖所有物品列表
	int TASK_COMMAND_SILVER_ALL_REWARE_ITEM = TASK_COMMAND_BASE + 9;
	//获取金币兑换界面信息
	int TASK_COMMAND_GOLD_REWARE_INFO = TASK_COMMAND_BASE + 10;
	//获取金币物品详请
	int TASK_COMMAND_GOLD_REWARE_ITEM_INFO = TASK_COMMAND_BASE + 11;
	//金币兑换物品
	int TASK_COMMAND_GOLD_REWARE_GET = TASK_COMMAND_BASE + 12;
}
