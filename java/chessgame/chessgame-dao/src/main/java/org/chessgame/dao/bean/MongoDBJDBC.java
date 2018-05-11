package org.chessgame.dao.bean;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.management.Query;

import org.bson.types.ObjectId;
import org.chessgame.dao.UsersDao;
import org.chessgame.dao.vo.UserVo;

import com.guse.chessgame.commonm.mongodb.MongodbManagerUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;


public class MongoDBJDBC{
	

public static void main( String args[] ){
      try{
//    	  UsersDao usersDao = new UsersDao();
//    	  Users user = usersDao.seachUser("598d42a2c4cb1612e38a5955");
//    	  user.setSex(0);
//    	  user.setOnline_state(1);
//    	  System.out.println(usersDao.updateUser(user));
//    	  System.out.println(usersDao.deleteUser(user));
//    	  usersDao.closeDao();
    	  
    	  
	             MongodbManagerUtil util = MongodbManagerUtil.getSingletonInstance();
	             
	             BasicDBList orList = new BasicDBList();
	             BasicDBObject orObject = new BasicDBObject();
	             
//	             Sequence sequence = new Sequence();
//	             sequence.setCollectionName("users");
//	             sequence.setCnt(5);
//	             util.saveBean("sequence", sequence);
	             
	             System.out.println(util.getSequence("users"));
	             
//	             orList.add(new BasicDBObject("create_ip", "192.168.10.202"));
//	             orList.add(new BasicDBObject("create_ip", "192.168.10.222"));
//	             orObject.put("$or", orList);
//	             orList.add("192.168.10.202");
//	             orList.add("192.168.10.222");
//	             orObject.put("create_ip",new BasicDBObject("$in", orList));
//	             
//	             List<DBObject> userList = util.seachBean("users", orObject);
//	             for (int i = 0; i < userList.size(); i++) {
//	            	 Users user = new Users();
//	            	 user = util.dbObjectToBean(userList.get(i), user);
//	            	 System.out.println(user.getUid()+"  "+user.getNickname()+"  "+user.getCreate_ip()+"  "+user.getStatus());
//					
//	             }
	             
//	             Users user = new  Users();
//	             user.setUid("598d42a2c4cb1612e38a5955");
//	             user.setUtype(2);
//	             user.setNickname("游客00000001");
//	             user.setHead_portrait("");
//	             user.setSex(1);
//	             user.setOnline_state(0);
//	             user.setUser_type(1);
//	             user.setReal_name("");
//	             user.setId_card_number("");
//	             user.setStatus(1);
//	             user.setCreate_ip("127.0.0.1");
//	             user.setCreate_time(new Date());
//	             user.setRecommend_id("");
//	             util.saveBean("users", user);
//	             System.out.println("文档插入成功");
//	             
//	             BasicDBObject delObject = new BasicDBObject("_id",new ObjectId("5993ed3a1a8f2a4d1a3b5694"));
//	             List<DBObject> delList = util.seachBean("users", delObject);
//	             for (int i = 0; i < delList.size(); i++) {
//					DBObject obj = delList.get(i);
//					System.out.println(obj.get("_id").toString());
//					
//				}
//	             System.out.println(delList.size());
//	             int delNum = util.deleteObject("users", delObject);
//	             System.out.println(delNum);
	             
//	             BasicDBObject seachObject = new BasicDBObject("uid","598d42a2c4cb1612e38a5955");
//	             Users user = new Users();
//	             BasicDBObject seachObject = new BasicDBObject();
//	             List<DBObject> userList = util.seachBean("users", seachObject);
//	             System.out.println(userList.size());
//	             for (int i = 0; i < userList.size(); i++) {
//	            	 Users dbUser = util.dbObjectToBean(userList.get(i), user);
//	            	 System.out.println(dbUser.getNickname()+"  "+dbUser.getSex()+"   "+dbUser.getHead_portrait()+"   "+
//	            	 dbUser.getCreate_ip()+"   "+dbUser.getId_card_number()+"  "+dbUser.getCreate_time()+"   "+dbUser.getRecommend_id());
//	             }
	             
//	             util.put("chessTest", "userTest", user);
//	             Users user_2 = new  Users();
//	             user_2.setCode("000002");
//	             user_2.setSex(0);
//	             user_2.setNickame("游客000002");
//	             user_2.setWechat_id("xxxd87367342");
//	             BasicDBObject jo =  (BasicDBObject) util.beanToDBObject(user_2);
//	             collection.save(jo);
//	             System.out.println("文档插入成功"); 
	             
//	             util.put("chessTest", "code", "000007");
//	             System.out.println("文档插入成功"); 
	             
	           //完全匹配
	             //Pattern pattern = Pattern.compile("^name$", Pattern.CASE_INSENSITIVE);
	             //右匹配
	             //Pattern pattern = Pattern.compile("^.*name$", Pattern.CASE_INSENSITIVE);
	             //左匹配
	             //Pattern pattern = Pattern.compile("^name.*$", Pattern.CASE_INSENSITIVE);
	             //模糊匹配
//	             Pattern pattern = Pattern.compile("^.*00.*$", Pattern.CASE_INSENSITIVE);
//	             BasicDBObject paDBObject = new BasicDBObject("code",pattern);
//	             paDBObject.put("sex", new BasicDBObject("$lt", 1));
////	             paDBObject.put("sex", new BasicDBObject("$gte", 1));
//	             util.seachBean("chessTest", paDBObject);
//	             
//	             Users user_2 = new Users();
//	             List<DBObject> userList = util.seachBean("chessTest", paDBObject);
//	             System.out.println(userList.size());
//	             for (int i = 0; i < userList.size(); i++) {
//	            	 Users dbUser = util.dbObjectToBean(userList.get(i), user_2);
//	            	 System.out.println(dbUser.getCode()+"  "+dbUser.getSex()+"   "+dbUser.getNickame()+"   "+dbUser.getWechat_id());
//	             }
//	             
////	             List<DBObject> array = collection.find(new BasicDBObject("code","000002")).toArray();
//	             List<DBObject> array = collection.find(paDBObject).toArray();
//	             System.out.println(array.size());
//	             for (int i = 0; i < array.size(); i++) { 
//	            	Users dbUser = new Users();
//					dbUser =  util.dbObjectToBean(array.get(i), dbUser);
//					
//					System.out.println(dbUser.getCode()+"  "+dbUser.getSex()+"   "+dbUser.getNickame()+"   "+dbUser.getWechat_id());
//				}
	             
//    	  }
    	  
    	  
    	  //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址  
          //ServerAddress()两个参数分别为 服务器地址 和 端口  
//          ServerAddress serverAddress = new ServerAddress("139.129.53.180",27017);  
//          List<ServerAddress> addrs = new ArrayList<ServerAddress>();  
//          addrs.add(serverAddress);  
//            
//          //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码  
//          MongoCredential credential = MongoCredential.createScramSha1Credential("php-dev", "db_php_dmh", "guse2017".toCharArray());  
//          List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
//          credentials.add(credential);  
//            
//          //通过连接认证获取MongoDB连接  
//          MongoClient mongoClient = new MongoClient(addrs,credentials);  
//            
//          //连接到数据库  
//          MongoDatabase mongoDatabase = mongoClient.getDatabase("db_php_dmh");  
//          System.out.println("Connect to database successfully");   
//          System.out.println("MongoDatabase inof is : "+mongoDatabase.getName());
//          
////    	  mongoDatabase.createCollection("chrsstest");
////    	  System.out.println("集合创建成功");
////    	  
//    	  
//    	  
//    	  MongoCollection<Document> collection = mongoDatabase.getCollection("inventory");
//          
//
//          System.out.println("Collection created successfully");
//
//          System.out.println("当前数据库中的所有集合是：");
//
//          for (String name : mongoDatabase.listCollectionNames()) {
//              System.out.println(name);
//          }
//        
//          //插入文档  
//          /** 
//          * 1. 创建文档 org.bson.Document 参数为key-value的格式 
//          * 2. 创建文档集合List<Document> 
//          * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document) 
//          * */
//          Document document = new Document("title", "MongoDB").  
//          append("description", "database").  
//          append("likes", 100).  
//          append("by", "Fly");  
//          List<Document> documents = new ArrayList<Document>();  
//          documents.add(document);  
//          collection.insertMany(documents);  
//          System.out.println("文档插入成功");  
//           
          //检索所有文档  
//          /** 
//          * 1. 获取迭代器FindIterable<Document> 
//          * 2. 获取游标MongoCursor<Document> 
//          * 3. 通过游标遍历检索出的文档集合 
//          * */  
//          FindIterable<Document> findIterable = collection.find();  
//          MongoCursor<Document> mongoCursor = findIterable.iterator();  
//          while(mongoCursor.hasNext()){  
//             System.out.println(mongoCursor.next());  
//          }
//          
//          //更新文档   将文档中likes=100的文档修改为likes=200   
//          collection.updateMany(Filters.eq("likes", 100), new Document("$set",new Document("likes",200)));  
//          //检索查看结果  
//          FindIterable<Document> updatetable = collection.find();  
//          MongoCursor<Document> updateMongoCursor = updatetable.iterator();  
//          while(updateMongoCursor.hasNext()){  
//             System.out.println(updateMongoCursor.next());  
//          } 
//          
//          
//          //删除符合条件的第一个文档  
//          collection.deleteOne(Filters.eq("likes", 200));  
//          //删除所有符合条件的文档  
//          collection.deleteMany (Filters.eq("likes", 200));  
//          //检索查看结果  
//          FindIterable<Document> deleteIterable = collection.find();  
//          MongoCursor<Document> deletemongoCursor = deleteIterable.iterator();  
//          while(deletemongoCursor.hasNext()){  
//            System.out.println(deletemongoCursor.next());  
//          }  
          
      }catch(Exception e){
    	  e.printStackTrace();
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
     }
   }
}