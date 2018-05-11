package com.guse.stock.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.guse.stock.dao.model.User;

/**
 * 用户信息Dao
 * @author 不能
 *
 */
public interface IUserDao {

	/**
	 * 根据用户id获取用户
	 * @param userId 用户id
	 * @return 用户对象
	 */
	@Select("select * from rp_user where id=#{userId}")
	public User finbByUserId(@Param("userId")String userId);
}
