

package timer;

/**
* @Title: Timer.java 
* @Package timer 
* @Description: TODO(定时器) 
* @author hy  
* @date 2015年8月14日 上午9:55:56 
* @version V1.0
 */
public class Timer implements Runnable
{

	/* fields */
	/** 定时器事件数组 */
	private TimerEventArray array=new TimerEventArray();

	/* properties */
	/** 获得定时器事件数组 */
	public TimerEvent[] getArray()
	{
		return array.getArray();
	}
	/* methods */
	/** 判断是否包含指定的对象 */
	public boolean contain(TimerEvent e)
	{
		return array.contain(e);
	}
	/** 获得指定监听器和动作参数的定时器事件（如果多个则返回第一个） */
	public TimerEvent get(TimerListener listener,Object parameter)
	{
		TimerEvent[] temp=array.getArray();
		TimerEvent ev;
		for(int i=temp.length-1;i>=0;i--)
		{
			ev=temp[i];
			if(listener!=ev.getTimerListener()) continue;
			if(parameter==null||parameter.equals(ev.getParameter()))
				return ev;
		}
		return null;
	}
	/** 加上一个定时器事件 */
	public void add(TimerEvent e)
	{
		e.init();
		array.add(e);
	}
	/** 移除指定的定时器事件 */
	public void remove(TimerEvent e)
	{
		array.remove(e);
	}
	/** 移除指定定时事件监听器的定时器事件，包括所有的事件动作参数 */
	public void remove(TimerListener listener)
	{
		remove(listener,null);
	}
	/** 移除带有指定的定时事件监听器和事件动作参数的定时器事件 */
	public void remove(TimerListener listener,Object parameter)
	{
		TimerEvent[] temp=array.getArray();
		TimerEvent ev;
		for(int i=temp.length-1;i>=0;i--)
		{
			ev=temp[i];
			if(listener!=ev.getTimerListener()) continue;
			if(parameter!=null&&!parameter.equals(ev.getParameter()))
				continue;
			array.remove(ev);
		}
	}
	/** 运行方法 */
	public void run()
	{
		fire(System.currentTimeMillis());
	}
	/** 通知所有定时器事件，检查是否要引发定时事件 */
	public void fire(long time)
	{
		TimerEvent[] temp=array.getArray();
		TimerEvent ev;
		for(int i=temp.length-1;i>=0;i--)
		{
			ev=temp[i];
			if(ev.count<=0)
				remove(ev);
			else if(time>=ev.nextTime) ev.fire(time);
		}
	}
	/** 清理方法 */
	public void clear()
	{
		array.clear();
	}

}