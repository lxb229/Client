package org.chessgame.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.chessgame.dao.bean.Versions;

import com.guse.chessgame.commonm.mongodb.MongodbManagerUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 版本信息Dao
 * @author 不能
 *
 */
public class VersionsDao {
	
	MongodbManagerUtil mongoUtil = MongodbManagerUtil.getSingletonInstance();
	
	/**
	 * 销毁数据库连接
	 */
	public void closeDao() {
		mongoUtil.destroyDBLink();
	}
	
	/**
	 * 保存版本信息
	 * @param log 用户登录日志
	 * @return 保存成功后对象的ObjectId
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public String savePlayer(Versions versions) throws IllegalArgumentException, IllegalAccessException {
		return mongoUtil.saveBean("versions", versions);
	}
	
	/**
	 * 获取最新的版本信息
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public Versions getNewestVersion() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		BasicDBObject seachobject = new BasicDBObject();
		BasicDBObject orderByObject = new BasicDBObject("create_time",-1);
		List<DBObject> objList = mongoUtil.seachBean("versions", seachobject, 1, 1, orderByObject);
		if(objList.size() > 0) {
			Versions version = new Versions();
			return mongoUtil.dbObjectToBean(objList.get(0), version);
		} else {
			return null;
		}
	}

}
