package com.guse.platform.utils.date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
    public static SimpleDateFormat sdf        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat format     = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat format_month     = new SimpleDateFormat("yyyy-MM");
    public static SimpleDateFormat ymd_format = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat redis_format = new SimpleDateFormat("yyyyMMdd");

    /**
     * 检查2个时间段是否有交集，无所谓前后
     * @param beginDate
     * @param endDate
     * @param newBeginDate
     * @param newEndDate
     * @return
     * true 有交叉
     * false 无交叉
     */
    public static boolean checkAcross(Date beginDate, Date endDate, Date newBeginDate, Date newEndDate) {
        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();
        long newBeginTime = newBeginDate.getTime();
        long newEndTime = newEndDate.getTime();

        if ((beginTime < newBeginTime && endTime < newBeginTime) && (beginTime < newEndTime && endTime < newEndTime)) {
            return false;
        } else if ((beginTime > newEndTime && endTime > newEndTime) && (beginTime > newBeginTime && endTime > newBeginTime)) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 昨天的日期
     * @Title: yesterday 
     * @param @return 
     * @return String
     */
    public static String yesterdayStr(){
      Calendar   cal   =   Calendar.getInstance();
	  cal.add(Calendar.DATE,   -1);
	  return format.format(cal.getTime());
    }
    
    
    /**
     * 最近n天的日期 比如:day=1,返回昨天的日期字符串
     * @param day
     * @return
     */
    public static String lastOtherDayStr(int day){
    	return lastOtherCustomDayStr(new Date(), day);
    }
    
    /**
     * 以date为准,最近n天的日期 比如:day=1,返回昨天的日期字符串
     * @param day
     * @return
     */
    public static String lastOtherCustomDayStr(Date date,int day){
      Calendar   cal   =   Calendar.getInstance();
      cal.setTime(date);
	  cal.add(Calendar.DATE,   -day);
	  return format.format(cal.getTime());
    }
    
    
    /**
     * 最近n月的年月 例如month=1，返回上个月的年月 yyyy-MM
     * @param month
     * @return
     */
    public static String lastOtherMonthStr(int month){
      Calendar   cal   =   Calendar.getInstance();
	  cal.add(Calendar.MONTH,   -month);
	  return format_month.format(cal.getTime());
    }
    
    /**
     * 从指定时间算起,最近n月的年月 例如month=1，返回上个月的年月 yyyy-MM
     * @param month
     * @return
     */
    public static String lastOtherCustomMonthStr(Date date,int month){
      Calendar   cal   =   Calendar.getInstance();
      cal.setTime(date);
	  cal.add(Calendar.MONTH,   -month);
	  return format_month.format(cal.getTime());
    }
    
    
    /**
     * 获取当前周 当前年周 yyyy-WW
     * @return
     */
    public static String NowWeekStr(){
      Calendar   cal   =   Calendar.getInstance();
      int year = cal.get(Calendar.YEAR);
      int week = cal.get(Calendar.WEEK_OF_YEAR);
	  return year+"-"+week;
    }
    
    
    
    /**
     * 最近n周的年周 例如week=1 就是上周 yyyy-WW
     * @param week
     * @return
     */
    public static String lastOtherWeekStr(int week){
      Calendar   cal   =   Calendar.getInstance();
	  cal.add(Calendar.WEEK_OF_YEAR,   -  week);
	  int year = cal.get(Calendar.YEAR);
      int w = cal.get(Calendar.WEEK_OF_YEAR);
	  return year+"-"+w;
    }
    
    /**
     * 最近n周的年周 例如week=1 就是上周 yyyy-WW
     * @param week
     * @return
     */
    public static String lastOtherCustomWeekStr(Date date,int week){
      Calendar   cal   =   Calendar.getInstance();
      cal.setTime(date);
	  cal.add(Calendar.WEEK_OF_YEAR,   -  week);
	  int year = cal.get(Calendar.YEAR);
      int w = cal.get(Calendar.WEEK_OF_YEAR);
	  return year+"-"+w;
    }
    
    /**
     * 最近n周的起止日期
     * @param week
     * @return
     */
    public static String[] lastOtherWeekTimeStr(int week){
      Calendar   cal   =   Calendar.getInstance();
	  cal.add(Calendar.WEEK_OF_YEAR,   -  week);
	  int year = cal.get(Calendar.YEAR);
      int w = cal.get(Calendar.WEEK_OF_YEAR);
	  return getDayOfWeek(year, w);
    }
    
    
    /**
     * 获取某年某周的起始日期和结束日期
     * 
     * @param year
     * @param weekindex
     * @return
     */
    public static String[] getDayOfWeek(int year, int weekindex) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setWeekDate(year, weekindex, 1);
 
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
        c.add(Calendar.DATE, -dayOfWeek); // 得到本周的第一天
        String begin = format.format(c.getTime());
        c.add(Calendar.DATE, 6); // 得到本周的最后一天
        String end = format.format(c.getTime());
        String[] range = new String[2];
        range[0] = begin;
        range[1] = end;
        return range;
    }
    
    
    /**
     * 最近n天的日期
     * @Title: yesterday 
     * @param @return 
     * @return String
     */
    public static String lastOtherCustomDayStr(int day){
      Calendar   cal   =   Calendar.getInstance();
	  cal.add(Calendar.DATE,   -day);
	  return sdf.format(cal.getTime());
    }
    
    
    /**
     * 最近n天的日期
     * @Title: yesterday 
     * @param @return 
     * @return String
     */
    public static Date lastOtherCustomDay(int day){
      Calendar   cal   =   Calendar.getInstance();
	  cal.add(Calendar.DATE,   -day);
	  return cal.getTime();
    }
    
    /**
     * 昨天日期
     * @Title: yesterdayDate 
     * @param @return 
     * @return Date
     */
    public static Date yesterdayDate(){
    	return StrToDate(yesterdayStr(), format);
    }

    /**
    * 日期转换成字符串
    * @param date
    * @return str
    */
    public static String DateToStr(Date date, SimpleDateFormat sdf) {
        String str = sdf.format(date);
        return str;
    }

    /**
    * 字符串转换成日期
    * @param str
    * @return date
    */
    public static Date StrToDate(String str, SimpleDateFormat sdf) {
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    
    /**
     * String转毫秒
     * @param inVal
     * @return
     */
    public static long fromDateStringToLong(String inVal) { //此方法计算时间毫秒
    	  Date date = null;   //定义时间类型       
    	  try { 
    	  date = sdf.parse(inVal); //将字符型转换成日期型
    	  } catch (Exception e) { 
    	  e.printStackTrace(); 
    	  } 
    	  return date.getTime();   //返回毫秒数
    } 
    
    /**
     * 两个时间相减
     * @param start
     * @param end
     * @return
     */
    public static long dateMin(String start,String end){
    	 long startT=fromDateStringToLong(start); //
		 long endT=fromDateStringToLong(end);  //
    	 long ss=(endT-startT)/(1000); //共计秒数
    	 return ss;
    }
    
    /**
     * 两个时间相减
     * @param start
     * @param end
     * @return
     */
    public static long dateMin(Date start,Date end){
    	 long startT=start.getTime(); //
		 long endT=end.getTime();  //
    	 long ss=(endT-startT)/(1000); //共计秒数
    	 return ss;
    }
    
    
    public static void main(String[] args) {
//		 System.out.println(fromDateStringToLong("2017-07-28 12:12:12"));
//		 long startT=fromDateStringToLong("2005-03-03 14:51:22"); //定义上机时间
//		 long endT=fromDateStringToLong("2005-03-03 14:50:23");  //定义下机时间
//
//		  long ss=(startT-endT)/(1000); //共计秒数
//		  int MM = (int)ss/60;   //共计分钟数
//		  System.out.println(DateUtils.DateToStr(DateUtils.yesterdayDate(), DateUtils.redis_format));
//		  System.out.println(lastOtherMonthStr(4));
    	  System.out.println(NowWeekStr());
		  System.out.println(lastOtherWeekStr(1));
		  System.out.println(lastOtherWeekTimeStr(1)[0]);
		  System.out.println(lastOtherWeekTimeStr(1)[1]);
		  System.out.println(lastOtherCustomMonthStr(lastOtherCustomDay(30), 1));
		  System.out.println(dateMin(yesterdayDate(),new Date()));
	}

}
