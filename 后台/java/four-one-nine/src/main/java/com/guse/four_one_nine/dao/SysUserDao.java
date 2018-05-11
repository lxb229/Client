package com.guse.four_one_nine.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.SysUser;

/** 
* @ClassName: SysUserDao 
* @Description: 系统用户操作接口
* @author Fily GUSE
* @date 2018年1月16日 下午2:56:39 
*  
*/
@Repository
public interface SysUserDao {
	
	/** 
	* @Description: 查询系统用户 
	* @param @param username
	* @param @param password
	* @param @return
	* @return SysUser 
	* @throws 
	*/
	@Select("select * from sys_user where username=#{username} and password=#{password}")
	public SysUser findUser(@Param("username")String username, @Param("password")String password);

}
