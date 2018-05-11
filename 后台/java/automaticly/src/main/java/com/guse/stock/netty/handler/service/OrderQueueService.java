package com.guse.stock.netty.handler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
* @ClassName: OrderQueueService 
* @Description: 订单队列服务
* @author Fily GUSE
* @date 2017年10月24日 下午2:24:41 
*  
*/
public class OrderQueueService {
	
	// 排队信息
	private static Map<Long, List<HttpHandlerService>> QUEUE_MAP = new HashMap<Long, List<HttpHandlerService>>();
	// 添加到队列中
	public static void addQueue(long uid, HttpHandlerService service) {
		List<HttpHandlerService> list =  QUEUE_MAP.get(uid);
		if(list == null) {
			list = new ArrayList<HttpHandlerService>();
		}
		list.add(service);
		QUEUE_MAP.put(uid, list);
	}
	// 从队列中移除
	public static void removeQueue(long uid, HttpHandlerService service) {
		List<HttpHandlerService> list =  QUEUE_MAP.get(uid);
		if(list != null && !list.isEmpty()) {
			list.remove(service);
		}
	}
	// 获取当前排队位置
	public static int indexQueue(long uid, HttpHandlerService service) {
		List<HttpHandlerService> list =  QUEUE_MAP.get(uid);
		return list.indexOf(service);
	}
	
}
