package com.guse.platform.common.base;

import java.util.List;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;

/**
 * 通用service方法
 * @author nbin
 * @date 2017年7月28日 上午9:59:43 
 * @version V1.0
 */
public interface BaseService<T,PK>{
	
	/**
	 * 分页查询
	 * @Title: queryPageList 
	 * @date 2017年8月1日 下午1:50:14 
	 * @version V1.0
	 */
	Result<PageResult<T>> queryPageList(PageBean pageBean,T t,String orderBy);
	/**
	 * excel导出
	 * @Title: queryExportExcel 
	 * @date 2017年8月1日 下午1:50:25 
	 * @version V1.0
	 */
	List<T> queryExportExcel(T t);
	
	
	/**
	 * 插入
	 * @Title: insert 
	 * @date 2017年8月24日 下午3:44:09 
	 * @version V1.0
	 */
	PK insert(T t);
	
	
}
