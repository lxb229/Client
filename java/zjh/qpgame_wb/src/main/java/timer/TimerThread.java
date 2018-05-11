

package timer;

/**
* @Title: TimerThread.java 
* @Package timer 
* @Description: TODO(事件线程) 
* @author hy  
* @date 2015年8月14日 上午9:55:32 
* @version V1.0
 */
public class TimerThread extends Thread
{

	/* fields */
	/** 定时器 */
	Timer timer;
	/** 运行时间 */
	int runTime;
	/** 超时时间 */
	int timeout;

	/** 活动标志 */
	volatile boolean active;

	/** 运行开始时间 */
	long runStartTime;
	/** 运行结束时间 */
	long runEndTime;

	//线程结束标志,0=已退出,1=正在运行,2=正在退出
	private byte threadOverState;
	
	/* constructors */
	/** 用指定的定时器、运行时间和超时时间构造一个定时器线程 */
	TimerThread(Timer timer,int runTime,int timeout)
	{
		this.timer=timer;
		this.runTime=runTime;
		this.timeout=timeout;
		runEndTime=runStartTime=System.currentTimeMillis();
		active=true;
		setName(getName()+"@"+getClass().getName()+"@"+hashCode()+"/"
			+runTime);
		setDaemon(true);
		threadOverState = 0;
	}
	/** 复制构造方法 */
	TimerThread(TimerThread thread)
	{
		this.timer=thread.timer;
		this.runTime=thread.runTime;
		this.timeout=thread.timeout;
		runEndTime=runStartTime=System.currentTimeMillis();
		active=true;
		setName(getName()+"@"+getClass().getName()+"@"+hashCode()+"/"
			+runTime);
		setDaemon(true);
		threadOverState = 0;
	}
	/* properties */
	/** 判断线程是否活动 */
	public boolean isActive()
	{
		return active;
	}
	/** 获得线程的运行时间 */
	public int getRunTime()
	{
		return runTime;
	}
	/** 设置线程的运行时间 */
	public void setRunTime(int time)
	{
		runTime=time;
	}
	/** 获得线程的超时时间 */
	public int getTimeout()
	{
		return timeout;
	}
	/** 设置线程的超时时间 */
	public void setTimeout(int timeout)
	{
		this.timeout=timeout;
	}
	/** 获得线程的运行开始时间 */
	public long getRunStartTime()
	{
		return runStartTime;
	}
	/** 获得线程的运行结束时间 */
	public long getRunEndTime()
	{
		return runEndTime;
	}
	
	public void _start(){
		threadOverState = 1;
		start();
	}
	
	/* methods */
	/** 判断是否超时 */
	public boolean isTimeout(long time)
	{
		return time-runStartTime>timeout;
	}
	/** 运行方法 */
	public void run()
	{
		long time;
		TimerEvent[] temp;
		TimerEvent ev;
		int i;
		while(active)
		{
			try
			{
				time=System.currentTimeMillis();
				runStartTime=time;
				temp=timer.getArray();
				for(i=temp.length-1;active&&i>=0;i--)
				{
					ev=temp[i];
					if(ev.count<=0)
						timer.remove(ev);
					else if(time>=ev.nextTime) ev.fire(time);
				}
				runEndTime=System.currentTimeMillis();
				Thread.sleep(runTime);
			}
			catch(InterruptedException e)
			{
			}
		}
		threadOverState = 0;
	}
	/** 关闭方法 */
	public void close()
	{
		active=false;
		threadOverState = 2;
		for(;;){
			if(threadOverState == 0){
				break;
			}
			Thread.yield();
		}
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[active="+active+", runTime="+runTime
			+", timeout="+timeout+", runStartTime="+runStartTime
			+", runEndTime="+runEndTime+"]";
	}

}