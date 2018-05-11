package org.chessgame.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.chessgame.dao.bean.Players;
import org.chessgame.dao.bean.Users;

import com.guse.chessgame.commonm.mongodb.MongodbManagerUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 玩家Dao
 * @author 不能
 *
 */
public class PlayerDao {
	
	MongodbManagerUtil mongoUtil = MongodbManagerUtil.getSingletonInstance();
	
	/**
	 * 销毁数据库连接
	 */
	public void closeDao() {
		mongoUtil.destroyDBLink();
	}
	
	/**
	 * 保存玩家
	 * @param player 玩家对象
	 * @return 保存成功后对象的ObjectId
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public String savePlayer(Players player) throws IllegalArgumentException, IllegalAccessException {
		return mongoUtil.saveBean("players", player);
	}
	
	/**
	 * 根据用户查询相应的玩家
	 * @param user 用户对象
	 * @return 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public Players seachPlayer(Users user) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BasicDBObject dbObj = new BasicDBObject("user",user);
		
		List<DBObject> playList = mongoUtil.seachBean("players", dbObj);
		if(playList.size() > 0) {
			Players player = new Players();
			return mongoUtil.dbObjectToBean(playList.get(0), player);
		} else {
			return null;
		}
	}
	
	/**
	 * 修改玩家资料信息
	 * @param players 玩家最新信息
	 * @return 修改更新数据条数
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public int updatePlayer(Players players) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		UsersDao usersDao = new UsersDao();
		String uid = players.getUser().getUid();
		Users oldUser = usersDao.seachUser(uid);
		Players oldPlayer = this.seachPlayer(oldUser);
		DBObject oldObject = mongoUtil.beanToDBObject(oldPlayer);
		DBObject nowObject = mongoUtil.beanToDBObject(players);
		return mongoUtil.updateObject("players", oldObject, nowObject);
	}
	
	/**
	 * 获取玩家最新编号
	 * @return 玩家最新编号
	 */
	public int getPlayerSequence() {
		return mongoUtil.getSequence("players");
	}

}
