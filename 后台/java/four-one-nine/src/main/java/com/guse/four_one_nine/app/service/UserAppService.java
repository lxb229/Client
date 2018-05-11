package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.UserDao;
import com.guse.four_one_nine.dao.model.User;

/**
 * @ClassName: UserService
 * @Description: 用户管理
 * @author: wangkai
 * @date: 2018年1月8日 下午2:23:30
 * 
 */
@Service
public class UserAppService {

	@Autowired
	UserDao userDao;


	/**
	 * 新增用户
	 * 
	 * @param user
	 */
	public boolean addUser(User user) {
		return userDao.addUser(user);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 */
	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	/**
	 * 用户认证
	 * 
	 * @param user
	 */
	public void userCertification(User user) {
		userDao.userCertification(user);
	}

	/**
	 * 用户卖家认证
	 * 
	 * @param user
	 */
	public void userMerchantsCertification(User user) {
		userDao.userMerchantsCertification(user);
	}
}
