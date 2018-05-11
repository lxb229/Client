package com.guse.stock.netty.handler;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

import com.guse.stock.netty.handler.service.UserInfo;

/** 
* @ClassName: ISession 
* @Description: 用户session信息
* @author Fily GUSE
* @date 2017年9月4日 上午11:09:50 
*  
*/
public class ISession {
	
	// 连接通道集合
	private static Map<Channel, UserInfo> CHANNEL_SESSION = new HashMap<Channel, UserInfo>();
	
	/** 
	* @Title: addChannel 
	* @Description: 添加连接通道 
	* @param @param channel
	* @return void 
	* @throws 
	*/
	public static void addChannel(Channel channel) {
		
		UserInfo user = CHANNEL_SESSION.get(channel);
		if(user == null) {
			CHANNEL_SESSION.put(channel, null);
		}
	}
	
	/** 
	* @Title: getUserInfo 
	* @Description: 根据连接信息获取登录用户信息 
	* @param @param channel
	* @param @return
	* @return UserInfo 
	* @throws 
	*/
	public static UserInfo getUserInfo(Channel channel) {
		
		return CHANNEL_SESSION.get(channel);
	}
	
	/** 
	* @Title: updateUser 
	* @Description: 跟新连接通道信息 
	* @param @param channel
	* @param @param user
	* @return void 
	* @throws 
	*/
	public static void updateUser(Channel channel, UserInfo user) {
		CHANNEL_SESSION.put(channel, user);
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
	* @Title: getChannelByuid 
	* @Description: 根据userID获取连接信息 
	* @param @param userId
	* @param @return
	* @return Channel 
	* @throws 
	*/
	public static Channel getChannelByuid(Long userId) {
		Channel channel = null;
		for(Map.Entry<Channel, UserInfo> entry : CHANNEL_SESSION.entrySet()) {
			UserInfo user = entry.getValue();
			if(user != null && user.getUid() != null && userId.equals(user.getUid())) {
				channel = entry.getKey();
				break;
			}
		}
		return channel;
	}
	
	
	/** 
	* @Title: getChannelByDevice 
	* @Description: 根据设备号获取用户信息 
	* @param @param device_number
	* @param @return
	* @return Channel 
	* @throws 
	*/
	public static UserInfo getUserInfoByDevice(String device_number) {
		for(Map.Entry<Channel, UserInfo> entry : CHANNEL_SESSION.entrySet()) {
			UserInfo user = entry.getValue();
			if(user != null && device_number.equals(user.device_number)) {
				return user;
			}
		}
		return null;
	}

}
