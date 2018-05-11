package org.chessgame.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.chessgame.dao.bean.Users;
import org.chessgame.dao.vo.UserVo;

import com.guse.chessgame.commonm.mongodb.MongodbManagerUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * UsersDao
 * @author 不能
 *
 */
public class UsersDao {

	MongodbManagerUtil mongoUtil = MongodbManagerUtil.getSingletonInstance();
	
	/**
	 * 销毁数据库连接
	 */
	public void closeDao() {
		mongoUtil.destroyDBLink();
	}
	/**
	 * 保存用户
	 * @param user 用户对象
	 * @return 保存成功后对象的ObjectId
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public String saveUser(Users user) throws IllegalArgumentException, IllegalAccessException {
		return mongoUtil.saveBean("users", user);
	}
	
	/**
	 * 根据uid返回对应User对象
	 * @param uid 用户唯一标识
	 * @return 符合条件的User对象
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public Users seachUser(String uid) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BasicDBObject uidDBObj = new BasicDBObject("uid",uid);
		List<DBObject> userDBobject = mongoUtil.seachBean("users", uidDBObj);
		if(userDBobject != null && userDBobject.size() > 0) {
			DBObject userObject = userDBobject.get(0);
			Users user = new Users();
			return mongoUtil.dbObjectToBean(userObject, user);
		} else {
			return null;
		}
	}
	
	/**
	 * 根据搜索条件查询用户对象
	 * @param user
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	public List<Users> seachUser(UserVo userVo) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		BasicDBObject seachUser = (BasicDBObject) mongoUtil.beanToDBObject(userVo);
		List<DBObject> userDBobject = mongoUtil.seachBean("users", seachUser);
		List<Users> userList = new ArrayList<Users>();
		for (int i = 0; i < userDBobject.size(); i++) {
			DBObject  userObject = userDBobject.get(i);
			Users userTemplate = new Users();
			userList.add(mongoUtil.dbObjectToBean(userObject, userTemplate));
		}
		return userList;
	}
	
	/**
	 * 更新用户信息
	 * @param newUser 用户最新信息
	 * @return 更新数量
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public int updateUser(Users  newUser) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Users oldUser = seachUser(newUser.getUid());
		BasicDBObject oldObject = (BasicDBObject) mongoUtil.beanToDBObject(oldUser);
		BasicDBObject nowObject = (BasicDBObject) mongoUtil.beanToDBObject(newUser);
		return mongoUtil.updateObject("users", oldObject, nowObject);
	}
	
	/**
	 * 删除用户(假删除，更改用户有效状态)
	 * @param userVo 用户Vo
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public int deleteUser(Users delUser) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		delUser.setStatus(0);
		return updateUser(delUser);
	}
	
}
