package org.chessgame.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.chessgame.dao.bean.LoginLog;

import com.guse.chessgame.commonm.mongodb.MongodbManagerUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 用户登录日志Dao
 * @author 不能
 *
 */
public class LoginLogDao {
	
	MongodbManagerUtil mongoUtil = MongodbManagerUtil.getSingletonInstance();
	
	/**
	 * 销毁数据库连接
	 */
	public void closeDao() {
		mongoUtil.destroyDBLink();
	}
	
	/**
	 * 保存用户登录日志
	 * @param log 用户登录日志
	 * @return 保存成功后对象的ObjectId
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public String saveLoginLog(LoginLog log) throws IllegalArgumentException, IllegalAccessException {
		return mongoUtil.saveBean("login_log", log);
	}
	
	/**
	 * 获取用户最新的登录日志信息
	 * @param uid 用户唯一标识
	 * @return 用户最新的登录日志
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public LoginLog seachLastLoginLog(String uid) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BasicDBObject seachobject = new BasicDBObject("uid", uid);
		BasicDBObject orderByObject = new BasicDBObject("login_time",-1);
		List<DBObject> objList = mongoUtil.seachBean("login_log", seachobject, 1, 1, orderByObject);
		if(objList.size() > 0) {
			LoginLog log = new LoginLog();
			return mongoUtil.dbObjectToBean(objList.get(0), log);
		} else {
			return null;
		}
		
	}

}
