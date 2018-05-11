package com.palmjoys.yf1b.act.task.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.task.service.TaskService;

@Component
public class TaskFacadeImp implements TaskFacade{
	@Autowired
	private TaskService taskService;

	@Override
	public Object task_get_welfare_list(Long accountId) {
		return taskService.task_get_welfare_list(accountId);
	}

	@Override
	public Object task_get_daytask_list(Long accountId) {
		return taskService.task_get_daytask_list(accountId);
	}

	@Override
	public Object task_get_welfare_reware(Long accountId, int taskId) {
		return taskService.task_get_welfare_reware(accountId, taskId);
	}

	@Override
	public Object task_get_task_reware(Long accountId, int taskId) {
		return taskService.task_get_task_reware(accountId, taskId);
	}

	@Override
	public Object task_shar_fight_score(Long accountId) {
		return taskService.task_shar_fight_score(accountId);
	}

	@Override
	public Object task_silver_reware_info(Long accountId) {
		return taskService.task_silver_reware_info(accountId);
	}

	@Override
	public Object task_silver_reware_refsh(Long accountId) {
		return taskService.task_silver_reware_refsh(accountId);
	}

	@Override
	public Object task_silver_reware_get(Long accountId) {
		return taskService.task_silver_reware_get(accountId);
	}

	@Override
	public Object task_silver_reware_all_item(Long accountId) {
		return taskService.task_silver_reware_all_item(accountId);
	}

	@Override
	public Object task_gold_reware_info(Long accountId) {
		return taskService.task_gold_reware_info(accountId);
	}

	@Override
	public Object task_gold_reware_item_info(Long accountId, int itemId) {
		return taskService.task_gold_reware_item_info(accountId, itemId);
	}

	@Override
	public Object task_gold_reware_exchange(Long accountId, int itemId, String name, String phone, String addr) {
		return taskService.task_gold_reware_exchange(accountId, itemId, name, phone, addr);
	}
}
