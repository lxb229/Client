package com.guse.apple_reverse.service.query;

import io.netty.channel.Channel;

import java.util.Map;

import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.netty.handler.session.ClientSession;
import com.guse.apple_reverse.netty.handler.session.ClientSessionUtil;
import com.guse.apple_reverse.netty.handler.session.Message;

/**
 * @ClassName: QueryService
 * @Description: 查询服务
 * @author Fily GUSE
 * @date 2017年11月13日 上午11:07:12
 * 
 */
public class QueryService implements Runnable {
	
	private Message msg;
	// 构造方法
	public QueryService(Message msg) {
		this.msg = msg;
	}
	
	/** 
	* @Title: run 
	* @Description: 方法执行 
	* @param 
	* @return void 
	* @throws 
	*/
	public void run() {
		// 获取一条查询路线
		Map<String, Object> json = ClientSessionUtil.getQueryChannel(msg.getType());
		// 设置查询连接
		Channel channel = null;
		if(json != null) {
			channel = (Channel) json.get("channel");
			msg.setType((int)json.get("query_type"));
		}
		
		if(channel != null) {
			ClientSession client = (ClientSession) json.get("client");
			ServiceStart.INFO_LOG.info("start exector query. query:{}, data:{}"
				, client.findQueryTypeName(msg.getType()), msg.getData());
			Message.sendMessage(channel, msg);
		}
	}
	
}
