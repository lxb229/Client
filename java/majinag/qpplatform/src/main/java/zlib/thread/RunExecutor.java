
package zlib.thread;

/**
 * 
* @Title: RunExecutor.java 
* @Package zlib.thread 
* @Description: TODO(任务执行器) 
* @author hy  
* @date 2015年8月7日 下午1:51:47 
* @version V1.0
 */
public class RunExecutor implements Executor
{

	/** 任务执行方法 */
	public void execute(Runnable task)
	{
		task.run();
	}

}