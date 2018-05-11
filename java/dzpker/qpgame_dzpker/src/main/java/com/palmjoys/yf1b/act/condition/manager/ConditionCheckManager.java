package com.palmjoys.yf1b.act.condition.manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.condition.model.ConditionAttrib;
import com.palmjoys.yf1b.act.condition.model.ConditionCheckResult;
import com.palmjoys.yf1b.act.condition.service.ConditionService;

@Component
public class ConditionCheckManager implements ApplicationContextAware{
	private ApplicationContext applicationContext;
	private Map<Integer, ConditionService> conditionServices = new HashMap<Integer, ConditionService>();
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;		
	}
	
	@PostConstruct
	private void init(){
		for(ConditionService service : applicationContext.getBeansOfType(ConditionService.class).values()){
			if(conditionServices.containsKey(service.getConditionId()) == false){
				conditionServices.put(service.getConditionId(), service);
			}
		}
	}
	
	/**
	 * 检测条件是否完成
	 * */
	public ConditionCheckResult checkCondition(Long playerId, ConditionAttrib []conditions){
		ConditionCheckResult ret = new ConditionCheckResult();
		if(conditions == null || conditions.length == 0){
			ret.complete = ret.total;
			return ret;
		}
		ConditionCheckResult progessResult = null;
		int complete = 0;
		for(ConditionAttrib condition : conditions){
			ConditionService service = conditionServices.get(condition.eventId);
			if(null != service){
				progessResult = service.checkCondition(playerId, condition);
				if(progessResult.complete >= progessResult.total){
					complete++;
				}
			}
		}
		if(complete == conditions.length){
			//条件已达到
			if(complete == 1){
				//单条件
				return progessResult;
			}else{
				//多条件
				ret.complete = ret.total;
				return ret;
			}
		}else{
			//没有达到条件
			if(conditions.length == 1){
				//单条件
				if(progessResult == null){
					return ret;
				}else{
					return progessResult;
				}
			}else{
				//多条件
				return ret;
			}
		}
	}
	

}
