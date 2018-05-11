

package zlib.set;

/**
* @Title: ArrayQueue.java 
* @Package zlib.set 
* @Description: TODO(用一句话描述该文件做什么) 
* @author hy  
* @date 2015年8月7日 下午2:21:16 
* @version V1.0
 */

public class ArrayQueue implements Container
{

	/* fields */
	/** 队列的对象数组 */
	Object[] array;
	/** 队列的头 */
	int head;
	/** 队列的尾 */
	int tail;
	/** 队列的长度 */
	int size;

	/* constructors */
	/** 按指定的大小构造一个队列 */
	public ArrayQueue(int capacity)
	{
		if(capacity<1)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid capacity:"+capacity);
		array=new Object[capacity];
		head=0;
		tail=0;
		size=0;
	}
	/* properties */
	/** 获得队列的长度 */
	public int size()
	{
		return size;
	}
	/** 获得队列的容积 */
	public int capacity()
	{
		return array.length;
	}
	/** 判断队列是否为空 */
	public boolean isEmpty()
	{
		return size<=0;
	}
	/** 判断队列是否已满 */
	public boolean isFull()
	{
		return size>=array.length;
	}
	/** 得到队列的对象数组 */
	public Object[] getArray()
	{
		return array;
	}
	/* methods */
	/** 判断对象是否在容器中 */
	public boolean contain(Object obj)
	{
		if(obj!=null)
		{
			for(int i=head,n=tail>head?tail:array.length;i<n;i++)
			{
				if(obj.equals(array[i])) return true;
			}
			for(int i=0,n=tail>head?0:tail;i<n;i++)
			{
				if(obj.equals(array[i])) return true;
			}
		}
		else
		{
			for(int i=head,n=tail>head?tail:array.length;i<n;i++)
			{
				if(array[i]==null) return true;
			}
			for(int i=0,n=tail>head?0:tail;i<n;i++)
			{
				if(array[i]==null) return true;
			}
		}
		return false;
	}
	/** 将对象放入到队列中 */
	public boolean add(Object obj)
	{
		if(size>=array.length) return false;
		if(size<=0)
		{
			tail=0;
			head=0;
		}
		else
		{
			tail++;
			if(tail>=array.length) tail=0;
		}
		array[tail]=obj;
		size++;
		return true;
	}
	/** 检索队列中的第一个对象 */
	public Object get()
	{
		return array[head];
	}
	/** 从队列中弹出第一个的对象 */
	public Object remove()
	{
		Object obj=array[head];
		array[head]=null;
		size--;
		if(size>0)
		{
			head++;
			if(head>=array.length) head=0;
		}
		return obj;
	}
	/** 清除队列 */
	public void clear()
	{
		for(int i=head,n=tail>head?tail:array.length;i<n;i++)
			array[i]=null;
		for(int i=0,n=tail>head?0:tail;i<n;i++)
			array[i]=null;
		tail=0;
		head=0;
		size=0;
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[size="+size+", capacity="+array.length+"]";
	}

}