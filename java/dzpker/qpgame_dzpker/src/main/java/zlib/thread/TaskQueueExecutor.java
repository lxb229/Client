

package zlib.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zlib.set.ArrayQueue;


/**
 * 
* @Title: TaskQueueExecutor.java 
* @Package zlib.thread 
* @Description: TODO(用一句话描述该文件做什么) 
* @author hy  
* @date 2015年8月7日 下午2:19:42 
* @version V1.0
 */
public final class TaskQueueExecutor extends Thread implements Executor
{

	/* static fields */
	/** 默认的线程可运行对象的队列的大小 */
	public static final int CAPACITY=255;

	/** 日志记录 */
	private static final Logger log = LoggerFactory.getLogger(TaskQueueExecutor.class);

	/** 编号 */
	static int count=0;

	/* fields */
	/** 任务队列 */
	private ArrayQueue queue;

	/** 线程活动的标志 */
	private boolean active;
	/** 是否正在运行 */
	boolean running;
	/** 线程上次运行的时间 */
	private long lastTime;

	/* constructors */
	/** 构造默认的任务队列执行器 */
	public TaskQueueExecutor()
	{
		this(CAPACITY,null);
	}
	/** 构造指定线程组的任务队列执行器 */
	public TaskQueueExecutor(ThreadGroup group)
	{
		this(CAPACITY,group);
	}
	/** 构造指定队列大小和线程组的任务队列执行器 */
	public TaskQueueExecutor(int capacity,ThreadGroup group)
	{
		super(group,(Runnable)null);
		queue=new ArrayQueue(capacity);
		active=true;
		setName(getName()+"@"+getClass().getName()+"@"+hashCode()+"-"
			+(count++)+"/"+capacity);
		start();
	}
	/* properties */
	/** 判断执行器是否活动 */
	public synchronized boolean isActive()
	{
		return active;
	}
	/** 判断任务是否正在运行 */
	public synchronized boolean isRunning()
	{
		return running;
	}
	/** 判断执行器是否正在活动休眠 */
	public synchronized boolean isSleeping()
	{
		return active&&!running;
	}
	/** 得到上次任务运行完毕后的时间 */
	public synchronized long getLastTime()
	{
		return lastTime;
	}
	/** 得到可以运行任务的最大数量 */
	public synchronized int getCapacity()
	{
		return queue.capacity();
	}
	/** 得到当前等待运行的任务数量 */
	public synchronized int getCount()
	{
		return queue.size();
	}
	/** 判断任务队列是否已满 */
	public synchronized boolean isFull()
	{
		return queue.isFull();
	}
	/* methods */
	/** 任务执行方法 */
	public void execute(Runnable task)
	{
		pushTask(task);
		threadWake();
	}
	/** 执行结束任务 */
	public void executeEndTask()
	{
		execute(new Runnable()
		{

			public void run()
			{
				stopTask();
			}

		});
	}
	/** 执行新的任务 */
	void runTask(Runnable task)
	{
		try
		{
			task.run();
		}
		catch(Throwable e)
		{
			if(log.isWarnEnabled()) log.warn("runTask error, "+task,e);
		}
		lastTime=System.currentTimeMillis();
	}
	/** 取出新的任务 */
	synchronized Runnable popTask()
	{
		if(queue.isEmpty())
		{
			running=false;
			return null;
		}
		return (Runnable)(queue.remove());
	}
	/** 加入新任务 */
	private synchronized void pushTask(Runnable task)
	{
		if(!active)
			throw new IllegalStateException(this
				+" pushTask, has already stopped");
		if(queue.isFull())
			throw new IllegalStateException(this
				+" pushTask, task queue is full");
		queue.add(task);
		running=true;
	}
	/** 线程唤醒方法 */
	private void threadWake()
	{
		synchronized(queue)
		{
			queue.notify();
		}
	}
	/** 线程等待方法，线程休眠时，在此方法中循环 */
	void threadWait()
	{
		synchronized(queue)
		{
			while(isSleeping())
			{
				try
				{
					queue.wait();
				}
				catch(InterruptedException e)
				{
				}
			}
		}
	}
	/** 运行方法 */
	public void run()
	{
		while(isActive())
		{
			Runnable task=popTask();
			if(task!=null)
				runTask(task);
			else
				threadWait();
		}
		if(log.isInfoEnabled()) log.info("run over, "+this);
	}
	/**
	 * 结束方法，但不一定可以将该线程立即结束，
	 * 如果当前有任务正在执行，则在任务完成后结束。
	 * 该方法可以将正在执行的任务的阻塞或休眠打断，
	 * 使用前可以先判断线程是否工作running来确定。
	 */
	public synchronized void stopTask()
	{
		if(log.isDebugEnabled()) log.debug("stop, "+this);
		active=false;
		queue.clear();
		if(running&&log.isWarnEnabled())
			log.warn("stop, thread running, "+ThreadKit.toString(this));
		if(isAlive()) interrupt();
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[ active="+active+", running="+running
			+", count="+queue.size()+"]";
	}

}