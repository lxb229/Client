package com.guse.four_one_nine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.SysUserDao;
import com.guse.four_one_nine.dao.model.SysUser;

/** 
* @ClassName: SysUserService 
* @Description: 系统用户 服务类
* @author Fily GUSE
* @date 2018年1月16日 下午3:03:34 
*  
*/
@Service
public class SysUserService {
	public final static Logger logger = LoggerFactory.getLogger(SysUserService.class);

	@Autowired
	SysUserDao dao;
	
	/** 
	* @Description: 获取用户信息 
	* @param @param username
	* @param @param password
	* @param @return
	* @return SysUser 
	* @throws 
	*/
	public SysUser getUser(String username,String password) {
		
		SysUser user = dao.findUser(username, password);
		if(user != null) {
			logger.info("{} 登录了系统", user.getName());
		}
		return user;
	}
}
