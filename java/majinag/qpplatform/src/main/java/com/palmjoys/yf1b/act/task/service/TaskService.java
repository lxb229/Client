package com.palmjoys.yf1b.act.task.service;

public interface TaskService{
	//获取红包福利任务列表
	public Object task_get_welfare_list(Long accountId);
	//获取每日任务列表
	public Object task_get_daytask_list(Long accountId);
	//领取福利奖励
	public Object task_get_welfare_reware(Long accountId, int taskId);
	//领取每日任务奖励
	public Object task_get_task_reware(Long accountId, int taskId);
	//分享战绩
	public Object task_shar_fight_score(Long accountId);
	//获取银币抽奖界面信息
	public Object task_silver_reware_info(Long accountId);
	//银币抽奖刷新物品列表
	public Object task_silver_reware_refsh(Long accountId);
	//银币抽奖抽取奖品
	public Object task_silver_reware_get(Long accountId);
	//获取银币奖励所有物品列表
	public Object task_silver_reware_all_item(Long accountId);
	//获取金币兑换界面信息
	public Object task_gold_reware_info(Long accountId);
	//获取金币物品详情
	public Object task_gold_reware_item_info(Long accountId, int itemId);
	//金币兑换物品
	public Object task_gold_reware_exchange(Long accountId, int itemId, String name, String phone, String addr);
}
