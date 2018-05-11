package com.guse.stock.dao;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import com.guse.stock.dao.model.StockCer;
import com.guse.stock.mongo.MongodbManagerUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 凭证dao——操作mongodb数据库
 * @author 不能
 *
 */
public class StockCerDao {
	
	MongodbManagerUtil mongoUtil = MongodbManagerUtil.getSingletonInstance();
	
	/**
	 * 销毁数据库连接
	 */
	public void closeDao() {
		mongoUtil.destroyDBLink();
	}
	
	/**
	 * 保存凭证对象
	 * @param stockCer 凭证对象对象
	 * @return 保存成功后对象的ObjectId
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public String savePlayer(String tableName, StockCer stockCer) throws IllegalArgumentException, IllegalAccessException {
		return mongoUtil.saveBean(tableName, stockCer);
	}
	
	/**
	 * 根据库存id查询凭证
	 * @param stockId 库存id
	 * @return 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public StockCer seachStockCer(String colletionName,Long stockId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// 设置凭证查询条件，符合库存id并且状态为未使用
		BasicDBObject dbObj = new BasicDBObject("stock_id",stockId);
		dbObj.put("status", 0);
		// 设置排序规则，根据创建时间倒序
		BasicDBObject orderObject = new BasicDBObject("create_time",-1);
		// 只查询一条查询符合条件的凭证
		List<DBObject> stockCerList = mongoUtil.seachBean(colletionName, dbObj, 1, 1, orderObject);
		if(stockCerList.size() > 0) {
			StockCer stockCer = new StockCer();
			return mongoUtil.dbObjectToBean(stockCerList.get(0), stockCer);
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) throws ParseException {
		MongodbManagerUtil mongoUtil = MongodbManagerUtil.getSingletonInstance();
//		BasicDBObject dbObject = new BasicDBObject("$ne",null);
		BasicDBObject dbObject = new BasicDBObject("$in",null);
		BasicDBObject dbObj = new BasicDBObject("ios_6",dbObject);
		dbObj.put("status", 0);
		// 设置排序规则，根据创建时间倒序
		BasicDBObject orderObject = new BasicDBObject("create_time",-1);
		// 只查询一条查询符合条件的凭证
		List<DBObject> stockCerList = mongoUtil.seachBean("pl_stock_cer1", dbObj, 1, 1, orderObject);
		if(stockCerList.size() > 0) {
			StockCer stockCer = new StockCer();
			try {
				stockCer = mongoUtil.dbObjectToBean(stockCerList.get(0), stockCer);
				System.out.println(stockCer.getStock_id()+"  "+stockCer.getIos_6()+"  "+stockCer.getIos_7());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		ConfigReader reader = new ConfigReader();
//		String gameIds = reader.read("gameIds");
//		System.out.println(gameIds);
//		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//	    Long time= 1480801009225l;  
//	    String d = format.format(time);  
//	    System.out.println("1480801009225 日期:"+d); 
//	    time = 1480801227454l;
//	    d = format.format(time);  
//	    System.out.println("1480801227454 日期:"+d);
//	    time = 1480801227454l;
//	    d = format.format(time);  
//	    System.out.println("1480801227454 日期:"+d);
//	    time = 1480801392451l;
//	    d = format.format(time);  
//	    System.out.println("1480801392451 日期:"+d);
//	    time = 1480801445214l;
//	    d = format.format(time);  
//	    System.out.println("1480801445214 日期:"+d);
//	    time = 1480801560565l;
//	    d = format.format(time);  
//	    System.out.println("1480801560565 日期:"+d);
//	    time = 1480801921889l;
//	    d = format.format(time);  
//	    System.out.println("1480801921889 日期:"+d);
//	    
	}

}
