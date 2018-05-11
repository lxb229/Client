package com.guse.platform.dao.system;


import java.util.List;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.system.Menus;


/**
 * 菜单
 * @see MenusMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface MenusMapper extends  BaseMapper<Menus, Integer>{

	/**
	 * 新增菜单
	 * @Title: insert 
	 * @param @param menus
	 * @param @return 
	 * @return Integer
	 */
	Integer insert(Menus menus);
	/**
	 * 更新菜单
	 * @Title: updateByPrimaryKeySelective 
	 * @param @param user
	 * @param @return 
	 * @return Integer
	 */
	Integer updateByPrimaryKeySelective(Menus menus);
	/**
	 * 删除菜单根据主键
	 * @Title: deleteByPrimaryKey 
	 * @param @param userId
	 * @param @return 
	 * @return Integer
	 */
	Integer deleteByPrimaryKey(Integer menuId);
	
	/**
	 * 根据主键查询菜单
	 * @Title: selectByPrimaryKey 
	 * @param @param menuId
	 * @param @return 
	 * @return Menus
	 */
	Menus selectByPrimaryKey(Integer menuId);
	
	/**
	 * 获取所有父id下的子菜单
	 * @Title: selectChildMenusList 
	 * @param @param pid
	 * @param @return 
	 * @return List<Menus>
	 */
	List<Menus> selectChildMenusList(Integer pid);
	
	/**
	 * 获得所有菜单
	 * @Title: selectAllMenusList 
	 * @param @return 
	 * @return List<Menus>
	 */
	List<Menus> selectAllMenusList();
	
}
