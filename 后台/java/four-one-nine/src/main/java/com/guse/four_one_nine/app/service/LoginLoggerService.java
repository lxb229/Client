package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.LoginLoggerDao;
import com.guse.four_one_nine.dao.model.LoginLogger;

/** 
* @ClassName: LoginLoggerService
* @Description: 登录日志管理
* @author: wangkai
* @date: 2018年1月8日 下午4:17:08 
*  
*/
@Service
public class LoginLoggerService {

	@Autowired
	LoginLoggerDao loginDao;
	
	/**
	 * @param loginLogger
	 */
	public void addLoginLogger(LoginLogger loginLogger){
		loginDao.addLoginLogger(loginLogger);
	}
}
