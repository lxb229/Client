package com.guse.chessgame.service.impl;

import java.lang.reflect.InvocationTargetException;

import org.chessgame.dao.LoginLogDao;
import org.chessgame.dao.bean.LoginLog;

import com.guse.chessgame.service.LoginLogService;

public class LoginLogServiceImpl implements LoginLogService {

	public String saveLoginLog(LoginLog log) {
		LoginLogDao logDao = new LoginLogDao();
		try {
			return logDao.saveLoginLog(log);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			logDao.closeDao();
		}
		return null;
	}

	public LoginLog seachLoginLog(String uid) {
		LoginLogDao logDao = new LoginLogDao();
		try {
			return logDao.seachLastLoginLog(uid);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			logDao.closeDao();
		}
		return null;
	}

}
