package com.palmjoys.yf1b.act.cooltime.service;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.cooltime.model.CoolTimeCondition;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeType;

/**按周定义时间重置*/
@Component
public class ICoolTimeResetServiceImp_week implements ICoolTimeResetService{

	@Override
	public CoolTimeResult resetTime(long nextRestTime,
			CoolTimeCondition condition) {
		
		CoolTimeResult result = new CoolTimeResult();
		
		if(condition.type != getServiceId() || condition == null || condition.conditions==null){
			return result;
		}
				
		Calendar calendar = Calendar.getInstance();
		long currTime = calendar.getTimeInMillis();		
		long interval = 7*86400*1000;
		if(currTime >= nextRestTime){			
			result.interval = (currTime - nextRestTime);
			result.nextTime = ((currTime - ((currTime - nextRestTime)%interval))) + interval;
			result.cooltime = result.nextTime - currTime;
			result.bReset = true;
		}
		else{
			result.nextTime = nextRestTime;
			result.cooltime = nextRestTime - currTime;
			result.interval = 0;
		}
		
		//转为秒,向上取整,大于0小于1,取为1
		result.interval = (long) Math.ceil(result.interval*1.0/1000);
		result.cooltime = (long) Math.ceil(result.cooltime*1.0/1000);
		
		return result;
	}

	@Override
	public int getServiceId() {
		return CoolTimeType.COOLTIME_TYPE_WEEK;
	}

	@Override
	public long getFristResetTime(CoolTimeCondition condition) {
		Calendar calendar = Calendar.getInstance();
		int year, month, day, rWeek, rHour, rMinute, rSceond;
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DATE);
		rWeek = calendar.get(Calendar.DAY_OF_WEEK);
		rHour = calendar.get(Calendar.HOUR_OF_DAY);
		rMinute = calendar.get(Calendar.MINUTE);
		rSceond = calendar.get(Calendar.SECOND);
		long currTime = calendar.getTimeInMillis();
		
		if(condition.conditions[0].week != -1){
			rWeek = condition.conditions[0].week;
		}	
		if(condition.conditions[0].hour != -1){
			rHour = condition.conditions[0].hour; 
		}
		if(condition.conditions[0].minute != -1){
			rMinute = condition.conditions[0].minute;
		}
		if(condition.conditions[0].second != -1){
			rSceond = condition.conditions[0].second;
		}
		calendar.set(Calendar.DAY_OF_WEEK, rWeek);
		day = calendar.get(Calendar.DATE);
		calendar.set(year, month, day, rHour, rMinute, rSceond);
		long resetTime = calendar.getTimeInMillis();
		if(currTime >= resetTime){
			//需要重置了
			long interval = 7*86400*1000;
			resetTime = ((currTime - ((currTime - resetTime)%interval))) + interval;
		}
		
		return resetTime;
	}

}
