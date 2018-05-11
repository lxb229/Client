package com.palmjoys.yf1b.act.task.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;
import com.palmjoys.yf1b.act.task.resource.WelfareRewareConfig;
import com.palmjoys.yf1b.act.task.service.TaskService;

@Component
@ConsoleBean
public class TaskCommand {
	@Autowired
	private TaskService taskService;
	@Static
	private Storage<Integer, WelfareRewareConfig> welfareRewareCfgs;
	
	@ConsoleCommand(name = "task_silver_reware_info", description = "奖励测试")
	public Object task_silver_reware_info(long accountId){
		return taskService.task_silver_reware_info(accountId);
	}
	
	@ConsoleCommand(name = "task_silver_reware_refsh", description = "奖励测试")
	public Object task_silver_reware_refsh(long accountId){
		return taskService.task_silver_reware_refsh(accountId);
	}
	
	@ConsoleCommand(name = "task_silver_reware_get", description = "奖励测试")
	public Object task_silver_reware_get(long accountId){
		return taskService.task_silver_reware_get(accountId);
	}
		
	@ConsoleCommand(name = "task_silver_reware_all_item", description = "奖励测试")
	public Object task_silver_reware_all_item(long accountId){
		return taskService.task_silver_reware_all_item(accountId);
	}
	
	@ConsoleCommand(name = "task_gold_reware_info", description = "奖励测试")
	public Object task_gold_reware_info(long accountId){
		return taskService.task_gold_reware_info(accountId);
	}
	
	@ConsoleCommand(name = "task_gold_reware_item_info", description = "奖励测试")
	public Object task_gold_reware_item_info(long accountId, int itemId){
		return taskService.task_gold_reware_item_info(accountId, itemId);
	}
	
	@ConsoleCommand(name = "task_gold_reware_exchange", description = "奖励测试")
	public Object task_gold_reware_exchange(long accountId, int itemId, String name, String phone, String addr){
		return taskService.task_gold_reware_exchange(accountId, itemId, name, phone, addr);
	}
}
