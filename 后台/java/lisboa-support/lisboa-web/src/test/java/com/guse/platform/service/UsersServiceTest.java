package com.guse.platform.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.guse.platform.TestBase;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.service.system.UsersTestService;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class UsersServiceTest extends TestBase {
	
	@Autowired
	private UsersService userService;
	@Autowired
	private UsersTestService usersTestService;
	
	
	/*
	 * 分页查询用户
	 */
	@Test
	public void queryPageList(){
		Users user = new Users();
		user.setLoginName("admin");
		try {
			
			Result<PageResult<Users>> result = usersTestService.queryPageList(new PageBean(), user, "");
//			Result<PageResult<Users>> result = userService.queryPageListByUsers(null, new PageBean());
			System.out.println(JSON.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/*
	 * 新增用户
	 */
	@Test
    public void testAddUser(){
		Users user = new Users();
		try {
			Result<Integer> result = null;
			for (int i = 0; i < 30; i++) {
				user.setLoginName("admin"+i);
				result = userService.saveOrUpdateUser(user);
				System.out.println(JSON.toJSONString(result));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 用户分配角色
	 * @Title: rightUser 
	 * @param  
	 * @return void
	 */
	@Test
    public void rightUser(){
		Users user = new Users();
		user.setUserId(1);
		user.setRoleId(1);
		Result<Integer> result;
		try {
			result = userService.saveOrUpdateUser(user);
			System.out.println(JSON.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
	
	/*
	 * 启用禁用账号
	 */
	@Test
	public void enableDisableUser(){
		Users user = new Users();
		user.setUserId(31);
		user.setUserStatus(1);
		try {
			Result<Integer> result =  userService.enableDisableUser(user);
			System.out.println(JSON.toJSONString(result));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 删除用户 根据主键
	 */
	@Test
	public void delectUser(){
		try {
			Result<Integer> result =  userService.delectUser(31);
			System.out.println(JSON.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 分页查询用户
	 */
	@Test
	public void queryPageListUser(){
		Users user = new Users();
		user.setLoginName("admin");
		try {
			Result<PageResult<Users>> result = userService.queryPageListByUsers(null, new PageBean());
			System.out.println(JSON.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * 带条件的分页查询
	 */
	@Test
	public void queryPageListUserCondition(){
		Users user = new Users();
		user.setLoginName("admin");
		try {
			Result<PageResult<Users>> result = userService.queryPageListByUsers(user, new PageBean());
			System.out.println(JSON.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
