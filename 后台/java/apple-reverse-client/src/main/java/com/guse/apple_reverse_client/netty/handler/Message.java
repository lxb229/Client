package com.guse.apple_reverse_client.netty.handler;

import net.sf.json.JSONObject;

import com.guse.apple_reverse_client.Main.ServiceStart;
import com.guse.apple_reverse_client.common.Base64Util;

import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * @ClassName: Message
 * @Description: 消息信息
 * @author Fily GUSE
 * @date 2017年11月23日 下午3:18:57
 * 
 */
public class Message {
	
	// 消息类型
	private Integer type;
	// 消息数据
	private String data;
	// 回执码.1成功，0失败.-1响应消息
	private Integer resutlCode;
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_FAIL = 0;
	public static final int RESULT_RESP = -1;
	// 回执码说明
	private String resultCodeMsg;
	/* 构造方法 */
	public Message() {
	}
	public Message(int type, String data) {
		this.type = type;
		this.data = data;
	}
	
	/**************************************
	 ***************消息类型定义***************
	 **************************************/
	// 客服端可处理消息条数
	public static final int CLIENT_THREAD_NUM = 100;
	
	// 服务器 查询帐号
//	public static final int SERVER_QUERY_APPLE = 210;
//	// 服务器 查询账单
//	public static final int SERVER_QUERY_BILL = 220;
//	// 服务器 账号和账单同时查询
//	public static final int SERVER_QUERY_ALL = 230;
	
	
	/**
	 * @Title: sendMessage
	 * @Description: 发送消息
	 * @param
	 * @return void
	 * @throws
	 */
	public static void sendMessage(Message message) {
		int count = 1;
		while(!EchoClientHandler.channel.isActive()) {
			if(count >= 30) {
				return;
			}
			count ++;
			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		String msg = JSONObject.fromObject(message).toString();
		ServiceStart.INFO_LOG.info("send Message. receiver[{}] data===>:[{}]", EchoClientHandler.channel.remoteAddress()
				.toString(), msg);
		msg = Base64Util.encode(msg);
		msg = msg.replaceAll("[\\s*\t\n\r]", "");
		msg = "[" + msg + "]";
		// 发送消息到服务器
		EchoClientHandler.channel.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
		
	}

	/* get/set */
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Integer getResutlCode() {
		return resutlCode;
	}
	public void setResutlCode(Integer resutlCode) {
		this.resutlCode = resutlCode;
	}
	public String getResultCodeMsg() {
		return resultCodeMsg;
	}
	public void setResultCodeMsg(String resultCodeMsg) {
		this.resultCodeMsg = resultCodeMsg;
	}
}
