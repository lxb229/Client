package com.guse.stock.mongo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.guse.stock.common.JSONUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;

public class MongodbManagerUtil {

	private static final Logger logger = Logger
			.getLogger(MongodbManagerUtil.class);

	private ServerAddress serverAddress = null;
	private Mongo mg = null;
	private DB db = null;
	private String username = null;
	private String password = null;
	private volatile static MongodbManagerUtil singleton = null;
	public static final String KEY = "key";
	public static final String VALUE = "value";

	public static MongodbManagerUtil getSingletonInstance() {

		if (singleton == null) {
			synchronized (MongodbManagerUtil.class) {
				if (singleton == null) {
					singleton = new MongodbManagerUtil();
				}
			}
			singleton = new MongodbManagerUtil();
		}
		return singleton;
	}

	private MongodbManagerUtil() {

		if (logger.isDebugEnabled()) {

			logger.debug("MongodbCacheManagerUtil() - start ");
			//$NON-NLS-1$
		}

		try {
			serverAddress = new ServerAddress("139.129.53.180", 27017);
			mg = new Mongo(serverAddress);
			db = mg.getDB("db_php_dmh");
			username = "php-dev";
			password = "guse2017";
			if (!db.authenticate(username, password.toCharArray())) {
				logger.debug("username or password error");
				return;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("MongodbCacheManagerUtil() - end");
			//$NON-NLS-1$

		}

	}
	
	/** 
     * 销毁数据库连接 
     */  
    public void destroyDBLink(){
    	try {
			serverAddress = new ServerAddress("139.129.53.180", 27017);
			mg = new Mongo(serverAddress);
			if(mg != null) {
				mg.close();
				mg = null; 
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    } 

	/**
	 * getCache:(获取缓存对象).
	 */

	public DBCollection getCache(String name) {
		return this.db.getCollection(name);
	}

	/**
	 * put:(在指定缓存对象中加入需要缓存的对象).
	 * 
	 */

	public void put(String cacheName, String key, Object value) {

		DBCollection cache = this.db.getCollection(cacheName);
		// String obj2Json = JSONUtils.toJSONString(value);
		// BasicDBObject obj = new BasicDBObject();
		// obj.put(MongodbManagerUtil.KEY,key);
		// obj.put(MongodbManagerUtil.VALUE, obj2Json);
		// BasicDBObject basicDBObject = new
		// BasicDBObject(MongodbManagerUtil.KEY,key);

		BasicDBObject obj = new BasicDBObject();
		obj.put(key, value);
		BasicDBObject basicDBObject = new BasicDBObject(key, value);
		int size = cache.find(basicDBObject).count();

		if (size == 0) {
			cache.save(obj);
		} else {
			cache.update(basicDBObject, obj);
		}

	}

	/**
	 * get:(根据key从指定缓存对象中获取对象).
	 * 
	 * @author sid
	 * 
	 * 
	 @param cacheName
	 * 
	 * 
	 @param key
	 * 
	 * 
	 @return
	 */

	public <T> T get(String cacheName, String key, Class<T> classOfT) {

		DBCollection cache = this.db.getCollection(cacheName);

		List<DBObject> array = cache.find(
				new BasicDBObject(MongodbManagerUtil.KEY, key)).toArray();

		if (array == null || array.size() == 0) {
			return null;
		}

		DBObject dbObject = array.get(0);
		String json = (String) dbObject.get(MongodbManagerUtil.VALUE);

		return JSONUtils.toBean(json, classOfT);
	}

	/**
	 * remove:(从指定缓存对象中清除对象).
	 * 
	 * 
	 @param cacheName
	 * 
	 * 
	 @param key
	 */

	public void remove(String cacheName, String key) {

		DBCollection cache = this.db.getCollection(cacheName);
		cache.remove(new BasicDBObject(MongodbManagerUtil.KEY, key));

	}

	/**
	 * 
	 getKeys:(获取keys列表).
	 * 
	 * @author sid
	 * 
	 * 
	 @param cacheName
	 * 
	 * 
	 @return
	 */

	public List<String> getKeys(String cacheName) {

		List<String> list = new ArrayList<String>();

		DBCollection cache = this.db.getCollection(cacheName);

		DBCursor find = cache.find();

		while (find.hasNext()) {

			DBObject next = find.next();
			String key = (String) next.get(MongodbManagerUtil.KEY);
			list.add(key);
		}
		return list;

	}

	/**
	 * 
	 containsKey:(判断消息是否存在).
	 * 
	 * @author sid
	 * 
	 * 
	 @param cacheName
	 * 
	 * 
	 @param key
	 * 
	 * 
	 @return
	 */

	public Boolean containsKey(String cacheName, String key) {

		DBCollection cache = this.db.getCollection(cacheName);

		BasicDBObject basicDBObject = new BasicDBObject(MongodbManagerUtil.KEY,
				key);

		int size = cache.find(basicDBObject).count();

		if (size == 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 把实体bean对象转换成DBObject
	 * 
	 * @param bean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public <T> DBObject beanToDBObject(T bean)
			throws IllegalArgumentException, IllegalAccessException {
		if (bean == null) {
			return null;
		}
		DBObject dbObject = new BasicDBObject();
		// 获取对象对应类中的所有属性域
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			// 获取属性名
			String varName = field.getName();
			// 修改访问控制权限
			boolean accessFlag = field.isAccessible();
			if (!accessFlag) {
				field.setAccessible(true);
			}
			Object param = field.get(bean);
			if (param == null) {
				continue;
			} else if (param instanceof Integer) {// 判断变量的类型
				int value = ((Integer) param).intValue();
				dbObject.put(varName, value);
			} else if (param instanceof String) {
				String value = (String) param;
				dbObject.put(varName, value);
			} else if (param instanceof Double) {
				double value = ((Double) param).doubleValue();
				dbObject.put(varName, value);
			} else if (param instanceof Float) {
				float value = ((Float) param).floatValue();
				dbObject.put(varName, value);
			} else if (param instanceof Long) {
				long value = ((Long) param).longValue();
				dbObject.put(varName, value);
			} else if (param instanceof Boolean) {
				boolean value = ((Boolean) param).booleanValue();
				dbObject.put(varName, value);
			} else if (param instanceof Date) {
				Date value = (Date) param;
				dbObject.put(varName, value);
			}
			// 恢复访问控制权限
			field.setAccessible(accessFlag);
		}
		return dbObject;
	}

	/**
	 * 把DBObject转换成bean对象
	 * 
	 * @param dbObject
	 * @param bean
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public <T> T dbObjectToBean(DBObject dbObject, T bean)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		if (bean == null) {
			return null;
		}
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			String varName = field.getName();
			Object object = dbObject.get(varName);
			if (object != null) {
				BeanUtils.setProperty(bean, varName, object);
			}
		}
		return bean;
	}

	/**
	 * mongodb保存Bean对象方法
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param bean
	 *            Bean对象
	 * @return 数据在monggodb数据库中的ObjectId,如果保存失败返回不成功标识：unsuccess
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public <T> String saveBean(String collectionName, T bean)
			throws IllegalArgumentException, IllegalAccessException {
		BasicDBObject dbBean = (BasicDBObject) beanToDBObject(bean);
		DBCollection collection = this.db.getCollection(collectionName);
		int size = collection.find(dbBean).count();
		if (size == 0) {
			collection.save(dbBean);
			return dbBean.get("_id").toString();
		} else {
			return "unsuccess";
		}
	}

	/**
	 * 根据条件删除数据——数据真删除慎用
	 * 
	 * @param collectionName
	 *            集合对象名称
	 * @param object
	 *            搜索条件
	 * @return 删除数据条数
	 */
	public int deleteObject(String collectionName, DBObject object) {
		DBCollection collection = this.db.getCollection(collectionName);
		WriteResult result = collection.remove(object);
		return result.getN();
	}
	
	/**
     * 更新数据
     * @param collectionName 集合集合名称
     * @param oldObject 原始数据
     * @param nowObject 最新数据
     * @return 更新条数
     */
    public int updateObject(String collectionName, DBObject oldObject, DBObject nowObject) {
    	DBCollection collection = this.db.getCollection(collectionName);
    	WriteResult result = collection.update(oldObject, nowObject);
    	return result.getN();
    }
    
	/**
	 * 查询搜索数据
	 * 
	 * @param collectionName
	 *            集合对象名称
	 * @param object
	 *            搜索条件
	 * @return 符合条件的数据
	 */
	public List<DBObject> seachBean(String collectionName, DBObject object) {
		DBCollection collection = this.db.getCollection(collectionName);
		List<DBObject> array = collection.find(object).toArray();
		return array;
	}

	/**
	 * 查询搜索数据
	 * 
	 * @param collectionName
	 *            集合对象名称
	 * @param object
	 *            搜索条件
	 * @param orderBy
	 *            排序条件
	 * @return 符合条件的数据
	 */
	public List<DBObject> seachBean(String collectionName, DBObject object,
			DBObject orderBy) {
		DBCollection collection = this.db.getCollection(collectionName);
		DBCursor cursor = collection.find(object).sort(orderBy);
		List<DBObject> array = cursor.toArray();
		return array;
	}
	
	/**
	 * 根据搜索条件查询符合条件的数据数量
	 * @param collectionName 集合对象名称
	 * @param object 搜索条件
	 * @return 符合搜索条件的数据数量
	 */
	public int getDBObjectCount(String collectionName, DBObject object) {
		DBCollection collection = this.db.getCollection(collectionName);
		int objectCount = collection.find(object).count();
		return objectCount;
	}

	/**
	 * 查询搜索数据分页显示
	 * 
	 * @param collectionName
	 *            集合对象名称
	 * @param object
	 *            搜索条件
	 * @param pageNum
	 *            页码 从1开始
	 * @param pageSize
	 *            条数
	 * @param orderBy
	 *            排序条件
	 * @return 符合条件的数据
	 */
	public List<DBObject> seachBean(String collectionName, DBObject object,
			int pageNum, int pageSize, DBObject orderBy) {
		DBCollection collection = this.db.getCollection(collectionName);
		if (pageNum > 0) {
			int skip = (pageNum - 1) * pageSize;
			DBCursor cursor = collection.find(object).skip(skip)
					.limit(pageSize).sort(orderBy);
			List<DBObject> array = cursor.toArray();
			return array;
		} else {
			return null;
		}
	}

	/**
	 * 设置搜索条件——完全匹配
	 * 
	 * @param value
	 *            匹配条件
	 * @return
	 */
	public Pattern setSeach_exactMatch(String value) {
		Pattern seachPattern = Pattern.compile("^" + value + "$",
				Pattern.CASE_INSENSITIVE);
		return seachPattern;
	}

	/**
	 * 设置搜索条件——左匹配
	 * 
	 * @param value
	 *            匹配条件
	 * @return
	 */
	public Pattern setSeach_leftMatch(String value) {
		Pattern seachPattern = Pattern.compile("^" + value + ".*$",
				Pattern.CASE_INSENSITIVE);
		return seachPattern;
	}

	/**
	 * 设置搜索条件——右匹配
	 * 
	 * @param value
	 *            匹配条件
	 * @return
	 */
	public Pattern setSeach_rightMatch(String value) {
		Pattern seachPattern = Pattern.compile("^.*" + value + "$",
				Pattern.CASE_INSENSITIVE);
		return seachPattern;
	}

	/**
	 * 设置搜索条件——模糊匹配
	 * 
	 * @param value
	 *            匹配条件
	 * @return
	 */
	public Pattern setSeach_fuzzyMatch(String value) {
		Pattern seachPattern = Pattern.compile("^.*" + value + ".*$",
				Pattern.CASE_INSENSITIVE);
		return seachPattern;
	}

	/**
	 * 设置搜索条件——大于
	 * 
	 * @param value
	 *            匹配条件
	 * @return
	 */
	public BasicDBObject setSeach_greater(String value) {
		BasicDBObject seachObject = new BasicDBObject("$gt", value);
		return seachObject;
	}

	/**
	 * 设置搜索条件——小于
	 * 
	 * @param value
	 *            匹配条件
	 * @return
	 */
	public BasicDBObject setSeach_less(String value) {
		BasicDBObject seachObject = new BasicDBObject("$lt", value);
		return seachObject;
	}

	/**
	 * 设置搜索条件——大于等于
	 * 
	 * @param value
	 *            匹配条件
	 * @return
	 */
	public BasicDBObject setSeach_greaterAndEqual(String value) {
		BasicDBObject seachObject = new BasicDBObject("$gte", value);
		return seachObject;
	}

	/**
	 * 设置搜索条件——小于等于
	 * 
	 * @param value
	 *            匹配条件
	 * @return
	 */
	public BasicDBObject setSeach_lessAndEqual(String value) {
		BasicDBObject seachObject = new BasicDBObject("$lte", value);
		return seachObject;
	}

	/**
	 * 设置一个排序条件
	 * 
	 * @param key
	 *            排序Key
	 * @param rank
	 *            排序方式 1 为升序排列 -1为降序排列
	 * @return
	 */
	public BasicDBObject setRank(String key, int rank) {
		BasicDBObject rankObject = new BasicDBObject(key, rank);
		return rankObject;
	}
	
	/**
	 * 设置or条件查询
	 * @param map or条件的键值对map
	 * @return
	 */
	public BasicDBObject setOr(Map< String, String> map) {
		BasicDBObject orObject = new BasicDBObject();
		BasicDBList orList = new BasicDBList();
		Iterator entries = map.entrySet().iterator();  
		while (entries.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) entries.next();
			String key = entry.getKey();
			String value = entry.getValue();
			orList.add(new BasicDBObject(key,value));
		}
		orObject.put("$or", orList);
		return orObject;
	}
	
	/**
	 * 设置in条件查询
	 * @param key 查询条件
	 * @param list in的数据集合
	 * @return
	 */
	public BasicDBObject setIn(String key, List list) {
		BasicDBObject inObject = new BasicDBObject();
		BasicDBList inList = new BasicDBList();
		for (int i = 0; i < list.size(); i++) {
			inList.add(list.get(i));
		}
		inObject.put("$in", inList);
		return inObject;
	}
	
	/**
	 * 根据集合名称获取，改集合的最大编号
	 * @param collectionName
	 * @return
	 */
	public int getSequence(String collectionName){
	        DBCollection colletion = this.db.getCollection("sequence");
	        DBObject query = new BasicDBObject();
	        query.put("collectionName", collectionName);
	        DBObject newDocument =new BasicDBObject();
	        newDocument.put("$inc", new BasicDBObject().append("cnt", 1));
	        DBObject ret = colletion.findAndModify(query, newDocument);
	        if (ret == null){
	        	return 0;
	        }else{
	        	return (Integer)ret.get("cnt") + 1;
	        }
	    }

}