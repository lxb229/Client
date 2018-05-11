
package timer;

import org.slf4j.LoggerFactory;

/**
 * 
* @Title: TimerEvent.java 
* @Package timer 
* @Description: TODO(定时事件) 
* @author hy  
* @date 2015年8月14日 上午9:52:51 
* @version V1.0
 */

public class TimerEvent
{

	/* static fields */
	/** 定义无限循环常量 */
	public static final int INFINITE_CYCLE=0x7fffffff;

	/** 日志记录 */
	private static final org.slf4j.Logger log=LoggerFactory.getLogger(TimerCenter.class);

	/* fields */
	/** 定时事件监听器 */
	TimerListener listener;
	/** 事件动作参数 */
	Object parameter;
	/** 定时时间 */
	int intervalTime;
	/** 定时次数 */
	int count;
	/** 初始延迟时间 */
	int initTime;
	/** 决对时间定时，线程工作的时间计算在定时时间内 */
	boolean absolute;
	/** 起始时间 */
	long startTime;
	/** 当前运行的时间 */
	long currentTime;
	/** 下一次运行的时间 */
	long nextTime;

	/* constructors */
	/** 构造一个定时事件对象，默认为无初始延迟时间、无限循环，相对时间定时 */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime)
	{
		this(listener,parameter,intervalTime,INFINITE_CYCLE,0,false);
	}
	/** 构造一个定时事件对象，默认为无初始延迟时间、无限循环 */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime,boolean absolute)
	{
		this(listener,parameter,intervalTime,INFINITE_CYCLE,0,absolute);
	}
	/** 构造一个定时事件对象，默认为无初始延迟时间，相对时间定时 */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime,int count)
	{
		this(listener,parameter,intervalTime,count,0,false);
	}
	/** 构造一个定时事件对象，默认为无初始延迟时间 */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime,int count,boolean absolute)
	{
		this(listener,parameter,intervalTime,count,0,absolute);
	}
	/** 构造一个定时事件对象，默认为相对时间定时 */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime,int count,int initTime)
	{
		this(listener,parameter,intervalTime,count,initTime,false);
	}
	/**
	 * 构造一个定时源事件对象
	 * 
	 * @param listener 定时事件监听器
	 * @param parameter 事件动作参数
	 * @param intervalTime 定时时间
	 * @param count 定时次数
	 * @param initTime 初始延迟时间
	 * @param absolute
	 *            决对时间定时，线程工作的时间计算在定时时间内
	 */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime,int count,int initTime,boolean absolute)
	{
		this.listener=listener;
		this.parameter=parameter;
		this.intervalTime=intervalTime;
		this.count=count;
		this.initTime=initTime;
		this.absolute=absolute;
	}
	/* properties */
	/** 获得定时事件监听器 */
	public TimerListener getTimerListener()
	{
		return listener;
	}
	/** 获得事件动作参数 */
	public Object getParameter()
	{
		return parameter;
	}
	/** 设置事件动作参数 */
	public void setParameter(Object parameter)
	{
		this.parameter=parameter;
	}
	/** 获得定时时间 */
	public int getIntervalTime()
	{
		return intervalTime;
	}
	/** 设置定时时间 */
	public void setIntervalTime(int time)
	{
		intervalTime=time;
	}
	/** 获得定时次数 */
	public int getCount()
	{
		return count;
	}
	/** 设置定时次数 */
	public void setCount(int count)
	{
		this.count=count;
	}
	/** 获得定时的起始延时时间 */
	public int getInitTime()
	{
		return initTime;
	}
	/** 判断是否为决对时间定时 */
	public boolean isAbsolute()
	{
		return absolute;
	}
	/** 设置决对或相对时间定时 */
	public void setAbsolute(boolean b)
	{
		absolute=b;
	}
	/** 获得起始时间 */
	public long getStartTime()
	{
		return startTime;
	}
	/** 获得当前运行的时间 */
	public long getCurrentTime()
	{
		return currentTime;
	}
	/** 获得下一次运行的时间 */
	public long getNextTime()
	{
		return nextTime;
	}
	/** 设置下一次运行的时间 */
	public void setNextTime(long time)
	{
		nextTime=time;
	}
	/* methods */
	/** 初始化方法 */
	public void init()
	{
		startTime=System.currentTimeMillis();
		nextTime=startTime+initTime;
	}
	/** 引发定时事件，通知定时事件监听器，设置定时次数和下一次的运行时间 */
	void fire(long currentTime)
	{
		if(count!=INFINITE_CYCLE) count--;
		this.currentTime=currentTime;
		nextTime=absolute?(nextTime+intervalTime):(currentTime+intervalTime);
		try
		{
			listener.onTimer(this);
		}
		catch(Throwable e)
		{
			if(log.isWarnEnabled()) log.warn("fire error, "+toString(),e);
		}
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[listener="+listener+", parameter="
			+parameter+", intervalTime="+intervalTime+", count="+count
			+", initTime="+initTime+", absolute="+absolute+"]";
	}

}