package com.guse.stock.mongo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.stock.dao.model.StockCer;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

/** 
* @ClassName: MongoStockCer 
* @Description: MongoDB凭证操作
* @author Fily GUSE
* @date 2017年10月10日 下午1:53:15 
*  
*/
@Component
public class MongoStockCer {
	private final static Logger logger = LoggerFactory.getLogger(MongoStockCer.class);
	
	
	/**
	 * 根据库存id查询凭证
	 * @param stockId 库存id
	 * @return 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public StockCer seachStockCer(String colletionName,Long stockId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// 创建连接MongoDB
		if(!createConnect()) {
			return null;
		}; 
		
		StockCer stockCer = null;
		// 设置凭证查询条件，符合库存id并且状态为未使用
		BasicDBObject dbObj = new BasicDBObject("stock_id",stockId);
//		dbObj.put("status", 0);
		// 设置排序规则，根据创建时间倒序
		BasicDBObject orderObject = new BasicDBObject("create_time",-1);
		// 只查询一条查询符合条件的凭证
		List<DBObject> stockCerList = seachBean(colletionName, dbObj, 1, 1, orderObject);
		if(stockCerList.size() > 0) {
			stockCer = new StockCer();
			dbObjectToBean(stockCerList.get(0), stockCer);
		}
		
		destroyDBLink(); // 销毁连接
		return stockCer;
	}
	
	
	/**
	 * ===========================================
	 * ===============MongoDB 数据库操作=============
	 * ===========================================
	 */
	@Autowired
	private MongoConnectUtil connectUtil;
	// 服务
	private ServerAddress serverAddress = null;
	private Mongo mg = null;
	private DB db = null;
	/** 
	* @Description: 创建连接 
	* @param 
	* @return void 
	* @throws 
	*/
	private boolean createConnect() {
		try {
			serverAddress = new ServerAddress(connectUtil.getHost(), connectUtil.getPort());
			mg = new Mongo(serverAddress);
			db = mg.getDB(connectUtil.getDbname());
			db.setReadPreference(ReadPreference.secondary());
			if (!db.authenticate(connectUtil.getUsername(), connectUtil.getPassword().toCharArray())) {
				logger.error("MongoDB Connect Failure. username or password error");
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("MongoDB Create Connect Failure.{}", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 查询搜索数据分页显示
	 * 
	 * @param collectionName 集合对象名称
	 * @param object 搜索条件
	 * @param pageNum 页码 从1开始
	 * @param pageSize 条数
	 * @param orderBy 排序条件
	 * @return 符合条件的数据
	 */
	private List<DBObject> seachBean(String collectionName, DBObject object,
			int pageNum, int pageSize, DBObject orderBy) {
		DBCollection collection = db.getCollection(collectionName);
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
	 * 销毁数据库连接
	 */
	private void destroyDBLink() {
		if (mg != null) {
			mg.close();
			mg = null;
		}
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
	private <T> T dbObjectToBean(DBObject dbObject, T bean)
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

}
