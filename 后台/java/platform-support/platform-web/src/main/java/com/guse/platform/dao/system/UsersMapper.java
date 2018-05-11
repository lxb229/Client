package com.guse.platform.dao.system;

import org.apache.ibatis.annotations.Param;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.system.Users;


/**
 * 用户
 * @author nbin
*  @see UsersMapper.xml
 * @version 1.0
 * @CreateDate 2017-7-13 - 上午11:34:26
 */
public interface UsersMapper extends BaseMapper<Users, Integer>{
	/**
	 *  获取登录用户信息
	 * @Title: login 
	 * @param @param username
	 * @param @return 
	 * @return Users
	 */
	Users getUserByLoginName(String username);
	
	/**
	 * 根据昵称获取用户
	 * @param nick
	 * @return
	 */
	Users getUserByNick(String nick);
	/**
	 * 新增用户
	 * @Title: insert 
	 * @param @param user
	 * @param @return 
	 * @return Integer
	 */
	Integer insert(Users user);
	/**
	 * 更新用户信息
	 * @Title: updateByPrimaryKeySelective 
	 * @param @param user
	 * @param @return 
	 * @return Integer
	 */
	Integer updateByPrimaryKeySelective(Users user);
	/**
	 * 删除用户根据主键
	 * @Title: deleteByPrimaryKey 
	 * @param @param userId
	 * @param @return 
	 * @return Integer
	 */
	Integer deleteByPrimaryKey(Integer userId);
	
	/**
	 * 根据主键查询
	 * @Title: selectByPrimaryKey 
	 * @param @param userId
	 * @param @return 
	 * @return Users
	 */
	Users selectByPrimaryKey(Integer userId);
	/**
	 * 根据明星号和唯一标识获取用户
	 * @param startNo 
	 * @return
	 */
	Users getUserByStartNo(@Param("accountId") Long accountId ,@Param("startNo")String startNo);
	/**
	 * 根据账号唯一标识获取用户
	 * @param accountId
	 * @return
	 */
	Users getUserByAccountId(@Param("accountId")Long accountId );
	
	/**
	 * 根据明星号获取用户
	 * @param startNo
	 * @return
	 */
	Users getUserBy(@Param("startNo")Integer startNo);
	
	/**
	 * 根据电话号码标识获取用户
	 * @param accountId
	 * @return
	 */
	Users getUserByPhone(@Param("phone")String phone );
	
}
