

package zlib.set;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
* @Title: SetKit.java 
* @Package zlib.set 
* @Description: TODO(集合工具类) 
* @author hy  
* @date 2015年8月7日 下午2:18:05 
* @version V1.0
 */
public final class SetKit
{

	/* static fields */
	/** 库信息 */
	public static final String toString=SetKit.class.getName();

	/** 快速排序的起步常数 */
	public static final int QUICK_SORT_LIMIT=20;
	/** 堆栈长度 */
	private static final int STACK_LENGTH=32;
	/** 快速排序的中值划分的阀值 */
	private static final int THRESHOLD=7;

	/* static methods */
	/** 获得指定类名的数组 */
	public static Object getArray(String className,int length)
	{
		return getArray(null,className,length);
	}
	/** 获得使用指定类加载器的类名的数组 */
	public static Object getArray(ClassLoader loader,String className,
		int length)
	{
		try
		{
			Class c=(loader!=null)?loader.loadClass(className):Class
				.forName(className);
			return Array.newInstance(c,length);
		}
		catch(ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
	/** 选择方法，用指定的选择器选出元素，返回值参考常量定义 */
	public static int select(Map map,Selector selector)
	{
		int t;
		Object obj;
		int r=Selector.FALSE;
		Iterator it=map.entrySet().iterator();
		while(it.hasNext())
		{
			obj=it.next();
			t=selector.select(obj);
			if(t==Selector.FALSE) continue;
			if(t==Selector.TRUE)
			{
				it.remove();
				r=t;
				continue;
			}
			if(t==Selector.TRUE_BREAK) it.remove();
			return t;
		}
		return r;
	}
	/** 选择方法，用指定的选择器选出元素，返回值参考常量定义 */
	public static int select(Collection collection,Selector selector)
	{
		int t;
		Object obj;
		int r=Selector.FALSE;
		Iterator it=collection.iterator();
		while(it.hasNext())
		{
			obj=it.next();
			t=selector.select(obj);
			if(t==Selector.FALSE) continue;
			if(t==Selector.TRUE)
			{
				it.remove();
				r=t;
				continue;
			}
			if(t==Selector.TRUE_BREAK) it.remove();
			return t;
		}
		return r;
	}
	/** 选择方法，用指定的选择器选出元素，返回值参考常量定义 */
	public static int select(List list,Selector selector)
	{
		int t;
		Object obj;
		int r=Selector.FALSE;
		for(int i=list.size()-1;i>=0;i--)
		{
			obj=list.get(i);
			t=selector.select(obj);
			if(t==Selector.FALSE) continue;
			if(t==Selector.TRUE)
			{
				list.remove(i);
				r=t;
				continue;
			}
			if(t==Selector.TRUE_BREAK) list.remove(i);
			return t;
		}
		return r;
	}
	/**
	 * 对象数组排序方法，
	 * 默认为升序，数组长度小于指定长度用希尔排序，否则用快速排序，
	 * 
	 * @param array 为要排序的对象数组，
	 * @param comparator 为对象比较器，
	 */
	public static void sort(Object[] array,Comparator comparator)
	{
		sort(array,0,array.length,comparator,false);
	}
	/**
	 * 对象数组排序方法，
	 * 默认为升序，数组长度小于指定长度用希尔排序，否则用快速排序，
	 * 
	 * @param array 为要排序的对象数组，
	 * @param offset 为排序的起始偏移位置，
	 * @param len 为排序的长度，
	 * @param comparator 为对象比较器，
	 */
	public static void sort(Object[] array,int offset,int len,
		Comparator comparator)
	{
		sort(array,offset,len,comparator,false);
	}
	/**
	 * 对象数组排序方法， 数组长度小于指定长度用希尔排序，否则用快速排序，
	 * 
	 * @param array 为要排序的对象数组，
	 * @param comparator 为对象比较器，
	 * @param descending 表示是否为降序，
	 */
	public static void sort(Object[] array,Comparator comparator,
		boolean descending)
	{
		sort(array,0,array.length,comparator,descending);
	}
	/**
	 * 对象数组排序方法， 数组长度小于指定长度用希尔排序，否则用快速排序，
	 * 
	 * @param array 为要排序的对象数组，
	 * @param offset 为排序的起始偏移位置，
	 * @param len 为排序的长度，
	 * @param comparator 为对象比较器，
	 * @param descending 表示是否为降序，
	 */
	public static void sort(Object[] array,int offset,int len,
		Comparator comparator,boolean descending)
	{
		// 对小数组排序，希尔排序更好一些
		if(array.length<QUICK_SORT_LIMIT)
			shellSort(array,offset,len,comparator,descending);
		else
			quickSort(array,offset,len,comparator,descending);
	}
	/* 比较高效的希尔排序 */
	public static void shellSort(Object[] array,int offset,int len,
		Comparator comparator,boolean descending)
	{
		if(len<=0) return;
		if(offset<0) offset=0;
		if(offset+len>array.length) len=array.length-offset;
		// 定义排序方向
		int comp=descending?Comparator.COMP_LESS:Comparator.COMP_GRTR;
		// 定义及计算增量值
		int inc=1;
		//增量随n值的变化动态变化
		for(int n=len/9;inc<=n;inc=3*inc+1)
			;
		Object o;
		int j;
		for(;inc>0;inc/=3)
		{
			for(int i=inc,n=offset+len;i<n;i+=inc)
			{
				o=array[i];
				j=i;
				while((j>=inc)&&(comparator.compare(array[j-inc],o)==comp))
				{
					array[j]=array[j-inc];
					j-=inc;
				}
				array[j]=o;
			}
		}
	}
	/* 快速排序 */
	public static void quickSort(Object[] array,int offset,int len,
		Comparator comparator,boolean descending)
	{
		if(len<=0) return;
		if(offset<0) offset=0;
		if(offset+len>array.length) len=array.length-offset;
		// 定义排序方向
		int comp=descending?Comparator.COMP_GRTR:Comparator.COMP_LESS;
		// 创建左右堆栈
		int size=STACK_LENGTH;
		int[] lefts=new int[size];
		int[] rights=new int[size];
		int top=0;
		// 三元素比较，取中值，中值划分的最小数据长度
		final int threshold=THRESHOLD;
		// 左右堆栈的大小
		int lsize,rsize;
		// 左右初始值
		int l=offset,r=offset+len-1;
		// 中间，左，右，枢轴指针
		int mid,scanl,scanr,pivot;
		Object temp;
		// 主循环
		while(true)
		{
			while(r>l)
			{
				// 中值划分，取左右和中间位置的元素进行比较，
				// 取值为中间的元素作为枢轴
				if((r-l)>threshold)
				{
					mid=(l+r)/2;
					if(comparator.compare(array[mid],array[l])==comp)
					{
						temp=array[mid];
						array[mid]=array[l];
						array[l]=temp;
					}
					if(comparator.compare(array[r],array[l])==comp)
					{
						temp=array[r];
						array[r]=array[l];
						array[l]=temp;
					}
					if(comparator.compare(array[r],array[mid])==comp)
					{
						temp=array[mid];
						array[mid]=array[r];
						array[r]=temp;
					}
					pivot=r-1;
					temp=array[mid];
					array[mid]=array[pivot];
					array[pivot]=temp;
					scanl=l+1;
					scanr=r-2;
				}
				else
				{
					pivot=r;
					scanl=l;
					scanr=r-1;
				}
				while(true)
				{
					// 扫描左边元素是否大于等于枢轴
					while((scanl<r)
						&&(comparator.compare(array[scanl],array[pivot])==comp))
						scanl++;
					// 扫描右边元素是否小于等于枢轴
					while((scanr>l)
						&&(comparator.compare(array[pivot],array[scanr])==comp))
						scanr--;
					// 如果左右扫描会合，则退出内层循环
					if(scanl>=scanr) break;
					// 交换元素
					temp=array[scanl];
					array[scanl]=array[scanr];
					array[scanr]=temp;
					if(scanl<r) scanl++;
					if(scanr>l) scanr--;
				}
				// 交换最后元素
				temp=array[scanl];
				array[scanl]=array[pivot];
				array[pivot]=temp;
				// 记录最大的分段到相应的堆栈中
				lsize=scanl-l;
				rsize=r-scanl;
				if(lsize>rsize)
				{
					if(lsize!=1)
					{
						top++;
						if(top==size)
							throw new IllegalArgumentException(toString
								+" quickSort, stack overflow");
						lefts[top]=l;
						rights[top]=scanl-1;
					}
					if(rsize==0) break;
					l=scanl+1;
				}
				else
				{
					if(rsize!=1)
					{
						top++;
						if(top==size)
							throw new IllegalArgumentException(toString
								+" quickSort, stack overflow");
						lefts[top]=scanl+1;
						rights[top]=r;
					}
					if(lsize==0) break;
					r=scanl-1;
				}
			}
			if(top==0) break;
			l=lefts[top];
			r=rights[top--];
		}
	}

	/* constructors */
	private SetKit()
	{
	}

}