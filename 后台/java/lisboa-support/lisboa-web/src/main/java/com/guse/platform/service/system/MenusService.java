package com.guse.platform.service.system;

import java.util.List;

import com.guse.platform.common.base.Result;
import com.guse.platform.entity.system.Menus;
import com.guse.platform.entity.system.Roles;
import com.guse.platform.entity.system.Users;
import com.guse.platform.vo.system.MenuTreeVo;
import com.guse.platform.vo.system.MenusVo;

/**
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface MenusService{
	
	/**
	 * 获取角色分配的菜单列表
	 * @param roles 角色
	 * @return
	 */
	Result<List<Menus>> getRoleMenus(Roles roles);
	
	/**
	 * 用户所在角色菜单
	 * @Title: getUserMenus 
	 * @param @param usersVo
	 * @param @return 
	 * @return Result<List<Menus>>
	 */
	Result<List<MenusVo>> getUserMenus(Users user);
	
	/**
	 * 所有菜单树
	 * @Title: menusTree 
	 * @param @return 
	 * @return Result<List<Menus>>
	 */
	Result<List<MenuTreeVo>> menusTree();
	/**
	 * 根据菜单id获得子菜单列表 不需要分页
	 * @Title: getChildMenusList 
	 * @param @param userId
	 * @param @return 
	 * @return Result<List<Menus>>
	 */
	Result<List<Menus>> getChildMenusList(Integer menusId);
	
	/**
	 * 新建更新菜单
	 * @Title: insertMenus 
	 * @param @param pid
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> insertUpdateMenus(Menus menus);
	
	
	/**
	 * 删除菜单
	 * @Title: deleteMenus 
	 * @param @param userId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> deleteMenus(Integer menusId);
	
	/**
	 * 启用禁用菜单
	 * @Title: enableDisableMenus 
	 * @param @param menusVo
	 * @param @return
	 * @param @throws Exception 
	 * @return Result<Integer>
	 */
	Result<Integer> enableDisableMenus(Menus menus);
	
	/**
	 * 获取菜单详细
	 * @Title: selectMenusDetial 
	 * @param @param menusId
	 * @param @return 
	 * @return Result<Menus>
	 */
	Result<Menus> selectMenusDetial(Integer menusId);
	
}
