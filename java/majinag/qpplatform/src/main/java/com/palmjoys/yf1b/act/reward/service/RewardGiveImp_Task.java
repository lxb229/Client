package com.palmjoys.yf1b.act.reward.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.framework.gameobject.model.ServiceType;
import com.palmjoys.yf1b.act.framework.gameobject.service.GameObjectService;
import com.palmjoys.yf1b.act.task.entity.TaskEntity;
import com.palmjoys.yf1b.act.task.manager.TaskManager;

@Service
public class RewardGiveImp_Task implements GameObjectService{
	@Autowired
	private TaskManager taskManager;

	@Override
	public ServiceType serviceType() {
		return ServiceType.TASK;
	}

	@Override
	public int checkEnough(Long traget, GameObject gameObject) {
		TaskEntity taskEntity = taskManager.load(traget);
		if(null == taskEntity){
			return 0;
		}
		if(taskEntity.getDayLuckValue()<gameObject.amount.intValue()){
			return 0;
		}
		return 1;
	}

	@Override
	public int increase(Long traget, GameObject gameObject) {
		TaskEntity taskEntity = taskManager.load(traget);
		if(null == taskEntity){
			return 0;
		}
		int dayLuckValue = taskEntity.getDayLuckValue() + gameObject.amount;
		taskEntity.setDayLuckValue(dayLuckValue);
		return 0;
	}

	@Override
	public int decrease(Long traget, GameObject gameObject) {
		TaskEntity taskEntity = taskManager.load(traget);
		if(null == taskEntity){
			return 0;
		}
		if(taskEntity.getDayLuckValue()<gameObject.amount.intValue()){
			return -1;
		}
		int dayLuckValue = taskEntity.getDayLuckValue() - gameObject.amount;
		taskEntity.setDayLuckValue(dayLuckValue);
		
		return 0;
	}

}
