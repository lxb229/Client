package com.guse.platform.service.system;

import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.system.Users;
import com.guse.platform.vo.system.AccountVo;

/**
 * 
 * @author nbin
 * @version 1.0
 * @CreateDate 2017-07-18 - 下午05:21:29
 */
public interface UsersService{
	
	/**
	 * 用户登录
	* @Title: login 
	* @param @param username
	* @param @param password
	* @param @return 
	* @return Result<Users>  
	 */
	public Result<Users> loginUser(String username ,String password);
	
	/**
	 * 分页获取用户列表
	* @Title: queryPageListByUsers 
	* @param @return 
	* @return Result<PageResult<UsersVo>>  
	 */
	public Result<PageResult<Users>> queryPageListByUsers(Users user,PageBean pageBean);
	
	/**
	 * 新增编辑用户
	* @Title: saveOrUpdateUser 
	* @param @param user
	* @param @return 
	* @return Result<UsersVo>  
	 */
	public Result<Integer> saveOrUpdateUser(Users user); 
	
	
	/**
	 * 删除用户
	* @Title: delectUser 
	* @param @param userId
	* @param @return 
	* @return Result<Integer>  
	 */
	public Result<Integer> delectUser(Integer userId); 
	
	
	/**
	 * 启用禁用用户
	 * @Title: enableDisableUser 
	 * @param @param user
	 * @param @return 
	 * @return Result<Integer>
	 */
	public Result<Integer> enableDisableUser(Users user);
	
	/**
	 * 用户信息
	 * @Title: selectUserDetial 
	 * @param @param userId
	 * @param @return
	 * @param @throws Exception 
	 * @return Result<UsersVo>
	 */
	public Result<Users> selectUserDetial(Integer userId);
	
	/**
	 * 设置用户角色
	 * @Title: setUserRole 
	 * @param userId 用户ID
	 * @param roleId 角色ID
	 * @return Result<Integer>
	 */
	public Result<Integer> setUserRole(Integer userId,Integer roleId);
	
	/**
	 * 从游戏服务器获取用户集合
	 * @return
	 */
	public Result<PageResult<AccountVo>> queryAccountList(Users user, PageBean pageBean);
	
	/**
	 * 增加或者减少玩家金币
	 * @param accountVo
	 * @return
	 */
	public Result<Integer> updateRoomcard(AccountVo accountVo);
	
	/**
	 * 修改密码
	 * @param accountVo
	 * @return
	 */
	public Result<Integer> updateUserPass(AccountVo accountVo);
}
