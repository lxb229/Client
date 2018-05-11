package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.DayUserCountDao;
import com.guse.four_one_nine.dao.LoginLoggerDao;
import com.guse.four_one_nine.dao.model.AppInstalled;
import com.guse.four_one_nine.dao.model.DayUserCount;
import com.guse.four_one_nine.dao.model.LoginLogger;
import com.guse.four_one_nine.dao.model.User;

/** 
* @ClassName: DayUserCountService 
* @Description: 用户日统计服务类
* @author Fily GUSE
* @date 2018年1月4日 下午4:01:00 
*  
*/
@Service
public class DayUserCountService {
	
	@Autowired
	DayUserCountDao dao;
	
	@Autowired
	LoginLoggerDao loginDao;
	
	/** 
	* @Title: userRegister 
	* @Description: 用户注册 
	* @param @param user
	* @return void 
	* @throws 
	*/
	public void userRegister(User user) {
		DayUserCount count = findToDay();
		count.setRegister_num(count.getRegister_num() + 1);
		dao.save(count);
	}
	
	/** 
	* @Description: 用户安装 应用软件 
	* @param @param app
	* @return void 
	* @throws 
	*/
	public void appInstaller(AppInstalled app) {
		DayUserCount count = findToDay();
		count.setAppinstaller_num(count.getAppinstaller_num() + 1);
		dao.save(count);
	}
	
	/** 
	* @Title: userReal 
	* @Description: 用户认证 
	* @param @param user
	* @return void 
	* @throws 
	*/
	public void userReal(User user) {
		
		DayUserCount count = findToDay();
		count.setReal_num(count.getReal_num() + 1);
		dao.save(count);
	}
	
	/** 
	* @Title: userSeller 
	* @Description: 用户注册成卖家 
	* @param @param user
	* @return void 
	* @throws 
	*/
	public void userSeller(User user) {
		
		DayUserCount count = findToDay();
		count.setSeller_num(count.getSeller_num() + 1);
		dao.save(count);
	}
	
	/** 
	* @Title: userLogin 
	* @Description: 用户登录 
	* @param @param user
	* @return void 
	* @throws 
	*/
	public void userLogin(LoginLogger user) {
		// 每个用户只统计一次
		int loginNum = loginDao.toDayLoginNum(user.getUser_id());
		if(loginNum == 0) {
			DayUserCount count = findToDay();
			count.setLive_num(count.getLive_num() + 1);
			dao.save(count);
		}
	}
	
	/* 获取当前统计信息 */
	public synchronized DayUserCount findToDay() {
		// 获取当天统计信息
		DayUserCount count = dao.getToDay();
		if(count == null) {
			// 创建当天统计信息
			dao.addToDay();
			count = dao.getToDay();
		}
		return count; 
	}
}
