
package zlib.thread;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 * 
* @Title: ThreadKit.java 
* @Package zlib.thread 
* @Description: TODO(线程查看工具) 
* @author hy  
* @date 2015年8月7日 下午2:26:08 
* @version V1.0
 */
public final class ThreadKit
{

	/* static fields */
	/** 库信息 */
	public static final String toString=ThreadKit.class.getName();

	/** 线程文字描述的大小常量 */
	public static final int THREAD_TOSTRING_SIZE=1024;
	/** 线程堆栈跟踪中的元素前缀 */
	public static final String STACKTRACEELEMENT_PREFIX="at ";

	/** 日志记录 */
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(TaskQueueExecutor.class);

	/* static methods */
	/** 线程的文字描述 */
	public static String toString(Thread thread)
	{
		StringBuilder cb=new StringBuilder();
		cb.append(thread.toString()).append('\n');
		StackTraceElement[] temp=thread.getStackTrace();
		for(int i=0;i<temp.length;i++)
			cb.append('\t').append(STACKTRACEELEMENT_PREFIX).append(temp[i])
				.append('\n');
		return cb.toString();
	}
	/** 记录线程的所有堆栈 */
	public static void logAllStackTraces()
	{
		Map map=Thread.getAllStackTraces();
		StringBuilder cb=new StringBuilder();
		cb.append("logAllStackTraces,").append('\n');
		Map.Entry e;
		StackTraceElement[] temp;
		Iterator it=map.entrySet().iterator();
		while(it.hasNext())
		{
			e=(Map.Entry)it.next();
			cb.append(e.getKey().toString()).append('\n');
			temp=(StackTraceElement[])e.getValue();
			for(int i=0;i<temp.length;i++)
				cb.append('\t').append(STACKTRACEELEMENT_PREFIX).append(
					temp[i]).append('\n');
		}
		log.warn(cb.toString());
	}
	/** 线程延迟方法 */
	public static void delay(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch(InterruptedException e)
		{
		}
	}

	/* constructors */
	private ThreadKit()
	{
	}

}