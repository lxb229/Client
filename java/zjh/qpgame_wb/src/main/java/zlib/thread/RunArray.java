

package zlib.thread;


/**
* @Title: RunArray.java 
* @Package zlib.thread 
* @Description: TODO(任务执行器) 
* @author hy  
* @date 2015年8月7日 下午1:51:14 
* @version V1.0
 */
public class RunArray
{

	/* static fields */
	/** 空数组 */
	public final static Runnable[] NULL={};

	/* fields */
	/** 数组 */
	Runnable[] array=NULL;

	/* properties */
	/** 获得数量 */
	public int size()
	{
		return array.length;
	}
	/** 获得数组 */
	public Runnable[] getArray()
	{
		return array;
	}
	/* methods */
	/** 判断是否包含指定的窗口 */
	public boolean contain(Runnable obj)
	{
		Runnable[] array=this.array;
		if(obj!=null)
		{
			for(int i=array.length-1;i>=0;i--)
			{
				if(obj.equals(array[i])) return true;
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
	/** 添加指定的窗口 */
	public synchronized void add(Runnable obj)
	{
		int i=array.length;
		Runnable[] temp=new Runnable[i+1];
		if(i>0) System.arraycopy(array,0,temp,0,i);
		temp[i]=obj;
		array=temp;
	}
	/** 移除指定的窗口 */
	public synchronized boolean remove(Runnable obj)
	{
		int i=array.length-1;
		if(obj!=null)
		{
			for(;i>=0;i--)
			{
				if(obj.equals(array[i])) break;
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
			array=NULL;
			return true;
		}
		Runnable[] temp=new Runnable[array.length-1];
		if(i>0) System.arraycopy(array,0,temp,0,i);
		if(i<temp.length) System.arraycopy(array,i+1,temp,i,temp.length-i);
		array=temp;
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