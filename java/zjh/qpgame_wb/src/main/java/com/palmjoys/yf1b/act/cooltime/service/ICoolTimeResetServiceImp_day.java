package com.palmjoys.yf1b.act.cooltime.service;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.cooltime.model.CoolTimeCondition;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeType;

/**按每天定义时间重置*/
@Component
public class ICoolTimeResetServiceImp_day implements ICoolTimeResetService{

	@Override
	public CoolTimeResult resetTime(long nextRestTime,
			CoolTimeCondition condition) {

		CoolTimeResult result = new CoolTimeResult();
		
		if(condition.type != getServiceId() || condition == null || condition.conditions==null){
			return result;
		}
		
		long currTime;
		Calendar calendar = Calendar.getInstance();
		currTime = calendar.getTimeInMillis();
		
		long interval = 1*86400*1000;
		if(currTime >= nextRestTime){
			result.interval = (currTime - nextRestTime);
			result.nextTime = ((currTime - ((currTime - nextRestTime)%interval))) + interval;
			result.cooltime = result.nextTime - currTime;
			result.bReset = true;
		}else{
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
		return CoolTimeType.COOLTIME_TYPE_DAY;
	}

	@Override
	public long getFristResetTime(CoolTimeCondition condition) {
		//首次重置,计算一个开始时间
		int year, month, day, rHour, rMinute, rSceond;
		Calendar calendar = Calendar.getInstance();
		
		long currTime = calendar.getTimeInMillis();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DATE);
		rHour = condition.conditions[0].hour; 
		rMinute = condition.conditions[0].minute;
		rSceond = condition.conditions[0].second;
		calendar.set(year, month, day, rHour, rMinute, rSceond);
		long resetTime = calendar.getTimeInMillis();
		if(currTime >= resetTime){
			long interval = 1*86400*1000;
			resetTime = ((currTime - ((currTime - resetTime)%interval))) + interval;
		}
		
		return resetTime;
	}

}
