package com.guse.platform.service.system;



import java.util.List;

import com.guse.platform.common.base.Result;
import com.guse.platform.entity.system.Resource;


/**
 * 
 * @author nbin
 * @version 1.0
 * @CreateDate 2017-07-18 - 下午05:21:29
 */
public interface ResourceService{
	
	/**
	 * 新增更新资源
	 * @Title: saveOrUpdateResource 
	 * @param @param resource
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> saveOrUpdateResource(Resource resource);
	
	/**
	 * 删除资源
	 * @Title: deleteResource 
	 * @param @param resourceId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer>  deleteResource(Integer resourceId);
	
	/**
	 * 资源详细
	 * @Title: selectResourceDetial 
	 * @param @param resourceId
	 * @param @return 
	 * @return Result<Resource>
	 */
	Result<Resource> selectResourceDetial(Integer resourceId);
	
	
	/**
	 * 根据菜单id获得菜单下资源列表不需要分页
	 * @Title: getChildMenusList 
	 * @param @param menuId
	 * @param @return 
	 * @return Result<List<Resource>>
	 */
	Result<List<Resource>> getResourceListForMenuId(Integer srGroup);
	
}
