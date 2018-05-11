package com.palmjoys.yf1b.act.task.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.task.entity.TaskEntity;
import com.palmjoys.yf1b.act.task.model.RewareGoldMoneyItem;
import com.palmjoys.yf1b.act.task.model.TaskAttrib;
import com.palmjoys.yf1b.act.task.model.TaskStatisticsAttrib;
import com.palmjoys.yf1b.act.task.resource.TaskConfig;
import com.palmjoys.yf1b.act.task.service.TaskService;

@Component
public class TaskManager {
	@Inject
	private EntityMemcache<Long, TaskEntity> taskCache;
	@Static
	private Storage<Integer, TaskConfig> taskConfigs;
	@Autowired
	private TaskService taskService;
	//任务完成状态
	//可领取
	public static int TASK_COMPLETE_STATE_FINSH = 1;
	//未完成
	public static int TASK_COMPLETE_STATE_NONE = 2;
	//已领取
	public static int TASK_COMPLETE_STATE_GET = 3;
	
	//银币奖励刷新消耗
	private int silverRewareRefshCost = 0;
	//银币奖励抽取消耗
	private int silverRewareDrawCost = 0;
	//服务器配置当前所有金币物品列表
	private List<RewareGoldMoneyItem> goldItemList;
	
	public TaskEntity loadOrCreate(long accountId){
		return taskCache.loadOrCreate(accountId, new EntityBuilder<Long, TaskEntity>(){
			@Override
			public TaskEntity createInstance(Long pk) {
				TaskEntity taskEntity = TaskEntity.valueOf(accountId);
				initEntity(taskEntity);
				return taskEntity;
			}
		});
	}
	
	public TaskEntity load(long accountId){
		return taskCache.load(accountId);
	}
	
	private void initEntity(TaskEntity taskEntity){
		List<TaskAttrib> dayTaskList = taskEntity.getDayTaskList();
		List<TaskAttrib> welfareList = taskEntity.getWelfareList();
		
		Collection<TaskConfig> cfgs = taskConfigs.getAll();
		for(TaskConfig cfg : cfgs){
			TaskAttrib TaskAttrib = new TaskAttrib();
			TaskAttrib.taskId = cfg.getId();
			TaskAttrib.state = TaskManager.TASK_COMPLETE_STATE_NONE;
			if(cfg.getType() == 1){
				dayTaskList.add(TaskAttrib);
			}else if(cfg.getType() == 2){
				welfareList.add(TaskAttrib);
			}
		}
		
		taskEntity.setDayTaskList(dayTaskList);
		taskEntity.setWelfareList(welfareList);
	}
	
	public void resetDaytask(long accountId){
		TaskEntity taskEntity = this.load(accountId);
		if(null == taskEntity){
			return;
		}
		List<TaskAttrib> dayTaskList = taskEntity.getDayTaskList();
		dayTaskList.clear();
		
		Collection<TaskConfig> cfgs = taskConfigs.getAll();
		for(TaskConfig cfg : cfgs){
			if(cfg.getType() != 1){
				//非每日任务跳过
				continue;
			}
			
			TaskAttrib TaskAttrib = new TaskAttrib();
			TaskAttrib.taskId = cfg.getId();
			TaskAttrib.state = TaskManager.TASK_COMPLETE_STATE_NONE;
			dayTaskList.add(TaskAttrib);
		}
		taskEntity.setDayTaskList(dayTaskList);
		taskEntity.setDayLuckValue(0);
		
		TaskStatisticsAttrib taskStatistics = taskEntity.getTaskStatistics();
		taskStatistics.reset();
		taskEntity.setTaskStatistics(taskStatistics);
	}

	public int getSilverRewareRefshCost() {
		return silverRewareRefshCost;
	}

	public void setSilverRewareRefshCost(int silverRewareRefshCost) {
		this.silverRewareRefshCost = silverRewareRefshCost;
	}

	public int getSilverRewareDrawCost() {
		return silverRewareDrawCost;
	}

	public void setSilverRewareDrawCost(int silverRewareDrawCost) {
		this.silverRewareDrawCost = silverRewareDrawCost;
	}

	public List<RewareGoldMoneyItem> getGoldItemList() {
		List<RewareGoldMoneyItem> retList = new ArrayList<>();
		retList.addAll(goldItemList);
		return retList;
	}

	public void setGoldItemList(List<RewareGoldMoneyItem> goldItemList) {
		this.goldItemList = goldItemList;
	}

	//检查任务红点
	public int checkHotPrompt(long accountId){
		taskService.task_get_daytask_list(accountId);
		TaskEntity taskEntity = this.loadOrCreate(accountId);
		List<TaskAttrib> taskList = taskEntity.getDayTaskList();
		for(TaskAttrib taskAttrib : taskList){
			if(taskAttrib.state == TaskManager.TASK_COMPLETE_STATE_FINSH){
				return 1;
			}
		}
		return 0;
	}
}
