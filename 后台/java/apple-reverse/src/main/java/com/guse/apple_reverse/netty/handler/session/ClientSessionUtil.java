package com.guse.apple_reverse.netty.handler.session;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.guse.apple_reverse.service.query.ClientComparator;

/** 
* @ClassName: ClientSessionUtil 
* @Description: 客服端管理
* @author Fily GUSE
* @date 2017年11月30日 下午4:16:15 
*  
*/
public class ClientSessionUtil {

	// 保存连接服务器
	private static Map<Channel, ClientSession> CHANNEL_MAP = new ConcurrentHashMap<Channel, ClientSession>();
	
	/** 
	* @Title: getQueryLine 
	* @Description: 获取一条查询线路 
	* @param @return
	* @return Channel 
	* @throws 
	*/
	public static synchronized Map<String, Object> getQueryChannel(int query_type) {
		int count = 0;
		do{
			if(!CHANNEL_MAP.isEmpty()) {
				// 可排序map
				Map<Channel, ClientSession> map = new HashMap<Channel, ClientSession>();
				map.putAll(CHANNEL_MAP);
				//这里将map.entrySet()转换成list
		        List<Map.Entry<Channel, ClientSession>> list = new ArrayList<Map.Entry<Channel, ClientSession>>(map.entrySet());
		        Collections.sort(list,new ClientComparator(query_type));
		        // 判断第一个有没有溢出
		        Map.Entry<Channel, ClientSession> mapping = list.get(0);
		        ClientSession client = mapping.getValue();
		        // 判断是否满足条件
		        if(client.getThreadNum() - client.getUseNum() > 0) {
		        	// 支持这种类型查询时
					if(client.checkedQueryType(query_type)) {
						client.setUseNum(client.getUseNum() + 1);
						Map<String, Object> json = new HashMap<String, Object>();
						json.put("channel", mapping.getKey());
						json.put("client", client);
						json.put("query_type", query_type);
						return json;
					}
					// 查详情时可以使用查询全部
					if(client.checkedQueryType(Message.SERVER_QUERY_ALL) && Message.SERVER_QUERY_APPLE==query_type) {
						client.setUseNum(client.getUseNum() + 1);
						Map<String, Object> json = new HashMap<String, Object>();
						json.put("channel", mapping.getKey());
						json.put("client", client);
						json.put("query_type", Message.SERVER_QUERY_ALL);
						return json;
					}
		        }
			}
			// 如果有没线路，等待5秒后继续查找线路
			count ++;
			try {
				Thread.sleep(1000 * 5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while(count < 40);
		return null;
	}
	
	/** 
	* @Title: entrySetIterator 
	* @Description: entrySet 遍历器 
	* @param @return
	* @return Iterator<Map.Entry<Channel,ClientSession>> 
	* @throws 
	*/
	public static Iterator<Map.Entry<Channel, ClientSession>> entrySetIterator() {
		return CHANNEL_MAP.entrySet().iterator();
	}
	
	/** 
	* @Title: removeClient 
	* @Description: 移除客服端 
	* @param @param channel
	* @return void 
	* @throws 
	*/
	public static void removeClient(Channel channel) {
		CHANNEL_MAP.remove(channel);
		try{
			channel.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 
	* @Title: addClient 
	* @Description: 添加客服端 
	* @param @param channel
	* @param @param client
	* @return void 
	* @throws 
	*/
	public static void addClient(Channel channel, ClientSession client) {
		// 判断客服端是否重复连接
		Iterator<Map.Entry<Channel, ClientSession>> it = entrySetIterator();
		while(it.hasNext()) {
			Map.Entry<Channel, ClientSession> entry = it.next();
			Channel c = entry.getKey();
			ClientSession cs = entry.getValue();
			if(client.getName().equals(cs.getName()) && !c.equals(channel)) {
				removeClient(c);
			}
		}
		CHANNEL_MAP.put(channel, client);
	}
	
	/** 
	* @Title: getClient 
	* @Description: 获取客服端信息 
	* @param @param channel
	* @param @return
	* @return ClientSession 
	* @throws 
	*/
	public static ClientSession getClient(Channel channel) {
		return CHANNEL_MAP.get(channel);
	}
}
