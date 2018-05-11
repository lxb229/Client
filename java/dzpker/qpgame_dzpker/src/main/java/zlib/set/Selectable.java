

package zlib.set;

/**
 * 
* @Title: Selectable.java 
* @Package zlib.set 
* @Description: TODO(集合选择器) 
* @author hy  
* @date 2015年8月7日 下午2:17:27 
* @version V1.0
 */

public interface Selectable
{

	/** 选择方法，用指定的选择器选出元素，返回值参考常量定义 */
	public int select(Selector selector);

}