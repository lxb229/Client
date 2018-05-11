package com.guse.apple_reverse.netty.handler.session;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.json.JSONObject;

import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.common.Base64Util;
import com.guse.apple_reverse.dao.model.AppleIdTable;
import com.guse.apple_reverse.dao.model.AppleSercurity;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
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
	public static final int SERVER_QUERY_APPLE = 210;
	// 服务器 查询账单
	public static final int SERVER_QUERY_BILL = 220;
	// 服务器 账号和账单同时查询
	public static final int SERVER_QUERY_ALL = 230;
	// 插件查询余额
	public static final int ADT_QUERY_BALANCE = 310;
	
	// 修改密码密保问题
	public static final int CHANGE = 410;
	
	/** 
	* @Title: createQuery 
	* @Description: 创建苹果信息查询命令
	* @param @param apple
	* @param @param query_type
	* @param @return
	* @return Message 
	* @throws 
	*/
	public static Message createQuery(AppleIdTable apple, int query_type) {
		JSONObject json = new JSONObject();
		json.put("id", apple.getId());
		json.put("appleId", apple.getApple_id());
		try {
			json.put("applePw", URLEncoder.encode(apple.getApple_pwd(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String data = json.toString();
		Message msg = new Message(query_type, data);
		return msg;
	}
	
	/** 
	* @Title: createQuery 
	* @Description: 创建苹果信息修改命令 
	* @param @param apple
	* @param @return
	* @return Message 
	* @throws 
	*/
	public static Message createQuery(AppleSercurity apple) {
		Message msg = new Message();
		msg.setType(Message.CHANGE);
		
		// 设置查询内容
		JSONObject data = new JSONObject();
		data.put("id", apple.getId());
		try {
			// 设置查询内容
			data.put("username", apple.getApple_id());
			data.putAll(JSONObject.fromObject(apple.getCurrency_security()));
			JSONObject changeData = JSONObject.fromObject(apple.getNew_sercurity());
			Object np = changeData.get("password");
			if(np != null) {
				data.put("newpassword", np);
			}
			Object nq = changeData.get("questions");
			if(nq != null) {
				data.put("new_questions", nq);
			}
		}catch(Exception e) {
			msg.setResutlCode(Message.RESULT_FAIL);
			msg.setResultCodeMsg("帐号信息异常");
		}
		msg.setData(data.toString());
		return msg;
	}
	
	/**
	 * @Title: sendMessage
	 * @Description: 发送消息
	 * @param
	 * @return void
	 * @throws
	 */
	public static void sendMessage(Channel channel, Message message) {
		String msg = JSONObject.fromObject(message).toString();
		ServiceStart.INFO_LOG.info("send Message. receiver[{}] data===>:[{}]", ClientSessionUtil.getClient(channel).getName(), msg);
		msg = Base64Util.encode(msg);
		msg = msg.replaceAll("[\\s*\t\n\r]", "");
		msg = "[" + msg + "]";
		// 发送消息到服务器
		channel.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
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
