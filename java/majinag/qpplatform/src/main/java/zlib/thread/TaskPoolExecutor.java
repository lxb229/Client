/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.thread;

import zlib.set.ArrayList;
/**
 * 
* @Title: TaskPoolExecutor.java 
* @Package zlib.thread 
* @Description: TODO(任务池) 
* @author hy  
* @date 2015年8月7日 下午1:52:10 
* @version V1.0
 */

public class TaskPoolExecutor implements Executor
{

	/* static fields */
	/** 默认的线程池的初始线程数和最大线程数 */
	public static final int INIT_SIZE=2,MAX_SIZE=100;
	/** 缓存线程的最大空闲时间，3分钟 */
	public static final int TIMEOUT=1*60*1000;
	/** 整理过程的间隔时间，1分钟 */
	public static final int COLLATE_TIME=60*1000;

	/* fields */
	/** 线程池的初始线程数 */
	int initSize;
	/** 线程池的最大线程数 */
	int maxSize;
	/** 线程池 */
	ArrayList pool;
	/** 线程组 */
	ThreadGroup group;

	/** 缓存线程的最大空闲时间 */
	int timeout;
	/** 活动标志 */
	boolean active;
	/** 整理过程的间隔时间 */
	int collateTime;

	/** 最后一次的整理时间 */
	private long lastCollateTime;

	/* constructors */
	/** 按照标准大小构造一个线程池 */
	public TaskPoolExecutor()
	{
		this(INIT_SIZE,MAX_SIZE,(ThreadGroup)null);
	}
	/** 按照指定的大小构造一个线程池 */
	public TaskPoolExecutor(int initSize,int maxSize)
	{
		this(initSize,maxSize,(ThreadGroup)null);
	}
	/** 按照指定的大小和线程组名称构造一个线程池 */
	public TaskPoolExecutor(int initSize,int maxSize,String name)
	{
		this(initSize,maxSize,new ThreadGroup(name));
	}
	/** 按照指定的大小和线程组构造一个线程池 */
	public TaskPoolExecutor(int initSize,int maxSize,ThreadGroup group)
	{
		if(initSize<=0||maxSize<initSize)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid size:"+initSize+","+maxSize);
		this.initSize=initSize;
		this.maxSize=maxSize;
		pool=new ArrayList(maxSize);
		this.group=group;
		for(int i=0;i<initSize;i++)
			pool.add(new TaskQueueExecutor(group));
		timeout=TIMEOUT;
		active=true;
		collateTime=COLLATE_TIME;
		lastCollateTime=System.currentTimeMillis();
	}
	/* properties */
	/** 判断线程池是否活动 */
	public boolean isActive()
	{
		return active;
	}
	/** 得到线程池的当前线程数 */
	public synchronized int size()
	{
		return pool.size();
	}
	/** 得到线程池的初始线程数 */
	public int getInitSize()
	{
		return initSize;
	}
	/** 得到线程池的最大线程数 */
	public int getMaxSize()
	{
		return maxSize;
	}
	/** 得到线程池的正在运行的线程数 */
	public synchronized int getRunningCount()
	{
		int n=0;
		for(int i=pool.size()-1;i>=0;i--)
		{
			if(((TaskQueueExecutor)(pool.get(i))).isRunning()) n++;
		}
		return n;
	}
	/** 获得线程组 */
	public ThreadGroup getThreadGroup()
	{
		return group;
	}
	/** 获得线程池的线程空闲时间 */
	public int getTimeout()
	{
		return timeout;
	}
	/** 设置线程池的线程空闲时间 */
	public void setTimeout(int timeout)
	{
		this.timeout=timeout;
	}
	/** 获得整理过程的间隔时间 */
	public int getCollateTime()
	{
		return collateTime;
	}
	/** 设置整理过程的间隔时间 */
	public void setCollateTime(int time)
	{
		collateTime=time;
	}
	/* method */
	/** 往线程池加入一个新线程 */
	private Executor addExecutor()
	{
		Executor e=new TaskQueueExecutor(group);
		pool.add(e);
		return e;
	}
	/** 从线程池中得到线程的方法，如果池满则返回空 */
	private synchronized Executor getExecutor()
	{
		if(!active)
			throw new IllegalStateException(this+" getExecutor, is stopped");
		// 当前最少任务数量的线程
		TaskQueueExecutor run=null;
		int c=Integer.MAX_VALUE,count=0;
		TaskQueueExecutor e;
		for(int i=pool.size()-1;i>=0;i--)
		{
			e=(TaskQueueExecutor)(pool.get(i));
			if(e.isActive())
			{
				if(!e.isRunning()) return e;
				if(e.isFull()) continue;
				count=e.getCount();
				if(c<=count) continue;
				c=count;
				run=e;
			}
			else
				pool.removeAt(i);
		}
		if(pool.size()<maxSize) return addExecutor();
		if(run!=null) return run;
		ThreadKit.logAllStackTraces();
		throw new IllegalStateException(this+" getExecutor, pool is full");
	}
	/**
	 * 任务运行方法，用一个缓存线程去运行任务对象。
	 * 线程池同步此方法可以保证从池内取到的缓存线程被立即使用，
	 * 从而避免出现外部线程取到了缓存线程后没有立即使用，
	 * 而被另一个线程再次从池中取得。
	 * 多数情况，该方法由一个线程进行调用，故可以不同步此方法。
	 * 根据当前时间，该方法还可能调用整理方法。
	 */
	public void execute(Runnable task)
	{
		getExecutor().execute(task);
		long time=System.currentTimeMillis();
		if(time-lastCollateTime<collateTime) return;
		collate(time);
		lastCollateTime=time;
	}
	/** 线程池的整理方法 */
	public synchronized void collate(long time)
	{
		time-=timeout;
		TaskQueueExecutor e;
		for(int i=pool.size()-1;i>=0;i--)
		{
			e=(TaskQueueExecutor)(pool.get(i));
			if(!e.isAlive())
			{
			}
			else if(!e.isActive())
			{
			}
			else if(e.isRunning()||time<e.getLastTime())
			{
				continue;
			}
			e.stopTask();
			pool.removeAt(i);
		}
	}
	/** 线程池的关闭方法，试图将所有线程都结束 */
	public synchronized void stop()
	{
		active=false;
		for(int i=pool.size()-1;i>=0;i--)
			((TaskQueueExecutor)(pool.get(i))).stopTask();
		pool.clear();
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[active="+active+", run="+getRunningCount()
			+", size="+size()+", maxSize="+maxSize+"]";
	}

}