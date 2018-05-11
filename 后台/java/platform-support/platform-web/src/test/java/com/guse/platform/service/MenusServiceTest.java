package com.guse.platform.service;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.guse.platform.TestBase;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.system.Menus;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.system.MenusService;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.vo.system.MenuTreeVo;
import com.guse.platform.vo.system.MenusVo;

/**
 * 菜单测试类
 * @author nbin
 * @date 2017年7月19日 下午4:08:14 
 * @version V1.0
 */
public class MenusServiceTest extends TestBase {
	
	@Autowired
	private MenusService menusService;
	@Autowired
	private UsersService userService;
	
	/**
	 * 新增
	 * @Title: saveOrUpdateRoles 
	 * @param  
	 * @return void
	 */
	@Test
	public void insertMenus(){
		Menus menus = new Menus();
//		menus.setMenuTitle("后台管理");
		menus.setMenuTitle("登录日志");
		menus.setPid(3);
		menus.setMenuIcon("fa fa-cogs");
		menus.setMenuSort(1);
		menus.setMenuUrl("system/loginLogs.html");
		menus.setMenuStatus(Constant.DISABLE);
		menus.setCreateTime(new Date());
		menus.setUpdateTime(new Date());
		Result<Integer> result = menusService.insertUpdateMenus(menus);
		System.out.println(JSON.toJSONString(result));
	}
	
	/**
	 * 删除
	 * @Title: deleteMenus 
	 * @param  
	 * @return void
	 */
	@Test
	public void deleteMenus(){
		
	}
	/**
	 * 启禁用
	 * @Title: enableDisableMenus 
	 * @param  
	 * @return void
	 */
	@Test
	public void enableDisableMenus(){
		
	}
	
	/**
	 * 详细
	 * @Title: selectMenusDetial 
	 * @param  
	 * @return void
	 */
	@Test
	public void selectMenusDetial(){
		
	}
	
	/**
	 * 获取子菜单
	 * @Title: getChildMenusList 
	 * @param  
	 * @return void
	 */
	@Test
	public void getChildMenusList(){
		Result<List<Menus>> result = menusService.getChildMenusList(1);
		System.out.println(JSON.toJSONString(result));
	}
	
	/**
	 * 菜单树
	 * @Title: menusTree 
	 * @param  
	 * @return void
	 */
	@Test
	public void menusTree(){
		Result<List<MenuTreeVo>> result = menusService.menusTree();
		System.out.println(JSON.toJSONString(result));
	}
	
	/**
	 * 获取用户角色菜单
	 * @Title: getUserMenus 
	 * @param  
	 * @return void
	 */
	@Test
	public void getUserMenus(){
		Result<Users> result = userService.selectUserDetial(1);
		Result<List<MenusVo>> mr = menusService.getUserMenus(result.getData());
		System.out.println(JSON.toJSONString(mr));
		
	}
	
}
