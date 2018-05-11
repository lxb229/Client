package com.palmjoys.yf1b.act.task.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

import com.palmjoys.yf1b.act.task.model.TaskAttrib;
import com.palmjoys.yf1b.act.task.model.TaskStatisticsAttrib;

@Entity
@Memcached
public class TaskEntity implements IEntity<Long>, Lifecycle{
	//帐号Id
	@Id
	private long accountId;
	//每日茁壮度
	@Column(nullable = false)
	private int dayLuckValue;
	//每日任务列表JSON
	@Lob
	@Column(nullable = false)
	private String dayTaskListJson;
	//任务统计数据JSON
	@Lob
	@Column(nullable = false)
	private String taskStatisticsJson;
	//红包福利列表Json
	@Lob
	@Column(nullable = false)
	private String welfareListJson;
	//当前领取红包福利轮数
	@Column(nullable = false)
	private long round;
	//每日任务列表
	@Transient
	private List<TaskAttrib> dayTaskList;
	
	//任务统计数据
	@Transient
	private TaskStatisticsAttrib taskStatistics;
	
	//红包福利列表
	@Transient
	private List<TaskAttrib> welfareList;
		
	public static TaskEntity valueOf(long accountId){
		TaskEntity taskEntity = new TaskEntity();
		taskEntity.accountId = accountId;
		taskEntity.dayLuckValue = 0;
		taskEntity.dayTaskListJson = null;
		taskEntity.dayTaskList = new ArrayList<>();
		taskEntity.welfareListJson = null;
		taskEntity.welfareList = new ArrayList<>();
		taskEntity.taskStatisticsJson = null;
		taskEntity.taskStatistics = new TaskStatisticsAttrib();
		taskEntity.round = 1;
		
		return taskEntity;
	}
	
	public int getDayLuckValue() {
		return dayLuckValue;
	}

	@Enhance
	public void setDayLuckValue(int dayLuckValue) {
		this.dayLuckValue = dayLuckValue;
	}	
	
	public List<TaskAttrib> getWelfareList() {
		return welfareList;
	}

	@Enhance
	public void setWelfareList(List<TaskAttrib> welfareList) {
		this.welfareList = welfareList;
	}

	public List<TaskAttrib> getDayTaskList() {
		return dayTaskList;
	}

	@Enhance
	public void setDayTaskList(List<TaskAttrib> dayTaskList) {
		this.dayTaskList = dayTaskList;
	}

	public TaskStatisticsAttrib getTaskStatistics() {
		return taskStatistics;
	}

	@Enhance
	public void setTaskStatistics(TaskStatisticsAttrib taskStatistics) {
		this.taskStatistics = taskStatistics;
	}

	public long getRound() {
		return round;
	}

	@Enhance
	public void setRound(long round) {
		this.round = round;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(dayTaskListJson)){
			dayTaskList = new ArrayList<>();
		}else{
			dayTaskList = JsonUtils.string2Collection(dayTaskListJson, List.class, TaskAttrib.class);
		}
		if(StringUtils.isBlank(welfareListJson)){
			welfareList = new ArrayList<>();
		}else{
			welfareList = JsonUtils.string2Collection(welfareListJson, List.class, TaskAttrib.class);
		}
		if(StringUtils.isBlank(taskStatisticsJson)){
			taskStatistics = new TaskStatisticsAttrib();
		}else{
			taskStatistics = JsonUtils.string2Object(taskStatisticsJson, TaskStatisticsAttrib.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		dayTaskListJson = JsonUtils.object2String(dayTaskList);
		welfareListJson = JsonUtils.object2String(welfareList);
		taskStatisticsJson = JsonUtils.object2String(taskStatistics);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

	@Override
	public Long getId() {
		return accountId;
	}

}
