package com.guse.chessgame.service;

import org.chessgame.dao.bean.LoginLog;

public interface LoginLogService {
	
	/**
	 * 保存登陆日志对象
	 * @return 登录日志在数据库的ObjectId
	 */
	public String saveLoginLog(LoginLog log);
	
	/**
	 * 查询登录日志对象
	 * @return 登录日志对象
	 */
	public LoginLog seachLoginLog(String uid);

}
