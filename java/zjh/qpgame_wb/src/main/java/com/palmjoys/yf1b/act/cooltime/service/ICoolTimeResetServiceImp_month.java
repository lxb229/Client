package com.palmjoys.yf1b.act.cooltime.service;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.cooltime.model.CoolTimeCondition;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeType;

/**按月定义时间重置*/
@Component
public class ICoolTimeResetServiceImp_month implements ICoolTimeResetService{

	@Override
	public CoolTimeResult resetTime(long nextRestTime,
			CoolTimeCondition condition) {
		
		CoolTimeResult result = new CoolTimeResult();
		
		if(condition.type != getServiceId() || condition == null || condition.conditions==null){
			return result;
		}
		
		Calendar calendar = Calendar.getInstance();
		long currTime = calendar.getTimeInMillis();
		
		if(currTime >= nextRestTime){
			int year, month, rDay, rHour, rMinute, rSecond;
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			rDay = calendar.get(Calendar.DATE);
			rHour = calendar.get(Calendar.HOUR_OF_DAY);
			rMinute = calendar.get(Calendar.MINUTE);
			rSecond = calendar.get(Calendar.SECOND);
			
			if(condition.conditions[0].day != -1){
				rDay = condition.conditions[0].day;
			}
			if(condition.conditions[0].hour != -1){
				rHour = condition.conditions[0].hour;
			}
			if(condition.conditions[0].minute != -1){
				rMinute = condition.conditions[0].minute;
			}
			if(condition.conditions[0].second != -1){
				rSecond = condition.conditions[0].second;
			}
			
			month = month + 1;
			if(month >= 12){
				month = 0;
				year = year + 1;
			}
			calendar.set(year, month, rDay, rHour, rMinute, rSecond);
			result.interval = currTime - nextRestTime;
			result.nextTime = calendar.getTimeInMillis();
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
		return CoolTimeType.COOLTIME_TYPE_MONTH;
	}

	@Override
	public long getFristResetTime(CoolTimeCondition condition) {
		Calendar calendar = Calendar.getInstance();
		long currTime = calendar.getTimeInMillis();
		
		int year, month, rDay, rHour, rMinute, rSecond;
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		rDay = calendar.get(Calendar.DATE);
		rHour = calendar.get(Calendar.HOUR_OF_DAY);
		rMinute = calendar.get(Calendar.MINUTE);
		rSecond = calendar.get(Calendar.SECOND);
		
		if(condition.conditions[0].day != -1){
			rDay = condition.conditions[0].day;
		}
		if(condition.conditions[0].hour != -1){
			rHour = condition.conditions[0].hour;
		}
		if(condition.conditions[0].minute != -1){
			rMinute = condition.conditions[0].minute;
		}
		if(condition.conditions[0].second != -1){
			rSecond = condition.conditions[0].second;
		}		
		//本月重置点
		calendar.set(year, month, rDay, rHour, rMinute, rSecond);
		long retsetTime = calendar.getTimeInMillis();
		if(currTime >= retsetTime){			
			month = month + 1;
			if(month >= 12){
				month = 0;
				year = year + 1;
			}
			calendar.set(year, month, rDay, rHour, rMinute, rSecond);
			retsetTime = calendar.getTimeInMillis();
		}
		
		return retsetTime;
	}

}
