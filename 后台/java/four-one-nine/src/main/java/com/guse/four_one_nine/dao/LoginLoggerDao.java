package com.guse.four_one_nine.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.guse.four_one_nine.dao.model.LoginLogger;

/** 
* @ClassName: LoginLoggerDao 
* @Description: 登录日志接口
* @author Fily GUSE
* @date 2018年1月4日 11:43:54 
*  
*/
@Repository
public interface LoginLoggerDao {
	
	/** 
	* @Title: addLoginLogger
	* @Description: 添加登录日志 
	* @param @param loginLogger
	* @return void 
	* @throws 
	*/	
	@Insert("insert into login_logger(user_id,login_date,login_ip,login_device,login_site) values"+
			"(#{user_id},#{login_date},#{login_ip},#{login_device},#{login_site})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addLoginLogger(LoginLogger loginLogger);
	
	/** 
	* @Title: toDayLoginNum 
	* @Description: 查询指定用户当天登录次数 
	* @param @param userId
	* @return void 
	* @throws 
	*/
	@Select("SELECT COUNT(1) FROM `login_logger` "
			+ "WHERE TO_DAYS(login_date) = TO_DAYS(NOW()) AND user_id = #{userId}")
	public Integer toDayLoginNum(@Param("userId") long userId);

}
