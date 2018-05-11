package com.guse.platform.dao.system;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.system.Menus;
import com.guse.platform.entity.system.Resource;

/**
 * 
 * 菜单下资源
 * @author nbin
*  @see ResourceMapper.xml
 * @version 1.0
 * @CreateDate 2017-7-13 - 上午11:34:26
 */
public interface ResourceMapper extends BaseMapper<Resource, Integer>{

	/**
	 * 新增资源
	 * @Title: insert 
	 * @param @param resource
	 * @param @return 
	 * @return Integer
	 */
	Integer insert(Resource resource);

	/**
	 * 更新资源
	 * @Title: updateByPrimaryKeySelective 
	 * @param @param resource
	 * @param @return 
	 * @return Integer
	 */
	Integer updateByPrimaryKeySelective(Resource resource);
	
	/**
	 * 删除资源
	 * @Title: deleteByPrimaryKey 
	 * @param @param resourceId
	 * @param @return 
	 * @return Integer
	 */
	Integer deleteByPrimaryKey(Integer resourceId);
	
	/**
	 * 资源明细
	 * @Title: selectByPrimaryKey 
	 * @param @param resourceId
	 * @param @return 
	 * @return Roles
	 */
	Resource selectByPrimaryKey(Integer resourceId);
	
	/**
	 * 根据菜单id查询所有资源
	 * @Title: selectResourceListByMenuId 
	 * @param @param menuId
	 * @param @return 
	 * @return List<Resource>
	 */
	List<Resource> selectResourceListByMenuId(Integer menuId);
	
	/**
	 * 根据菜单集合查询所有资源
	 * @Title: selectResourceListByMenuId 
	 * @param @param menuId
	 * @param @return 
	 * @return List<Resource>
	 */
	List<Resource> selectResourceListByMenuIds(@Param("list")List<Long> list);
	
}
