package com.guse.platform.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.guse.platform.TestBase;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.system.Roles;
import com.guse.platform.service.system.RolesService;

/**
 * 角色测试类
 * @author nbin
 * @date 2017年7月19日 下午4:07:06 
 * @version V1.0
 */
public class RolesServiceTest extends TestBase {
		
	@Autowired
	private RolesService rolesService;
	
	/**
	 * 新增
	 * @Title: saveOrUpdateRoles 
	 * @param  
	 * @return void
	 */
	@Test
	public void saveOrUpdateRoles(){
		Roles roles = new Roles();
		roles.setRoleName("系统管理员");
		Result<Integer> result = rolesService.saveOrUpdateRoles(roles);
		System.out.println(JSON.toJSONString(result));
	}
	
	/**
	 * 删除
	 * @Title: deleteRoles 
	 * @param  
	 * @return void
	 */
	@Test
	public void deleteRoles(){
		
	}
	
	/**
	 * 详细
	 * @Title: selectRulesDetial 
	 * @param  
	 * @return void
	 */
	@Test
	public void selectRulesDetial(){
		
	}
	
	/**
	 * 启禁用
	 * @Title: enableDisableRoles 
	 * @param  
	 * @return void
	 */
	@Test
	public void enableDisableRoles(){
		
	}
	
	/**
	 * 菜单权限
	 * @Title: menusRights 
	 * @param  
	 * @return void
	 */
	@Test
	public void menusRights(){
		String menusIds = "1,2,3,4,5,6,7,8";
		Integer roleId = 1;
		Result<Integer> result = rolesService.menusRights(menusIds, roleId);
		System.out.println(JSON.toJSONString(result));
	}
	
	/**
	 * 菜单资源权限
	 * @Title: menusResourceRigths 
	 * @param  
	 * @return void
	 */
	@Test
	public void menusResourceRigths(){
		
	}
}
