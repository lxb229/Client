

package timer;

/**
 * 
* @Title: TimerRunAdapter.java 
* @Package timer 
* @Description: TODO(事件) 
* @author hy  
* @date 2015年8月14日 上午9:54:18 
* @version V1.0
 */
public class TimerRunAdapter implements TimerListener
{

	/* fields */
	/** 运行对象 */
	private Runnable run;

	/* constructors */
	/** 构造一个空运行对象的定时运行适配器 */
	public TimerRunAdapter()
	{
	}
	/** 构造一个指定运行对象的定时运行适配器 */
	public TimerRunAdapter(Runnable run)
	{
		this.run=run;
	}
	/* properties */
	/** 获得运行对象 */
	public Runnable getRunnable()
	{
		return run;
	}
	/** 设置运行对象 */
	public void setRunnable(Runnable run)
	{
		this.run=run;
	}
	/* methods */
	/** 定时事件的监听方法 */
	public void onTimer(TimerEvent e)
	{
		if(run!=null) run.run();
	}

}