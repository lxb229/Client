

package zlib.set;

/**
 * 
* @Title: Comparator.java 
* @Package zlib.set 
* @Description: TODO(比较器) 
* @author hy  
* @date 2015年8月7日 下午2:18:41 
* @version V1.0
 */
public interface Comparator
{

	/** 比较结果常数，大于,等于,小于 */
	public static final int COMP_GRTR=1,COMP_EQUAL=0,COMP_LESS=-1;

	/* methods */
	/** 比较方法，返回比较结果常数 */
	int compare(Object o1,Object o2);

}