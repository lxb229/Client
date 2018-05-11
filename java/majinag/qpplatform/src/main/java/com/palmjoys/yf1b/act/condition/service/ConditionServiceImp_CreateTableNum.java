package com.palmjoys.yf1b.act.condition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palmjoys.yf1b.act.condition.model.ConditionAttrib;
import com.palmjoys.yf1b.act.condition.model.ConditionCheckResult;
import com.palmjoys.yf1b.act.condition.model.ConditionDefine;
import com.palmjoys.yf1b.act.task.entity.TaskEntity;
import com.palmjoys.yf1b.act.task.manager.TaskManager;
import com.palmjoys.yf1b.act.task.model.TaskStatisticsAttrib;

@Service
public class ConditionServiceImp_CreateTableNum implements ConditionService{
	@Autowired
	private TaskManager taskManager;

	@Override
	public int getConditionId() {
		return ConditionDefine.CONDITION_CREATE_TABLE_NUM;
	}

	@Override
	public ConditionCheckResult checkCondition(Long playerId, ConditionAttrib condition) {
		ConditionCheckResult ret = new ConditionCheckResult();
		
		TaskEntity taskEntity = taskManager.load(playerId);
		if(null == taskEntity){
			return ret; 
		}
		TaskStatisticsAttrib taskStatisticsAttrib = taskEntity.getTaskStatistics();
		ret.total = condition.condition[0];
		ret.complete = taskStatisticsAttrib.dayCreateTable;		
		return ret;
	}

}
