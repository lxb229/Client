package com.guse.platform.service.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.platform.common.utils.JedisUtils;
import com.guse.platform.dao.doudou.OperationBaseUserMapper;
import com.guse.platform.entity.doudou.OperationBaseUser;
import com.guse.platform.service.task.thread.BaseUserThread;
import com.guse.platform.utils.ThreadPoolUtil;
import com.guse.platform.utils.date.DateUtils;
import com.guse.platform.utils.redis.RedisKeys;
import com.guse.platform.utils.redis.RedisMapToBean;


/**
 * 基础数据入库
 * @author liyang
 *
 */
@Component
public class BaseUserTask {
	private static final Logger logger = LoggerFactory.getLogger(BaseUserTask.class);
	public ExecutorService                   threadPool;
	public Float costTime = 0f;
	public static final int PAGE_SIZE    = 10;
	public static final int THREAD_NUM    = 10;
	private static final int dbIndex = 3;
	@Autowired
	private JedisUtils          jedisUtils;
	@Autowired
	private RedisMapToBean rm;
	@Autowired
	private OperationBaseUserMapper mapper;
	
	
	/**
	 * 存储用户基础数据
	 */
	public void save(){
		logger.info("BaseUserTask.save()");
		insertData();
	}
	
	public void insertData(){
		logger.info("BaseUserTask.insertData()");
		//开始时间
		long startDate = System.currentTimeMillis();
		logger.info("任务开始时间:{}",DateUtils.DateToStr(new Date(), DateUtils.sdf));
		//待存储的数据
		List<OperationBaseUser> list = getOperationBaseUsers();
		if(CollectionUtils.isEmpty(list)){
			logger.info("redis中没有用户基础数据");
			return;
		}
		
		 //长度
		 int length = list.size();
		
		 if(length<PAGE_SIZE){
			 //直接执行
			 insert(list);
		 }else{
			  //创建线程池
			  threadPool = ThreadPoolUtil.createThreadPool(THREAD_NUM, "同步基础用户数据", true);
			  int pages = (int) Math.ceil(length / (Float.parseFloat(String.valueOf(PAGE_SIZE))));
			  for (int i = 0; i < pages; i++) {
			      int startIndex =i*PAGE_SIZE;
			      int toIndex =( i+1)*PAGE_SIZE;
			      if(toIndex>=length)toIndex=length;
			      List<OperationBaseUser> userList = list.subList(startIndex, toIndex);
			      //创建线程
				  BaseUserThread thread = new BaseUserThread(userList, this);
				  //执行
				  threadPool.execute(thread);
			  }
	      	}
		 	//关闭线程池
		 	threadPool.shutdown();
			//阻塞线程
			try {
		      boolean loop = true;
		      do {
		        // 阻塞，直到线程池里所有任务结束
		        loop = !threadPool.awaitTermination(10, TimeUnit.SECONDS);
		      } while (loop);
		      //结束时间
		      Long endDate = System.currentTimeMillis();
		      logger.info("任务结束时间:{}",DateUtils.DateToStr(new Date(), DateUtils.sdf));
		      this.costTime = (endDate - startDate) / 1000f / 60;
		      logger.info("User...........Task[" + this.getClass().getName() + ".insertData] end run at[{}]",
		          DateUtils.DateToStr(new Date(), DateUtils.sdf) + " \nCost: " + costTime + "分钟");
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		      logger.error(e.getMessage(),e);
		    }
	}
	
	
	/**
	 * 入库的list
	 * @param list
	 */
	public void insert(List<OperationBaseUser> list){
		//用户集合
		List<Long> userIds = new ArrayList<Long>();
		for (OperationBaseUser user : list) {
			userIds.add(user.getObuUserid());
		}
		//查询参数
		//先查询date天的统计数据
		List<OperationBaseUser> dateData = mapper.selectUsersByUserIds(userIds);
		
		//如果不存在执行插入
		if(CollectionUtils.isEmpty(dateData)){
			//插入数据库
			Object result = mapper.batchInsert(list);
			logger.info("数据插入结果:{},条数:{}",result,list.size());
		}else{
			//需要插入的数据
			List<OperationBaseUser> needInsert = new ArrayList<OperationBaseUser>();
			//需要更新的数据
			List<OperationBaseUser> needUpdate = new ArrayList<OperationBaseUser>();
			Map<Long, OperationBaseUser> hadMap = gethadData(dateData);
			for (OperationBaseUser u : list) {
				Long userId = u.getObuUserid();
				//获取已有数据对象
				OperationBaseUser hadData = hadMap.get(userId);
				if(hadMap.containsKey(userId)){
					//执行更新
					u.setObuUserid(hadData.getObuUserid());
					needUpdate.add(u);
				}else{
					//执行插入
					needInsert.add(u);
				}
			}
			//执行插入
			if(CollectionUtils.isNotEmpty(needInsert)){
				//插入数据库
				Object result = mapper.batchInsert(needInsert);
				logger.info("数据插入结果:{},条数:{}",result,needInsert.size());
			}
			//执行更新
			if(CollectionUtils.isNotEmpty(needUpdate)){
				//更新数据
				Object result = mapper.batchUpdate(needUpdate);
				logger.info("更新结果:{},条数:{}",result,needUpdate.size());
			}
		}
	}
	
	//获取所有的用户
	public List<OperationBaseUser> getOperationBaseUsers() {
		List<OperationBaseUser> list = new ArrayList<>();
		//获取所有用户的key
		String userKeyKeys = RedisKeys.OPERATION_BASE_USER_PERMANENT;
		Set<String> smembers = jedisUtils.smembers(dbIndex, userKeyKeys);
		for (String key : smembers) {
			Map<String, String> map = jedisUtils.getmap(dbIndex, key);
			OperationBaseUser user = new OperationBaseUser();
			if(map!=null&&map.size()>0){
				rm.mapToPojo(map, user);
				list.add(user);
			}
		}
		return list;
	}
		
	//转换已存数据为map
	private Map<Long, OperationBaseUser> gethadData(List<OperationBaseUser> dateData){
		Map<Long, OperationBaseUser> map = new HashMap<Long, OperationBaseUser>();
		for (OperationBaseUser o : dateData) {
			map.put( o.getObuUserid(),  o);
		}
		return map;
	}

}
