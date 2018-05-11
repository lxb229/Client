package com.guse.platform.service.system;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.SystemTask;
import com.guse.platform.entity.system.Users;

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
	public Result<PageResult<Users>> queryPageListByUsers(Users user,PageBean pageBean, Users roleuUser);
	
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
	 * 发送验证码之后将验证码写入用户信息
	 * @Title: setUserRole 
	 * @param phone 电话号码
	 * @param smsCode 验证码
	 * @return Result<Integer>
	 */
	public Result<Integer> sendVerCode(String phone, String smsCode);
	
	/**
	 * 根据明星号获取用户
	 * @param startNo
	 * @return
	 */
	public Users getUserByStartNo(Long accountId,String startNo);
	
	/**
	 * 根据accountId获取用户
	 * @param accountId
	 * @return
	 */
	public Users getUserByAccountId(Long accountId);
	
	/**
	 * 处理任务中的玩家数据
	 * @param task
	 */
	public void taskUsers(SystemTask task);
	
	/**
	 * 处理mq中的玩家信息
	 * @param obj
	 * @return
	 */
	public Result<Integer> processingUser(JSONObject obj);
	
}
