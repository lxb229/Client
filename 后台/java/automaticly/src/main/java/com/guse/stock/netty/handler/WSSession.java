package com.guse.stock.netty.handler;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/** 
* @ClassName: WSSession 
* @Description: websocket Session信息
* @author Fily GUSE
* @date 2017年10月18日 下午1:29:11 
*  
*/
public class WSSession {

	// 连接通道集合
	private static Map<Channel, Object> CHANNEL_SESSION = new HashMap<Channel, Object>();
	
	/** 
	* @Title: addChannel 
	* @Description: 添加连接信息 
	* @param @param channel
	* @param @param obj
	* @return void 
	* @throws 
	*/
	public static void addChannel(Channel channel, Object obj) {
		CHANNEL_SESSION.put(channel, obj);
	}
	
	/** 
	* @Title: delChannel 
	* @Description: 移除连接信息 
	* @param @param channel
	* @return void 
	* @throws 
	*/
	public static void delChannel(Channel channel) {
		CHANNEL_SESSION.remove(channel);
	}
	
	/** 
	* @Title: findByObj 
	* @Description: 根据值获取连接信息 
	* @param @param obj
	* @return void 
	* @throws 
	*/
	public static List<Channel> findByObj(Object obj) {
		List<Channel> list = new ArrayList<Channel>();
		for(Map.Entry<Channel, Object> entry : CHANNEL_SESSION.entrySet()) {
			Object temp = entry.getValue();
			if(temp != null && temp.toString().equals(obj.toString())) {
				list.add(entry.getKey());
			}
		}
		return list;
	}
	
	/** 
	* @Title: informClientByUid 
	* @Description: 通过uid通知客服端 
	* @param @param uid
	* @return void 
	* @throws 
	*/
	public static void informClientByUid(long uid, long orderId, int status, String status_coment) {
		List<Channel> list = findByObj(uid);
		for(Channel channel: list) {
			if(!channel.isActive()) {
				delChannel(channel);
				continue;
			}
			JSONObject json = new JSONObject();
			json.put("order_id", orderId);
			json.put("status", status);
			json.put("status_coment", status_coment);
			channel.writeAndFlush(new TextWebSocketFrame(json.toString()));
		}
	}
}
