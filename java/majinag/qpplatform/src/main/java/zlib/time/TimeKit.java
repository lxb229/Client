

package zlib.time;

/**
 * 
* @Title: TimeKit.java 
* @Package zlib.time 
* @Description: 时间功能函数
* @author hy  
* @date 2015年11月23日 下午6:27:09 
* @version V1.0
 */
public final class TimeKit
{

	/* static fields */
	/** 库信息 */
	public static final String toString=TimeKit.class.getName();
	/** 星期 */
	public static final String[] WEEK={"Sunday","Monday","Tuesday",
		"Wednesday","Thursday","Friday","Saturday"};
	/** 星期简写 */
	public static final String[] WEEK_={"Sun","Mon","Tue","Wed","Thu","Fri",
		"Sat"};
	/** 月 */
	public static final String[] MONTH={"January","February","March",
		"April","May","June","July","August","September","October",
		"November","December"};
	/** 月简写 */
	public static final String[] MONTH_={"Jan","Feb","Mar","Apr","May",
		"Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

	/** 时间的修正值 */
	private static long timeFix;

	/* static methods */
	/** 得到校正后时间，毫秒为单位 */
	public static long getMillisTime()
	{
		return System.currentTimeMillis()-timeFix;
	}
	/** 得到校正后时间，秒为单位 */
	public static int getSecondTime()
	{
		return (int)((System.currentTimeMillis()-timeFix)/1000);
	}
	/** 校正时间 */
	public static void resetTime(long time)
	{
		timeFix=System.currentTimeMillis()-time;
	}
	/** 将指定的毫秒数转换成秒数，毫秒数除1000 */
	public static int timeSecond(long timeMillis)
	{
		return (int)(timeMillis/1000);
	}
	/** 将指定的秒数转换成毫秒数，秒数乘1000 */
	public static long timeMillis(long timeSecond)
	{
		return timeSecond*1000;
	}

	/* constructors */
	private TimeKit()
	{
	}

}