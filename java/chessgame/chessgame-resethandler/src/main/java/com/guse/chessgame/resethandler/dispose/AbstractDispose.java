package com.guse.chessgame.resethandler.dispose;

import java.io.IOException;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.AttributeKey;

import com.guse.chessgame.common.json.JSONUtils;
import com.guse.chessgame.common.util.AesCBC;
import com.guse.chessgame.common.util.HMacMD5;
import com.guse.chessgame.resethandler.common.PlayerInfo;
import com.guse.chessgame.resethandler.request.Message;

/** 
* @ClassName: AbstractDispose 
* @Description: 消息处理封装类
*  调用dispose方法:
*  方法执行了 解密、业务处理（自实现）、回应消息
*  解密(method): 登录key解密data数据
*  业务处理(abstract method): 业务处理，回应消息加密
*  回应消息(method):生成HMAC-MD5
* @author Fily GUSE
* @date 2017年8月18日 下午7:08:33 
*  
*/
public abstract class AbstractDispose {
	

	public static AttributeKey<PlayerInfo> PLAYER_ATTR = AttributeKey.valueOf("player_attr");
	
	/** 
	* @Fields msg : 请求消息
	*/
	public Message msg;
	
	/** 
	* @Fields channel : 连接信息
	*/
	public Channel channel;
	
	
	// 是否解密消息，默认解密
	public boolean if_decode = true;
	// 解密消息方法
	protected void decodeMessage() {
		try {
			// 获取key信息
//			AttributeKey<String> KEY_LOGIN = AttributeKey.valueOf("netty.channel");
//			String key = channel.parent().attr(KEY_LOGIN).get();
			String key = "87473920";
			// 解密消息体数据
			String data = AesCBC.getInstance().decrypt(msg.getData(), key);
			msg.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			// 解密消息失败时调用
			msg.setErrorData(1, "数据解析错误");
			// 解密失败后不需要调用业务方法，直接返回信息
			if_processing = false;
		}
	}
	// 是否处理消息，默认处理
	public boolean if_processing = true;
	// 处理信息方法
	abstract void processing();
	// 是否回应消息，默认回应
	public boolean if_respond = true;
	// 回应消息方法
	protected void respond() throws IOException{
		// 生成HMAC-MD5信息
		String md5 = HMacMD5.getHmacMd5Str(msg.getPid().toString(), msg.getData()).toLowerCase();
		msg.setMd5(md5);
		// 添加服务器时间
		msg.setTime(new Date().getTime() + "");

		// 数据转换
		String request = JSONUtils.toJSONString(msg);
		byte[] bytes = request.getBytes("UTF-8");
		ByteBuf buf = Unpooled.wrappedBuffer(bytes);
		// 推送消息到客服端
		channel.writeAndFlush(new BinaryWebSocketFrame(buf));
	}
	
	
	/** 
	* @Title: dispose 
	* @Description: 执行消息处理 
	* @param 
	* @return void 
	* @throws 
	*/
	public void dispose() {
		// 原始消息就是失败的话就不用执行解密以及业务方法
		if(msg.getFlag() == 0) {
			if_decode = false;
			if_processing = false;
		}
		
		// 第一步解密消息
		if(if_decode) {
			decodeMessage();
			// 解密后数据放在msg对象中
		}
		// 第二部处理消息, 处理时需要对返回数据进行加密
		if(if_processing) {
			processing();
		}
		// 第三步，回应消息
		if(if_respond) {
			try {
				respond();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
