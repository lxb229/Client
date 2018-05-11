/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treediagram.nina.core.time;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.WEEK_OF_YEAR;
import static java.util.Calendar.YEAR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.scheduling.support.CronSequenceGenerator;
import org.treediagram.nina.core.math.MathUtils;

/**
 * 日期工具类，用于简化程序中的日期处理
 * 
 * @author kidal
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	/**
	 * 日期时间格式:年-月-日 时:分:秒[2011-5-5 20:00:00]
	 */
	public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期格式:年-月-日[2011-05-05]
	 */
	public static final String PATTERN_DATE = "yyyy-MM-dd";

	/**
	 * 时间格式:时:分:秒[20:00:00]
	 */
	public static final String PATTERN_TIME = "HH:mm:ss";

	/**
	 * 短时间格式:时:分[20:00]
	 */
	public static final String PATTERN_SHORT_TIME = "HH:mm";

	/**
	 * 一个很久很久以前的时间(格林威治的起始时间. 1970-01-01 00:00:00)
	 */
	public static final Date LONG_BEFORE_TIME = string2Date("1970-01-01 00:00:00", PATTERN_DATE_TIME);

	/**
	 * 一个很久很久以后的时间(该框架可能被遗弃的时间. 2048-01-01 00:00:00)
	 */
	public static final Date LONG_AFTER_TIME = string2Date("2048-01-01 00:00:00", PATTERN_DATE_TIME);

	/**
	 * 检查当前时间和指定时间是否同一周
	 * 
	 * @param year 年
	 * @param week 周
	 * @param firstDayOfWeek 周的第一天设置值，{@link Calendar#DAY_OF_WEEK}
	 */
	public static boolean isSameWeek(int year, int week, int firstDayOfWeek) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(firstDayOfWeek);
		return year == cal.get(YEAR) && week == cal.get(WEEK_OF_YEAR);
	}

	/**
	 * 检查当前时间和指定时间是否同一周
	 * 
	 * @param time 被检查的时间
	 * @param firstDayOfWeek 周的第一天设置值，{@link Calendar#DAY_OF_WEEK}
	 * @return {@link Boolean} 是否同一周. true-是, false-不是
	 */
	public static boolean isSameWeek(Date time, int firstDayOfWeek) {
		if (time == null) {
			return false;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.setFirstDayOfWeek(firstDayOfWeek);
		return isSameWeek(cal.get(YEAR), cal.get(WEEK_OF_YEAR), firstDayOfWeek);
	}

	/**
	 * 获取周的第一天
	 * 
	 * @param firstDayOfWeek 周的第一天设置值，{@link Calendar#DAY_OF_WEEK}
	 * @param time 指定时间，为 null 代表当前时间
	 * @return {@link Date} 周的第一天
	 */
	public static Date firstTimeOfWeek(int firstDayOfWeek, Date time) {
		Calendar cal = Calendar.getInstance();
		if (time != null) {
			cal.setTime(time);
		}

		cal.setFirstDayOfWeek(firstDayOfWeek);
		int day = cal.get(DAY_OF_WEEK);
		if (day == firstDayOfWeek) {
			day = 0;
		} else if (day < firstDayOfWeek) {
			day = day + (7 - firstDayOfWeek);
		} else if (day > firstDayOfWeek) {
			day = day - firstDayOfWeek;
		}

		cal.set(HOUR_OF_DAY, 0);
		cal.set(MINUTE, 0);
		cal.set(SECOND, 0);
		cal.set(MILLISECOND, 0);

		cal.add(DATE, -day);
		return cal.getTime();
	}

	/**获取今日凌晨时间**/
	public  static  Date  getDaybreakthe  (  )
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new  Date(System.currentTimeMillis()));
		cal.set(HOUR_OF_DAY, 0);
		cal.set(MINUTE, 0);
		cal.set(SECOND, 0);
		cal.set(MILLISECOND, 0);
		
		
		return  cal.getTime();
		
	}
	
	
	/**
	 * 检查指定日期是否今天(使用系统时间)
	 * 
	 * @param date 被检查的日期
	 * @return {@link Boolean} 是否今天, true-今天, false-不是今天
	 */
	public static boolean isToday(Date date) {
		if (date == null) {
			return false;
		}
		return isSameDay(date, new Date());
	}

	/**
	 * 日期转换成字符串格式
	 * 
	 * @param date 待转换的日期
	 * @param pattern 日期格式
	 * @return {@link String} 日期字符串
	 */
	public static String date2String(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 字符串转换成日期格式
	 * 
	 * @param string 待转换的日期字符串
	 * @param pattern 日期格式
	 * @return {@link Date} 转换后的日期
	 */
	public static Date string2Date(String string, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(string);
		} catch (ParseException e) {
			throw new IllegalArgumentException("无法将字符串[" + string + "]按格式[" + pattern + "]转换为日期", e);
		}
	}

	/**
	 * 对一个具体的时间增加时间
	 * 
	 * @param source 需要修改的时间
	 * @param hours 需要增加或者减少的小时
	 * @param minutes 需要增加或者减少的分
	 * @param second 需要增加或者减少的秒
	 * @return {@link Date} 返回修改过的时间
	 */
	public static Date addTime(Date source, int hours, int minutes, int second) {
		if (source == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		cal.add(Calendar.HOUR_OF_DAY, hours);
		cal.add(Calendar.MINUTE, minutes);
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}
	
	
	public static int  get_DAY_OF_WEEK() {
		Date source  =  new  Date(System.currentTimeMillis()) ;

		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		return cal.get(DAY_OF_WEEK);
	}

	/**
	 * 获取某日的开始时间，即获得某一时间的0点
	 * 
	 * @param date 需要计算的时间
	 */
	public static Date getFirstTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取某日期的结束时间，即23:59:59:999
	 * 
	 * @param date 需要计算的时间
	 */
	public static Date getLastTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获得指定时间的下一个0点
	 * 
	 * @param date 需要计算的时间
	 */
	public static Date getNextDayFirstTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime() + MILLIS_PER_DAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 计算2个时间相差的天数,这个方法算的是2个零点时间的绝对时间(天数)
	 * 
	 * @param start 起始时间
	 * @param end 结束时间
	 * @return 相差的天数
	 */
	public static int calcIntervalDays(Date start, Date end) {
		Date startDate0AM = getFirstTime(start);
		Date endDate0AM = getFirstTime(end);
		long subValue = startDate0AM.getTime() - endDate0AM.getTime();
		return Math.abs((int) MathUtils.divideAndRoundUp(subValue, MILLIS_PER_DAY, 0));
	}

	/**
	 * 计算2个时间差
	 * 
	 * @param start 起始时间
	 * @param end 结束时间
	 * @param filed 要计算的时间差类型
	 * @return 相差的时间
	 */
	public static int calcIntervalTime(Date start, Date end, int filed) {
		long subTimeInMillise = Math.abs(start.getTime() - end.getTime());
		long millisType = MILLIS_PER_SECOND;
		switch (filed) {
		case Calendar.SECOND:
			break;
		case Calendar.MINUTE:
			millisType = MILLIS_PER_MINUTE;
			break;
		case Calendar.HOUR:
			millisType = MILLIS_PER_HOUR;
			break;
		case Calendar.DATE:
			millisType = MILLIS_PER_DAY;
			break;
		default:
			throw new IllegalStateException("不支持的时间类型");
		}

		return (int) MathUtils.divideAndRoundUp(subTimeInMillise, millisType, 0);
	}

	/**
	 * 获取指定CRON表达式的下一个时间点
	 * 
	 * @param cron CRON表达式
	 * @param now 基准时间点
	 * @return 下一个时间点
	 */
	public static Date getNextTime(String cron, Date now) {
		CronSequenceGenerator gen = new CronSequenceGenerator(cron, TimeZone.getDefault());
		Date time = gen.next(now);
		return time;
	}

	/**
	 * 比较两个日期是否相等（毫秒数）
	 */
	public static boolean isEquals(Date date1, Date date2) {
		if (date1 == null && date2 == null) {
			return true;
		}
		if (date1 == null && date2 != null) {
			return false;
		}
		if (date1 != null && date2 == null) {
			return false;
		}
		return date1.getTime() == date2.getTime();
	}
	
	/**
	 * 返回指定时间的毫秒时间
	 * 所有参数取值为-1返回当前时间毫秒数
	 * */
	public static long getTime(int year, int month, int day, int hour, int minute, int second){
		
		int nYear, nMonth, nDay, nHour, nMinute, nSecond;
		Calendar calendar = Calendar.getInstance();
		nYear = calendar.get(Calendar.YEAR);
		nMonth = calendar.get(Calendar.MONTH);
		nDay = calendar.get(Calendar.DATE);
		nHour = calendar.get(Calendar.HOUR_OF_DAY);
		nMinute = calendar.get(Calendar.MINUTE);
		nSecond = calendar.get(Calendar.SECOND);
		
		if(year >= 1970)
			nYear = year;
		if(month>0 && month<13)
			nMonth = month-1;
		if(day>0 && day<32)
			nDay = day;
		if(hour>=0 && hour<24)
			nHour = hour;
		if(minute>=0 && minute<60)
			nMinute = minute;
		if(second>=0 && second<60)
			nSecond = second;
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.set(nYear, nMonth, nDay, nHour, nMinute, nSecond);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 以秒为单位与系统时间比较
	 * 大于等于系统时间返回true
	 * */
	public static boolean compareTime(int year, int month, int day, int hour, int minute, int second){
		int nYear, nMonth, nDay, nHour, nMinute, nSecond;
		Calendar calendar = Calendar.getInstance();
		nYear = calendar.get(Calendar.YEAR);
		nMonth = calendar.get(Calendar.MONTH);
		nDay = calendar.get(Calendar.DATE);
		nHour = calendar.get(Calendar.HOUR_OF_DAY);
		nMinute = calendar.get(Calendar.MINUTE);
		nSecond = calendar.get(Calendar.SECOND);
		
		long currSecond = calendar.getTimeInMillis()/1000;
		
		if(year >= 1970)
			nYear = year;
		if(month>0 && month<13)
			nMonth = month-1;
		if(day>0 && day<32)
			nDay = day;
		if(hour>=0 && hour<24)
			nHour = hour;
		if(minute>=0 && minute<60)
			nMinute = minute;
		if(second>=0 && second<60)
			nSecond = second;
		
		calendar.set(nYear, nMonth, nDay, nHour, nMinute, nSecond);
		long comboSecond = calendar.getTimeInMillis()/1000;
		
		if(comboSecond >= currSecond)
			return true;
		
		return false;
	}
	
}
