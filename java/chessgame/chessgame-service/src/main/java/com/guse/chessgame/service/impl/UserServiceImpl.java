package com.guse.chessgame.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.chessgame.dao.UsersDao;
import org.chessgame.dao.bean.Users;
import org.chessgame.dao.vo.UserVo;
import org.apache.commons.lang.StringUtils;

import com.guse.chessgame.service.UserService;

public class UserServiceImpl implements UserService {

	public String saveUser(String uid, String nickname,
			String head_portrait, int sex, String create_ip) {
		Users user = new Users();
		user.setUid(uid);
		if(nickname != null && StringUtils.isNotBlank(nickname) ) {
			user.setUtype(1);
			user.setNickname(nickname);
		} else {
			user.setUtype(0);
			user.setNickname("游客");
		}
        user.setHead_portrait(head_portrait);
        user.setSex(sex);
        user.setOnline_state(0);
        user.setUser_type(1);
        user.setReal_name("");
        user.setId_card_number("");
        user.setStatus(1);
        user.setCreate_ip(create_ip);
        user.setCreate_time(new Date());
        user.setRecommend_id("");
		return saveUser(user);
	}

	public String saveUser(Users user) {
		UsersDao usersDao = new UsersDao();
		try {
			return usersDao.saveUser(user);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			usersDao.closeDao();
		}
		return null;
	}

	public Users getUserByUid(String uid) {
		UsersDao usersDao = new UsersDao();
		try {
			return usersDao.seachUser(uid);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			usersDao.closeDao();
		}
		return null;
	}

	public List<Users> getUsersBySeach(UserVo userVo) {
		UsersDao usersDao = new UsersDao();
		try {
			return usersDao.seachUser(userVo);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			usersDao.closeDao();
		}
		return null;
	}

	public int updateUser(Users user) {
		UsersDao usersDao = new UsersDao();
		try {
			return usersDao.updateUser(user);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			usersDao.closeDao();
		}
		return 0;
	}

	public int deleteUser(Users user) {
		UsersDao usersDao = new UsersDao();
		try {
			return usersDao.deleteUser(user);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			usersDao.closeDao();
		}
		return 0;
	}

}
