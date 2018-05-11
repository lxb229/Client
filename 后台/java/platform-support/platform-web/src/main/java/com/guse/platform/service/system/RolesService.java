package com.guse.platform.service.system;



import java.util.List;

import com.guse.platform.common.base.Result;
import com.guse.platform.entity.system.Roles;


/**
 * 
 * @author nbin
 * @version 1.0
 * @CreateDate 2017-07-18 - 下午05:21:29
 */
public interface RolesService{
	
	/**
	 * 角色列表
	 * @Title: queryListRoles 
	 * @param @param roles
	 * @param @return 
	 * @return Result<List<Roles>>
	 */
	Result<List<Roles>> queryListRoles(Roles roles);
	
	/**
	 * 新增更新角色
	 * @Title: saveOrUpdateRole 
	 * @param @param roles
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> saveOrUpdateRoles(Roles roles);
	
	/**
	 * 删除角色
	 * @Title: deleteRoles 
	 * @param @param roleId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer>  deleteRoles(Integer roleId);
	
	/**
	 * 角色详细
	 * @Title: selectRulesDetial 
	 * @param @param roleId
	 * @param @return 
	 * @return Result<Roles>
	 */
	Result<Roles> selectRulesDetial(Integer roleId);
	
	
	/**
	 * 启用禁用角色
	 * @Title: enableDisableUser 
	 * @param @param user
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> enableDisableRoles(Roles roles);
	
	
	/**
	 * 菜单授权
	 * @Title: menusRights 
	 * @param @param menusIds
	 * @param @param roleId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> menusRights(String menusIds,Integer roleId);
	
	/**
	 * 菜单下资源授权
	 * @Title: menusResourceRigths 
	 * @param @param resouceIds
	 * @param @param roleId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> menusResourceRigths(String resouceIds,Integer roleId);
	
	
	
}
