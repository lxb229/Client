package com.guse.four_one_nine.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 日期工具类
 */
public final class DateUtil{

    /**
     * yyyy-MM-dd
     */
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

    public static final String DATE_FORMAT_YYYY_MM = "yyyy-MM";
    
    public static final String DATE_FORMAT_MM_dd = "MM_dd";

    public static final String DATE_FORMAT_HH_MM_SS  = "HH:mm:ss";
    
    public static final String DATE_FORMAT_HH_MM  = "HH:mm";
    
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS  = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM   = "yyyy-MM-dd HH:mm";
    
    public static final String DATE_FORMAT_YYYYMMDD_HHMM   = "yyyyMMdd:HHmm";
    
    /**
     * 一毫秒
     */
    public static final long   TIME_ONE_MILL_SECOND            = 1;
    /**
     * 一秒
     */
    public static final long   TIME_ONE_SECOND                 = 1000 * TIME_ONE_MILL_SECOND;
    /**
     * 一分钟
     */
    public static final long   TIME_ONE_MINUTE                 = TIME_ONE_SECOND * 60;
    /**
     * 一小时
     */
    public static final long   TIME_ONE_HOUR                   = TIME_ONE_MINUTE * 60;
    /**
     * 一天
     */
    public static final long   TIME_ONE_DAY                    = TIME_ONE_HOUR * 24;
    

    private final static ThreadLocal<DateFormat> formaterDate                = new ThreadLocal<DateFormat>();

    private final static ThreadLocal<DateFormat> formaterDateTime            = new ThreadLocal<DateFormat>();

    private final static ThreadLocal<DateFormat> formaterDateTimeNoSeparator = new ThreadLocal<DateFormat>();

    private final static ThreadLocal<DateFormat> formaterDateTimeNoSecond    = new ThreadLocal<DateFormat>();

    
    /**
     * 获取线程安全的 SimpleDateFormat/HH:mm
     * @return
     */
    public static DateFormat getFormaterDateHours() {
        DateFormat formater = formaterDate.get();
        if (formater == null) {
            formater = new SimpleDateFormat(DATE_FORMAT_HH_MM);
            formaterDate.set(formater);
        }
        return formater;
    }
    /**
     * 获取线程安全的 SimpleDateFormat/yyyy-MM-dd
     * @return
     */
    public static DateFormat getFormaterDate() {
        DateFormat formater = formaterDate.get();
        if (formater == null) {
            formater = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
            formaterDate.set(formater);
        }
        return formater;
    }

    /**
     * 获取线程安全的 SimpleDateFormat/yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static DateFormat getFormaterDateTimer() {
        DateFormat formater = formaterDateTime.get();
        if (formater == null) {
            formater = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            formaterDateTime.set(formater);
        }
        return formater;
    }

    /**
     * 获取线程安全的 SimpleDateFormat/yyyyMMddHHmmss
     * @return
     */
    public static DateFormat getFormaterDateTimeNoSeparator() {
        DateFormat formater = formaterDateTimeNoSeparator.get();
        if (formater == null) {
            formater = new SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMMSS);
            formaterDateTimeNoSeparator.set(formater);
        }
        return formater;
    }

    /**
     * 获取线程安全的 SimpleDateFormat
     * @see #DATE_FORMAT_YYYY_MM_DD_HH_MM
     * @return
     */
    public static DateFormat getFormaterDateTimeNoSecond() {
        DateFormat formater = formaterDateTimeNoSeparator.get();
        if (formater == null) {
            formater = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM);
            formaterDateTimeNoSecond.set(formater);
        }
        return formater;
    }

    /**
     * 获取startDate和endDate中的日期
     * @param index
     * @param startDate
     * @param endDate
     * @return List<String> String :yyy-MM-dd
     */
    public static List<String> getDaysStringByDates(Date index, Date startDate, Date endDate) {
        Date date = index;
        List<String> list = new ArrayList<String>();
        if (date == null) {
            return Collections.<String> emptyList();
        }
        if (index != null && DateUtil.compareDate(startDate, date) < 0) {
            startDate = date;
        }
        for (; DateUtil.compareDate(startDate, date) >= 0 && DateUtil.compareDate(startDate, endDate) <= 0;) {
            list.add(DateUtil.getFormaterDate().format(startDate));
            startDate = DateUtil.getAfterDayTime(startDate, 1);
        }
        return list;
    }

    public static List<Date> getDaysDateByDates(Date index, Date startDate, Date endDate) {
        Date date = index;
        List<Date> list = new ArrayList<Date>();
        if (date == null) {
            return Collections.<Date> emptyList();
        }
        if (index != null && DateUtil.compareDate(startDate, date) < 0) {
            startDate = date;
        }
        for (; DateUtil.compareDate(startDate, date) >= 0 && DateUtil.compareDate(startDate, endDate) <= 0;) {
            list.add(startDate);
            startDate = DateUtil.getAfterDayTime(startDate, 1);
        }
        return list;
    }
    
    /**
     * 是否为当天
     * @Title: isToday 
     * @date 2017年8月16日 下午3:52:12 
     * @version V1.0
     */
    public static boolean isToday(Date date){
//        SimpleDateFormat format = new SimpleDateFormat(formatStr);
//        Date date = null;
//        try {
//            date = format.parse(str);
//        } catch (ParseException e) {
//            logger.error("解析日期错误", e);
//        }
        Calendar c1 = Calendar.getInstance();              
        c1.setTime(date);                                 
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH)+1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);     
        Calendar c2 = Calendar.getInstance(); 
        c2.setTime(new Date());
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH)+1;
        int day2 = c2.get(Calendar.DAY_OF_MONTH);   
        if(year1 == year2 && month1 == month2 && day1 == day2){
            return true;
        }
        return false;   
    }
    
    /**
     * 当天开始日期
     * @Title: getStartTime 
     * @date 2017年7月31日 下午2:02:57 
     * @version V1.0
     */
    public static Date getStartTime() {  
        Calendar todayStart = Calendar.getInstance();  
        todayStart.set(Calendar.HOUR, 0);  
        todayStart.set(Calendar.MINUTE, 0);  
        todayStart.set(Calendar.SECOND, 0);  
        todayStart.set(Calendar.MILLISECOND, 0);  
        return todayStart.getTime();  
    }  
    
    /**
     * 当天结束日期
     * @Title: getEndTime 
     * @date 2017年7月31日 下午2:03:09 
     * @version V1.0
     */
    public static Date getEndTime() {  
        Calendar todayEnd = Calendar.getInstance();  
        todayEnd.set(Calendar.HOUR, 23);  
        todayEnd.set(Calendar.MINUTE, 59);  
        todayEnd.set(Calendar.SECOND, 59);  
        todayEnd.set(Calendar.MILLISECOND, 999);  
        return todayEnd.getTime();  
    }  
    
    /**
     * 获得指定格式的当前日期开始是日期
     * @param pattern
     * @return
     */
    public static String formatCurrentDate(Date startDate) {
    	if(startDate != null) {
    		 return getFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS).format(startDate);
    	}
       return null;
    }

    /**
     * 
     * 字符串转换为日期格式
     * @param dateString
     * @param pattern
     * @return
     */
    public static Date parseDate(String dateString, String pattern) {
        try {
            return getFormat(pattern).parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @Description: Long转换为日期格式
     * @param @param dateLong
     * @param @param pattern
     * @param @return   
     * @return Date  
     * @throws
     * @author wangkai
     * @date 2018年1月19日
     */
    public static Date parseDate(Long dateLong, String pattern) {
        try {
            return getFormat(pattern).parse(getFormat(pattern).format(dateLong));
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        try {
            if (date == null) {
                return StringUtils.EMPTY;
            }
            return getFormat(pattern).format(date);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }
    
    /**
     * 格式转化
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(String date, String pattern) {
        try {
            if (StringUtils.isEmpty(date)) {
                return null;
            }
            return getFormat(pattern).format(parseDate(date, DATE_FORMAT_YYYY_MM_DD));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获得指定格式的当前日期
     * @param pattern
     * @return
     */
    public static String formatCurrentDate(String pattern) {
        return getFormat(pattern).format(new Date());
    }
    
    /**
     * 获得指定格式的前n天日期 
     * @param pattern
     * @return
     */
    public static String formatCustomDate(int day,String pattern) {
    	Calendar   cal   =   Calendar.getInstance();
   	  	cal.add(Calendar.DATE,   -day);
   	  	return getFormat(pattern).format(cal.getTime());
    }
    
    /**
     * 获得指定格式的前n天日期 
     * @param pattern
     * @return
     */
    public static Date formatCustomDate(int day) {
    	Calendar   cal   =   Calendar.getInstance();
   	  	cal.add(Calendar.DATE,   -day);
   	  	return cal.getTime();
    }
    
    /**
     * 获得最近三天的日期
     * @param pattern
     * @return
     */
    public static String[] last3DaysDates() {
    	String last1Day = formatCustomDate(1, DATE_FORMAT_YYYY_MM_DD);
    	String last2Day = formatCustomDate(2, DATE_FORMAT_YYYY_MM_DD);
    	String last3Day = formatCustomDate(3, DATE_FORMAT_YYYY_MM_DD);
    	String today = formatCustomDate(0, DATE_FORMAT_YYYY_MM_DD); // TODO 测试加上今天
    	String[] dates = {last1Day,last2Day,last3Day}; 
    	return dates;
    }

    /**
     * 获取 DateFormat
     * @param pattern
     * @return
     */
    public static DateFormat getFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 取得某时间后n天,格式为yyyy-mm-dd  
     * @param date
     * @param n
     * @return
     */
    public static String getAfterDays(Date date, int n) {
        Date d = getAfterDays(date, Calendar.DAY_OF_MONTH, n);
        return getFormaterDate().format(d);
    }

    /**
     * 取得某时间前n天,格式为yyyy-mm-dd  
     * @param date
     * @param n
     * @return
     */
    public static String getBeforeDays(Date date, int n) {
        Date d = getAfterDays(date, Calendar.DAY_OF_MONTH, -n);
        return getFormaterDate().format(d);
    }
    
    /**
     * 取得某时间前n天,格式为yyyy-mm-dd  
     * @param date
     * @param n
     * @return date型时间
     */
    public static Date getBeforeDay(Date date, int n) {
        Date d = getAfterDays(date, Calendar.DAY_OF_MONTH, -n);
        return d;
    }

    /**
     * 获取日期,
     * @see Calendar
     * @param date
     * @param type
     *      @see Calendar#YEAR
     *      @see Calendar#MONTH
     *      @see Calendar#DAY_OF_MONTH
     *      @see Calendar#HOUR_OF_DAY
     *      @see Calendar#MINUTE
     *      @see Calendar#SECOND
     *      @see Calendar#MILLISECOND
     * @param n
     * @return
     */
    public static Date getAfterDays(Date date, int type, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(type, amount);
        return c.getTime();
    }

    /**
     * 计算两段日期之间的天数
     * @param beginDate
     * @param endDate
     * @param amount
     *      @see Calendar#DATE
     *      @see Calendar#HOUR
     *      @see Calendar#MINUTE
     *      @see Calendar#SECOND
     *      @see Calendar#MILLISECOND
     * @return
     */
    public static long getDaysByDate(Date beginDate, Date endDate, int amount) {
        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("argu is ellegal,beginDate or endDate is null");
        }
        long times = endDate.getTime() - beginDate.getTime();
        long result = 0;
        switch (amount) {
            case Calendar.MILLISECOND:
                result = times;
                break;
            case Calendar.SECOND:
                result = times / 1000;
                break;
            case Calendar.MINUTE:
                result = times / TIME_ONE_MINUTE;
                break;
            case Calendar.HOUR:
                result = times / TIME_ONE_HOUR;
                break;
            case Calendar.DATE:
                result = times / TIME_ONE_DAY;
                break;
            default:
                throw new IllegalArgumentException("argu is ellegal,amount is error.");
        }
        return result;
    }

    /**
     * 计算两段日期之间的天数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long getDaysByDate(Date beginDate, Date endDate) {
        long days = 0;
        if (beginDate == null || endDate == null)
            return days;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(beginDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(endDate);

        long l1 = c1.getTimeInMillis();
        long l2 = c2.getTimeInMillis();
        // 计算天数
        days = (l2 - l1) / TIME_ONE_DAY;
        return days;
    }

    /**
     * 比较日期大小，dt1>dt2返回1，dt1=dt2返回0，dt1<dt2返回-1
     * @param dt1
     * @param dt2
     * @return
     */
    public static int compareDate(Date dt1, Date dt2) {
        if (dt1.getTime() > dt2.getTime()) {
            return 1;
        } else if (dt1.getTime() == dt2.getTime()) {
            return 0;
        } else {
            return -1;
        }
    }
    
    /**
     * 根据日期返回星期
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 返回java code里面的星期
     * @param date
     * @return
     */
    public static Integer getWeekOfDateSource(Date date) {
        if (date == null) {
            throw new IllegalArgumentException();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 年月日
     * @param date
     * @return
     */
    public static Date getDate4YMD(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    //  得到日期的星期
    public static String getDayOfWeek(Date date) {
        String str = StringUtils.EMPTY;
        if (date == null) {
            return str;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        return StringUtils.EMPTY + day;
    }

    /**
     * 得到日期的星期 汉子
     * @param date
     * @return
     */
    public static String getDayOfWeek(String date) {
        String str = StringUtils.EMPTY;
        Calendar c = parseCalendar(date);
        if (c == null)
            return str;

        int day = c.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                str = "日";
                break;
            case Calendar.MONDAY:
                str = "一";
                break;
            case Calendar.TUESDAY:
                str = "二";
                break;
            case Calendar.WEDNESDAY:
                str = "三";
                break;
            case Calendar.THURSDAY:
                str = "四";
                break;
            case Calendar.FRIDAY:
                str = "五";
                break;
            case Calendar.SATURDAY:
                str = "六";
                break;
            default:
                str = StringUtils.EMPTY;
        }
        return str;
    }

    /**
     * 
     * @param strDate yyyy-MM-dd
     * @return
     */
    public static Calendar parseCalendar(String strDate) {
        Calendar c = null;
        Date d = null;
        try {
            d = getFormaterDate().parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d != null) {
            c = Calendar.getInstance();
            c.setTime(d);
        }
        return c;
    }

    /**
     * 获取某时间后n天的时间
     * @param date
     * @param n
     * @return
     */
    public static Date getAfterDayTime(Date date, int n) {
        return getAfterDays(date, Calendar.DAY_OF_MONTH, n);
    }

    /**
     * 获取指定时间的上一天
     * @author dqzhai
     * @param time
     * @return 
     * 2012-3-16
     */
    public static Date returnPreviousDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        calendar.get(Calendar.DAY_OF_MONTH);
        return calendar.getTime();
    }

    /**
     * 获取指定时间的下一天
     * @author dqzhai
     * @param time
     * @return 
     * 2012-3-16
     */
    public static Date getNextDay(Date time) {
        return getAfterDayTime(time, 1);
    }

    /**
     * 日期间隔数
     * 
     * @param beginTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 日期间隔数
     */
    public static int getDiffDate(Date beginTime, Date endTime) {
        return (int) ((beginTime.getTime() - endTime.getTime()) / TIME_ONE_DAY);
    }

    /**
     * 返回两个日期相差的天数
     * 
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return 两个日期相差的天数
     * @throws ParseException
     */
    public static long getDistDates(Date startDate, Date endDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(startDate);
        long time0 = aCalendar.getTimeInMillis();
        aCalendar.setTime(endDate);
        long time1 = aCalendar.getTimeInMillis();
        return (time1 - time0) / (TIME_ONE_DAY);
    }

    /**
     * 获取两个时间相差的秒
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    public static long getDistDatesBySecond(Date startTime, Date endTime) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(startTime);
        long time0 = aCalendar.getTimeInMillis();
        aCalendar.setTime(endTime);
        long time1 = aCalendar.getTimeInMillis();
        return (time1 - time0) / (TIME_ONE_SECOND);
    }

    /**
     * @see 计算当前月有多少天
     * @param
     * */
    public static int getDateMonthByDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * @see 计算运营(月)报表信息需要的时间(当前日期的前11个月)
     */
    public static Date[] getDateByReportAndMonth() {
        Date[] dateArr = new Date[2];
        Date date = getAfterDays(new Date(), Calendar.MONTH, -11);
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(date);
        calStart.set(Calendar.DAY_OF_MONTH, 1);
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        dateArr[0] = calStart.getTime();

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(new Date());
        calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        calEnd.set(Calendar.HOUR_OF_DAY, 23);
        calEnd.set(Calendar.MINUTE, 59);
        calEnd.set(Calendar.SECOND, 59);
        dateArr[1] = calEnd.getTime();
        return dateArr;
    }

    /**
     * 获取开始和结束日期之间的所有日期
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合  
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间  
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后  
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合  
        return lDate;
    }

    /**
     * 日期是否有交集
     * 
     * @param leftStartDate
     * @param leftEndDate
     * @param rightStartDate
     * @param rightEndDate
     * @return
     */
    public static boolean isOverlap(Date leftStartDate, Date leftEndDate, Date rightStartDate, Date rightEndDate) {
        return ((leftStartDate.getTime() >= rightStartDate.getTime()) && leftStartDate.getTime() <= rightEndDate.getTime())
               || ((leftStartDate.getTime() >= rightStartDate.getTime()) && leftStartDate.getTime() <= rightEndDate.getTime())
               || ((rightStartDate.getTime() >= leftStartDate.getTime()) && rightStartDate.getTime() <= leftEndDate.getTime())
               || ((rightStartDate.getTime() >= leftStartDate.getTime()) && rightStartDate.getTime() <= leftEndDate.getTime());

    }
    
    /**
     * 获取date每间隔interval分钟的所有时间点
     * @param date
     * @param interval 间隔时间
     * @return
     */
    public static List<Date> get2MinuteOneDay(Date date,int interval) {
        Date start = dayStartDate(date);//转换为天的起始date
        Date nextDayDate = nextDay(start);//下一天的date
         
        List<Date> result = new ArrayList<Date>();
        while (start.compareTo(nextDayDate) < 0) {
            result.add(start);
            //日期加2分钟
            start = addFiveMin(start, 2);
        }
         
        return result;
    }
 
    private static Date addFiveMin(Date start, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.MINUTE, offset);
        return c.getTime();
    }
 
    private static Date nextDay(Date start) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }
 
    private static Date dayStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static void main(String[] args) {
//        String d1 = "2016-10-12";
//        String d2 = "2016-10-22";
//        String d3 = "2016-10-23";
//        String d4 = "2016-10-24";
//
//        System.out.println(isOverlap(parseDate(d1, DATE_FORMAT_YYYY_MM_DD), parseDate(d2, DATE_FORMAT_YYYY_MM_DD), parseDate(d3, DATE_FORMAT_YYYY_MM_DD),
//            parseDate(d4, DATE_FORMAT_YYYY_MM_DD)));
//        
//        System.out.println(formatDate("2017-08-09", DATE_FORMAT_YYYYMMDD));
//        
//        System.out.println(DateUtil.getAfterDays(new Date(), Calendar.DAY_OF_MONTH, -1));
//        
//        System.out.println(get2MinuteOneDay(new Date(), 2));
        
        Date aa = parseDate("2017-08-10 22:00:00", DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		System.out.println(compareDate(aa, new Date()));
		System.out.println(getAfterDays(new Date(), 2));
		System.out.println(getDaysByDate(getAfterDayTime(new Date(), -2), new Date()));
		
		
		
		try {
			System.out.println(isToday(parseDate("2017-08-16 00:00:00", DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

    }

}
