

package timer;

/**
 * 
* @Title: TimerListener.java 
* @Package timer 
* @Description: TODO(事件监听器) 
* @author hy  
* @date 2015年8月14日 上午9:53:47 
* @version V1.0
 */

public interface TimerListener
{

	/** 定时事件的监听方法 */
	public void onTimer(TimerEvent e);

}