

package timer;

import org.slf4j.LoggerFactory;
import zlib.thread.ThreadKit;

/**
 * 
* @Title: TimerCenter.java 
* @Package timer 
* @Description: TODO(定时器管理中心) 
* @author hy  
* @date 2015年8月14日 上午9:51:36 
* @version V1.0
 */

public final class TimerCenter implements Runnable
{

	//线程状态,0=已退出,1=正在运行,2=正在退出
	private byte threadState;
	
	/* static fields */
	/** 默认的毫秒级、秒级定时器、分钟级定时器的运行时间 */
	public static final int MILLIS_TIME=10,SECOND_TIME=200,MINUTE_TIME=4000;
//	/** 默认的毫秒级、秒级定时器、分钟级定时器的超时时间 */
//	public static final int MILLIS_TIMEOUT=500,SECOND_TIMEOUT=10000,
//					MINUTE_TIMEOUT=200000;
	/** 默认的毫秒级、秒级定时器、分钟级定时器的超时时间 */
	public static final int MILLIS_TIMEOUT=10000,SECOND_TIMEOUT=200000,
					MINUTE_TIMEOUT=4000000;
	/** 默认的监控时间 */
	public static final int COLLATE_TIME=1000;

	/** 当前的定时器中心 */
	private static TimerCenter center=new TimerCenter();

	/** 日志记录 */
	private static final org.slf4j.Logger log=LoggerFactory.getLogger(TimerCenter.class);

	/* static methods */
	/** 获得当前的定时器中心 */
	public static TimerCenter getInstance()
	{
		return center;
	}
	/** 获得毫秒级定时器 */
	public static Timer getMillisTimer()
	{
		return center.getMillisThread().timer;
	}
	/** 获得秒级定时器 */
	public static Timer getSecondTimer()
	{
		return center.getSecondThread().timer;
	}
	/** 获得分钟级定时器 */
	public static Timer getMinuteTimer()
	{
		return center.getMinuteThread().timer;
	}

	/* fields */
	/** 毫秒级定时器线程 */
	TimerThread millisThread;
	/** 秒级定时器线程 */
	TimerThread secondThread;
	/** 分钟级定时器线程 */
	TimerThread minuteThread;

	/** 监控线程的工作时间间隔 */
	private int runTime=COLLATE_TIME;
	/** 监控线程 */
	private Thread run;

	/* constructors */
	/** 构造方法 */
	TimerCenter()
	{
		run=new Thread(this);
		run.setName(run.getName()+"@"+getClass().getName()+"@"+hashCode()
			+"/"+runTime);
		run.setDaemon(true);
		threadState = 1;
		run.start();
	}
	/* properties */
	/** 获得毫秒级定时器线程 */
	public synchronized TimerThread getMillisThread()
	{
		if(millisThread==null)
		{
			millisThread=new TimerThread(new Timer(),MILLIS_TIME,
				MILLIS_TIMEOUT);
			millisThread._start();
		}
		return millisThread;
	}
	/** 获得秒级定时器线程 */
	public synchronized TimerThread getSecondThread()
	{
		if(secondThread==null)
		{
			secondThread=new TimerThread(new Timer(),SECOND_TIME,
				SECOND_TIMEOUT);
			secondThread._start();
		}
		return secondThread;
	}
	/** 获得分钟级定时器线程 */
	public synchronized TimerThread getMinuteThread()
	{
		if(minuteThread==null)
		{
			minuteThread=new TimerThread(new Timer(),MINUTE_TIME,
				MINUTE_TIMEOUT);
			minuteThread._start();
		}
		return minuteThread;
	}
	public synchronized void shutDown(){
		//首先退出当前守护线程
		threadState = 2;
		for(;;){
			if(threadState == 0){
				break;
			}
			Thread.yield();
		}
		//检查毫秒级线程
		if(millisThread != null){
			millisThread.close();
		}
		//检查秒级线程
		if(secondThread != null){
			secondThread.close();
		}
		//检查分钟级线程
		if(minuteThread != null){
			minuteThread.close();
		}
	}
	
	/* methods */
	/** 整理方法 */
	public synchronized void collate(long time)
	{
		if(millisThread==null)
		{
		}
		else if(millisThread.isTimeout(time)||!millisThread.isAlive())
		{
			if(log.isWarnEnabled())
				log.warn("collate, millisThread timeout, "
					+ThreadKit.toString(millisThread));
			millisThread.close();
			millisThread=new TimerThread(millisThread);
			millisThread.start();
			if(log.isWarnEnabled())
				log.warn("collate, millisThread start, "+millisThread);
		}
		if(secondThread==null)
		{
		}
		else if(secondThread.isTimeout(time)||!secondThread.isAlive())
		{
			if(log.isWarnEnabled())
				log.warn("collate, secondThread timeout, "
					+ThreadKit.toString(secondThread));
			secondThread.close();
			secondThread=new TimerThread(secondThread);
			secondThread.start();
			if(log.isWarnEnabled())
				log.warn("collate, secondThread start, "+secondThread);
		}
		if(minuteThread==null)
		{
		}
		else if(minuteThread.isTimeout(time)||!minuteThread.isAlive())
		{
			if(log.isWarnEnabled())
				log.warn("collate, minuteThread timeout, "
					+ThreadKit.toString(minuteThread));
			minuteThread.close();
			minuteThread=new TimerThread(minuteThread);
			minuteThread.start();
			if(log.isWarnEnabled())
				log.warn("collate, minuteThread start, "+minuteThread);
		}
	}
	/** 循环监听方法 */
	public void run()
	{
		while(threadState == 1)
		{
			collate(System.currentTimeMillis());
			try
			{
				Thread.sleep(runTime);
			}
			catch(InterruptedException e)
			{
			}
		}
		threadState = 0;
	}

}