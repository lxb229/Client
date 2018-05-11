package com.guse.platform.dao.system;

import java.util.List;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.system.Roles;


/**
 * 角色
 * @author nbin
*  @see RolesMapper.xml
 * @version 1.0
 * @CreateDate 2017-7-13 - 上午11:34:26
 */
public interface RolesMapper extends BaseMapper<Roles, Integer>{
	
	/**
	 * 新增角色
	 * @Title: insert 
	 * @param @param roles
	 * @param @return 
	 * @return Integer
	 */
	Integer insert(Roles roles);
	/**
	 * 更新信息
	 * @Title: updateByPrimaryKeySelective 
	 * @param @param user
	 * @param @return 
	 * @return Integer
	 */
	Integer updateByPrimaryKeySelective(Roles roles);
	/**
	 * 删除用户根据主键
	 * @Title: deleteByPrimaryKey 
	 * @param @param userId
	 * @param @return 
	 * @return Integer
	 */
	Integer deleteByPrimaryKey(Integer rolesId);
	
	/**
	 * 根据主键查询
	 * @Title: selectByPrimaryKey 
	 * @param @param userId
	 * @param @return 
	 * @return Users
	 */
	Roles selectByPrimaryKey(Integer rolesId);
	
	/**
	 * 根据角色名称获取角色
	 * @Title: getRolesByRoleName 
	 * @param @param roleName
	 * @param @return 
	 * @return List<Roles>
	 */
	List<Roles> getRolesByRoleName(String roleName);
}
