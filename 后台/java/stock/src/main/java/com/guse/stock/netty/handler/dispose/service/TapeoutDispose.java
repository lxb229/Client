package com.guse.stock.netty.handler.dispose.service;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import net.sf.json.JSONObject;

import com.guse.stock.common.JSONUtils;
import com.guse.stock.netty.handler.ISession;
import com.guse.stock.netty.handler.dispose.AbstractDispose;

/** 
* @ClassName: TapeoutDispose 
* @Description: php调用通知下线
* @author Fily GUSE
* @date 2017年9月5日 下午3:49:53 
*  
*/
@Component
public class TapeoutDispose extends AbstractDispose {
	/**
	 * @Fields pt : 协议号
	 */
	public Integer pt = 20;
	
	@Override
	public void loginCheck() {
		// 不用验证登录
	}

	@Override
	public void processing() {
		// 解析业务数据
		JSONObject data = JSONUtils.toJSONObject(msg.getData());
		// 获取数据值
		Long user_id = data.getLong("user_id");
		
		Channel channel = ISession.getChannelByuid(user_id);
		if(channel != null) {
			channel.writeAndFlush("you are out!");
			
			channel.close();
		}
		
		msg.setData("");
	}
}
