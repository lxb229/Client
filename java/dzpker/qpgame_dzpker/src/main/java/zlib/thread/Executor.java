

package zlib.thread;


/**
 * 
* @Title: Executor.java 
* @Package zlib.thread 
* @Description: TODO(任务执行器) 
* @author hy  
* @date 2015年8月7日 下午1:50:40 
* @version V1.0
 */
public interface Executor
{

	/** 任务执行方法 */
	public void execute(Runnable task);
}