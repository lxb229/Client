
package timer;

/**
 * 
* @Title: TimerEventArray.java 
* @Package timer 
* @Description: TODO(事件数组) 
* @author hy  
* @date 2015年8月14日 上午9:53:22 
* @version V1.0
 */

public class TimerEventArray
{

	/* static fields */
	/** 空数组 */
	public final static TimerEvent[] NULL={};

	/* fields */
	/** 数组 */
	TimerEvent[] array=NULL;

	/* properties */
	/** 获得数量 */
	public int size()
	{
		return array.length;
	}
	/** 获得数组 */
	public TimerEvent[] getArray()
	{
		return array;
	}
	/* methods */
	/** 判断是否包含指定的定时器事件 */
	public boolean contain(TimerEvent ev)
	{
		TimerEvent[] array=this.array;
		if(ev!=null)
		{
			for(int i=array.length-1;i>=0;i--)
			{
				if(ev.equals(array[i])) return true;
			}
		}
		else
		{
			for(int i=array.length-1;i>=0;i--)
			{
				if(array[i]==null) return true;
			}
		}
		return false;
	}
	/** 添加指定的定时器事件 */
	public synchronized void add(TimerEvent ev)
	{
		TimerEvent[] array=this.array;
		int i=array.length;
		TimerEvent[] temp=new TimerEvent[i+1];
		if(i>0) System.arraycopy(array,0,temp,0,i);
		temp[i]=ev;
		this.array=temp;
	}
	/** 移除指定的定时器事件 */
	public synchronized boolean remove(TimerEvent ev)
	{
		TimerEvent[] array=this.array;
		int i=array.length-1;
		if(ev!=null)
		{
			for(;i>=0;i--)
			{
				if(ev.equals(array[i])) break;
			}
		}
		else
		{
			for(;i>=0;i--)
			{
				if(array[i]==null) break;
			}
		}
		if(i<0) return false;
		if(array.length==1)
		{
			this.array=NULL;
			return true;
		}
		TimerEvent[] temp=new TimerEvent[array.length-1];
		if(i>0) System.arraycopy(array,0,temp,0,i);
		if(i<temp.length) System.arraycopy(array,i+1,temp,i,temp.length-i);
		this.array=temp;
		return true;
	}
	/** 清除列表中的所有元素 */
	public synchronized void clear()
	{
		array=NULL;
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[size="+array.length+"]";
	}

}