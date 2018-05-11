
package zlib.set;

/**
 * 
* @Title: ArrayList.java 
* @Package zlib.set 
* @Description: TODO(集合) 
* @author hy  
* @date 2015年8月7日 下午2:19:21 
* @version V1.0
 */

public class ArrayList implements Cloneable,Container,Selectable
{

	/* static fields */
	/** 默认的初始容量大小 */
	public static final int CAPACITY=10;

	/* fields */
	/** 列表的对象数组 */
	Object[] array;
	/** 列表的长度 */
	int size;

	/* constructors */
	/** 按默认的大小构造一个列表 */
	public ArrayList()
	{
		this(CAPACITY);
	}
	/** 按指定的大小构造一个列表 */
	public ArrayList(int capacity)
	{
		if(capacity<1)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid capatity:"+capacity);
		array=new Object[capacity];
	}
	/** 用指定的对象数组构造一个列表 */
	public ArrayList(Object[] array)
	{
		this(array,(array!=null)?array.length:0);
	}
	/**
	 * 用指定的对象数组及长度构造一个列表， 指定长度不能超过对象数组的长度，
	 */
	public ArrayList(Object[] array,int len)
	{
		if(array==null)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, null array");
		if(len>array.length)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid length:"+len);
		this.array=array;
		this.size=len;
	}
	/* properties */
	/** 得到列表的长度 */
	public int size()
	{
		return size;
	}
	/** 得到列表的容积 */
	public int capacity()
	{
		return array.length;
	}
	/** 判断列表是否是空 */
	public boolean isEmpty()
	{
		return size<=0;
	}
	/** 判断队列是否已满 */
	public boolean isFull()
	{
		return false;
	}
	/** 得到列表的对象数组，一般使用toArray()方法 */
	public Object[] getArray()
	{
		return array;
	}
	/* methods */
	/** 设置列表的容积，只能扩大容积 */
	public void setCapacity(int len)
	{
		Object[] array=this.array;
		int c=array.length;
		if(len<=c) return;
		for(;c<len;c=(c<<1)+1)
			;
		Object[] temp=new Object[c];
		System.arraycopy(array,0,temp,0,size);
		this.array=temp;
	}
	/** 检索容器中的对象 */
	public Object get()
	{
		return getLast();
	}
	/** 得到列表的指定位置的元素 */
	public Object get(int index)
	{
		return array[index];
	}
	/** 得到列表的第一个元素 */
	public Object getFirst()
	{
		return array[0];
	}
	/** 得到列表的最后一个元素 */
	public Object getLast()
	{
		return array[size-1];
	}
	/** 判断列表是否包含指定的元素 */
	public boolean contain(Object obj)
	{
		return indexOf(obj,0)>=0;
	}
	/** 获得指定元素在列表中的位置，从开头向后查找 */
	public int indexOf(Object obj)
	{
		return indexOf(obj,0);
	}
	/** 获得指定元素在列表中的位置，从指定的位置向后查找 */
	public int indexOf(Object obj,int index)
	{
		int top=this.size;
		if(index>=top) return -1;
		Object[] array=this.array;
		if(obj==null)
		{
			for(int i=index;i<top;i++)
			{
				if(array[i]==null) return i;
			}
		}
		else
		{
			for(int i=index;i<top;i++)
			{
				if(obj.equals(array[i])) return i;
			}
		}
		return -1;
	}
	/** 获得指定元素在列表中的位置，从末尾向前查找 */
	public int lastIndexOf(Object obj)
	{
		return lastIndexOf(obj,size-1);
	}
	/** 获得指定元素在列表中的位置，从指定的位置向前查找 */
	public int lastIndexOf(Object obj,int index)
	{
		if(index>=size) return -1;
		Object[] array=this.array;
		if(obj==null)
		{
			for(int i=index;i>=0;i--)
			{
				if(array[i]==null) return i;
			}
		}
		else
		{
			for(int i=index;i>=0;i--)
			{
				if(obj.equals(array[i])) return i;
			}
		}
		return -1;
	}
	/** 设置列表的指定位置的元素，返回原来的元素 */
	public Object set(Object obj,int index)
	{
		if(index>=size)
			throw new ArrayIndexOutOfBoundsException(getClass().getName()
				+" set, invalid index="+index);
		Object o=array[index];
		array[index]=obj;
		return o;
	}
	/** 列表添加元素 */
	public boolean add(Object obj)
	{
		if(size>=array.length) setCapacity(size+1);
		array[size++]=obj;
		return true;
	}
	/** 在指定位置插入元素，元素在数组中的顺序不变 */
	public void add(Object obj,int index)
	{
		if(index<size)
		{
			if(size>=array.length) setCapacity(size+1);
			System.arraycopy(array,index,array,index+1,size-index);
			array[index]=obj;
			size++;
		}
		else
		{
			if(index>=array.length) setCapacity(index+1);
			array[index]=obj;
			size=index+1;
		}
	}
	/**
	 * 在指定位置插入元素，
	 * 元素在数组中的顺序改变，原插入的位置上的元素移到的最后，
	 */
	public void addAt(Object obj,int index)
	{
		if(index<size)
		{
			if(size>=array.length) setCapacity(size+1);
			array[size++]=array[index];
			array[index]=obj;
		}
		else
		{
			if(index>=array.length) setCapacity(index+1);
			array[index]=obj;
			size=index+1;
		}
	}
	/** 从列表移除指定的元素 */
	public boolean remove(Object obj)
	{
		int i=indexOf(obj,0);
		if(i<0) return false;
		remove(i);
		return true;
	}
	/**
	 * 从列表移除指定的元素，
	 * 元素在数组中的顺序被改变，原来最后一项移到被移除元素的位置，
	 */
	public boolean removeAt(Object obj)
	{
		int i=indexOf(obj,0);
		if(i<0) return false;
		removeAt(i);
		return true;
	}
	/** 从容器中移除对象 */
	public Object remove()
	{
		return remove(size-1);
	}
	/** 移除指定位置的元素，元素在数组中的顺序不变 */
	public Object remove(int index)
	{
		if(index>=size)
			throw new ArrayIndexOutOfBoundsException(getClass().getName()
				+" remove, invalid index="+index);
		Object[] array=this.array;
		Object obj=array[index];
		int j=size-index-1;
		if(j>0) System.arraycopy(array,index+1,array,index,j);
		array[--size]=null;
		return obj;
	}
	/**
	 * 移除指定位置的元素，
	 * 元素在数组中的顺序被改变，原来最后一项移到被移除元素的位置，
	 */
	public Object removeAt(int index)
	{
		if(index>=size)
			throw new ArrayIndexOutOfBoundsException(getClass().getName()
				+" removeAt, invalid index="+index);
		Object[] array=this.array;
		Object obj=array[index];
		array[index]=array[--size];
		array[size]=null;
		return obj;
	}
	/** 选择方法，用指定的选择器选出元素，返回值参考常量定义 */
	public int select(Selector selector)
	{
		int t;
		Object obj;
		int r=Selector.FALSE;
		Object[] array=this.array;
		for(int i=size-1;i>=0;i--)
		{
			obj=array[i];
			t=selector.select(obj);
			if(t==Selector.FALSE) continue;
			if(t==Selector.TRUE)
			{
				array[i]=array[--size];
				array[size]=null;
				r=t;
				continue;
			}
			if(t==Selector.TRUE_BREAK)
			{
				array[i]=array[--size];
				array[size]=null;
			}
			return t;
		}
		return r;
	}
	/** 清除列表中的所有元素 */
	public void clear()
	{
		Object[] array=this.array;
		for(int i=size-1;i>=0;i--)
			array[i]=null;
		size=0;
	}
	/** 以对象数组的方式得到列表中的元素 */
	public Object[] toArray()
	{
		Object[] temp=new Object[size];
		System.arraycopy(array,0,temp,0,size);
		return temp;
	}
	/** 将列表中的元素拷贝到指定的数组 */
	public int toArray(Object[] temp)
	{
		int len=(temp.length>size)?size:temp.length;
		System.arraycopy(array,0,temp,0,len);
		return len;
	}
	/* common methods */
	public Object clone()
	{
		try
		{
			ArrayList temp=(ArrayList)super.clone();
			Object[] array=temp.array;
			temp.array=new Object[temp.size];
			System.arraycopy(array,0,temp.array,0,temp.size);
			return temp;
		}
		catch(CloneNotSupportedException e)
		{
			throw new RuntimeException(getClass().getName()
				+" clone, capacity="+array.length,e);
		}
	}
	public String toString()
	{
		return super.toString()+"[size="+size+", capacity="+array.length+"]";
	}

}