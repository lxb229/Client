package com.palmjoys.yf1b.act.task.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskVo {
	//茁壮度
	public int dayLuckValue;
	//红包福利列表
	public List<TaskItemInner> welfareList = new ArrayList<>();
	//每日任务列表
	public List<TaskItemInner> dayTaskList = new ArrayList<>();
	
	public void addTaskItem(int type, int taskId, int state, String finshDesc, String rewardDesc, int total, int complete, int sort){
		TaskItemInner item = new TaskItemInner();
		item.taskId = taskId;
		item.state = state;
		item.finshDesc = finshDesc;
		item.rewardDesc = rewardDesc;
		item.total = total;
		item.complete = complete;
		item.sort = sort;
		
		if(type == 1){
			dayTaskList.add(item);
		}else{
			welfareList.add(item);
		}
	}
		
	public void sortDayTask(){
		//按可领取,未完成,已完成,排序Id,任务Id排序
		dayTaskList.sort(new Comparator<TaskItemInner>(){
			@Override
			public int compare(TaskItemInner arg0, TaskItemInner arg1) {
				if(arg0.state > arg1.state){
					return 1;
				}else if(arg0.state < arg1.state){
					return -1;
				}else{
					if(arg0.sort > arg1.sort){
						return 1;
					}else if(arg0.sort < arg1.sort){
						return -1;
					}else{
						if(arg0.taskId > arg1.taskId){
							return 1;
						}else if(arg0.taskId < arg1.taskId){
							return -1;
						}else{
							return 0;
						}
					}
				}
			}
		});
	}
	
	public void sortWelfare(){
		welfareList.sort(new Comparator<TaskItemInner>(){
			@Override
			public int compare(TaskItemInner arg0, TaskItemInner arg1) {
				if(arg0.sort > arg1.sort){
					return 1;
				}else if(arg0.sort < arg1.sort){
					return -1;
				}else{
					return 0;
				}
			}
		});
	}
	
	public class TaskItemInner{
		//任务Id
		public int taskId;
		//任务完成状态(0=未完成,1=可领取,2=已领取)
		public int state;
		//任务完成条件描述
		public String finshDesc;
		//奖励描述
		public String rewardDesc;
		//总需完成进度
		public int total;
		//当前已完成
		public int complete;
		//排序
		public int sort;
	}
}
