package com.palmjoys.yf1b.act.task.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.task.model.TaskDefine;

@NetworkFacade
public interface TaskFacade {
	
	@NetworkApi(value = TaskDefine.TASK_COMMAND_GET_WELFARE_LIST,
			desc="获取红包福利列表")
	Object task_get_welfare_list(@InSession Long accountId);

	@NetworkApi(value = TaskDefine.TASK_COMMAND_GET_DAYTASK_LIST,
			desc="获取每日任务列表")
	Object task_get_daytask_list(@InSession Long accountId);
	
	@NetworkApi(value = TaskDefine.TASK_COMMAND_GET_WELFARE_REWARE,
			desc="获取红包福利奖励")
	Object task_get_welfare_reware(@InSession Long accountId, 
			@InBody(value = "taskId", desc = "任务Id") int taskId);
	
	@NetworkApi(value = TaskDefine.TASK_COMMAND_GET_TASK_REWARE,
			desc="获取任务奖励")
	Object task_get_task_reware(@InSession Long accountId, 
			@InBody(value = "taskId", desc = "任务Id") int taskId);
	
	@NetworkApi(value = TaskDefine.TASK_COMMAND_SHAR_FIGHT_SCORE,
			desc="分享一次战绩")
	Object task_shar_fight_score(@InSession Long accountId);
	
	@NetworkApi(value = TaskDefine.TASK_COMMAND_SILVER_REWARE_INFO,
			desc="获取银币抽奖界面信息")
	Object task_silver_reware_info(@InSession Long accountId);
	
	@NetworkApi(value = TaskDefine.TASK_COMMAND_SILVER_REWARE_REFSH,
			desc="银币抽奖刷新物品列表")
	Object task_silver_reware_refsh(@InSession Long accountId);
		
	@NetworkApi(value = TaskDefine.TASK_COMMAND_SILVER_REWARE_GET,
			desc="银币抽奖抽取奖品")
	Object task_silver_reware_get(@InSession Long accountId);
	
	@NetworkApi(value = TaskDefine.TASK_COMMAND_SILVER_ALL_REWARE_ITEM,
			desc="获取银币所有奖励物品列表")
	Object task_silver_reware_all_item(@InSession Long accountId);
	
	@NetworkApi(value = TaskDefine.TASK_COMMAND_GOLD_REWARE_INFO,
			desc="获取金币兑换界面信息")
	Object task_gold_reware_info(@InSession Long accountId);
	
	@NetworkApi(value = TaskDefine.TASK_COMMAND_GOLD_REWARE_ITEM_INFO,
			desc="获取金币物品详情")
	Object task_gold_reware_item_info(@InSession Long accountId,
			@InBody(value = "itemId", desc = "物品Id") int itemId);
	
	@NetworkApi(value = TaskDefine.TASK_COMMAND_GOLD_REWARE_GET,
			desc="金币兑换物品")
	Object task_gold_reware_exchange(@InSession Long accountId,
			@InBody(value = "itemId", desc = "要兑换的物品Id") int itemId,
			@InBody(value = "name", desc = "物品接收者名称") String name,
			@InBody(value = "phone", desc = "物品接收者联系电话") String phone,
			@InBody(value = "addr", desc = "物品发送地址") String addr);
}
