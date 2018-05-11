package com.guse.apple_reverse.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.dao.model.AppleIdTable;
import com.guse.apple_reverse.netty.handler.session.Message;
import com.guse.apple_reverse.service.query.QueryService;

/** 
* @ClassName: ADTQueryService 
* @Description: 插件查询信息
* @author Fily GUSE
* @date 2017年12月4日 下午5:07:31 
*  
*/
public class ADTQueryService {
	
	private String appleId;
	private String applePwd;
	public ADTQueryService(String appleId, String applePwd) {
		this.appleId = appleId;
		this.applePwd = applePwd;
	}
	
	public synchronized String run() {
		ServiceStart.CONSOLE_LOG.info("start exector ADT query balance....");
		Date start = new Date();
		
		// 创建查询信息
		AppleIdTable apple = new AppleIdTable();
		apple.setId(-1);
		apple.setApple_id(appleId);
		apple.setApple_pwd(applePwd);
		
		// 初始化查询结果信息
		ADT_QUERY_RESULT.clear();
		ADT_QUERY_RESULT.put(apple.getId(), "waiting");
		
		// 创建查询信息
		Message msg = Message.createQuery(apple, Message.ADT_QUERY_BALANCE);
		// 开始查询
		new Thread(new QueryService(msg)).start();
			// 超时回调控制
		int count = 0;
		while("waiting".equals(ADT_QUERY_RESULT.get(apple.getId()))) {
			if(count > 33) {
				ADT_QUERY_RESULT.put(apple.getId(), "查询失败");
				break;
			}
			count ++;
			try {
				Thread.sleep(1000 * 5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ServiceStart.CONSOLE_LOG.info("exector ADT query finish. result:{} . elapsed time:{}",ADT_QUERY_RESULT.get(apple.getId()) , (new Date().getTime() - start.getTime()));
		return ADT_QUERY_RESULT.get(apple.getId());
	}
	
	// 查询结果
	private static Map<Integer, String> ADT_QUERY_RESULT = new HashMap<Integer, String>();
	public static void setQueryResult(int id, String result) {
		ADT_QUERY_RESULT.put(id, result);
	}
}
