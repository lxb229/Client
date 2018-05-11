package com.guse.stock.netty.handler.dispose;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.guse.stock.common.Base64Util;
import com.guse.stock.common.DESUtil;
import com.guse.stock.common.HMacMD5;
import com.guse.stock.common.JSONUtils;
import com.guse.stock.netty.handler.Message;
import com.guse.stock.netty.handler.MessageCode;
import com.guse.stock.netty.handler.SocketServerHandler;

import io.netty.channel.Channel;

/**
 * @ClassName: AbstractDispose
 * @Description: 消息处理封装类 调用dispose方法: 方法执行了 解密、业务处理（自实现）、回应消息 解密(method):
 *               登录key解密data数据 业务处理(abstract method): 业务处理，回应消息加密
 *               回应消息(method):生成HMAC-MD5
 * @author Fily GUSE
 * @date 2017年8月18日 下午7:08:33
 * 
 */
public abstract class AbstractDispose {
	
	// 协议号
	public Integer pt;

	// 请求消息
	public Message msg;

	// 连接信息
	public Channel channel;

	// 当前连接对象
	public SocketServerHandler handler;

	// 是否解密消息，默认解密
	public boolean if_decode = true;
	// 是否处理消息，默认处理
	public boolean if_processing = true;
	// 是否响应消息，默认响应
	public boolean if_respond = true;
	
	/** 
	 * 抽象方法，用于处理不同协议之间的业务信息
	 * @Title: processing 
	 * @param 
	 * @return void 
	 * @throws 
	 */
	public abstract void processing();
	
	
	/**
	 * @Title: init
	 * @Description: 初始化数据
	 * @param
	 * @return void
	 * @throws
	 */
	private void init() {
		if_decode = true;
		if_processing = true;
		if_respond = true;
	}
	
	/** 
	* @Title: loginCheck 
	* @Description: 登录验证 
	* @param 
	* @return void 
	* @throws 
	*/
	public void loginCheck() {
		if (handler.userInfo == null) {
			msg.setError(MessageCode.NOTLOGIN);
			if_processing = false;
		}
	}

	/** 
	* @Title: decodeMessage 
	* @Description: 执行消息解密 
	* @param 
	* @return void 
	* @throws 
	*/
	public void decodeMessage() {
		// 验证hmac-md5
		String data = msg.getData();
		
		if(StringUtils.isBlank(data) || StringUtils.isBlank(msg.getMd5())) {
			msg.setError(MessageCode.NULLDATA);
			if_processing = false;
			return;
		}
		String md5 = HMacMD5.getHmacMd5Str(msg.getPt().toString(),
				(data.length() / 2) + data.substring(data.length() - 5));
		if (!md5.toUpperCase().equals(msg.getMd5().toUpperCase())) {
			// 解密失败后不需要调用业务方法，直接返回信息
			msg.setError(MessageCode.INVALIDDATA);
			if_processing = false;
		} else {
			// 解码data数据
			try {
				data = new String(DESUtil.decrypt(Base64Util.decode(data),
						DESUtil.STOCK_KEY));
				msg.setData(data);
			} catch (Exception e) {
				e.printStackTrace();
				msg.setError(MessageCode.ENCRYPTERROR);
				if_processing = false;
			}
		}
	}


	/** 
	* @Title: respond 
	* @Description: 回应客服端，发送响应消息到连接端
	* @param @throws IOException
	* @return void 
	* @throws 
	*/
	public void respond() throws IOException {
		// 错误数据data设置为null
		if (msg.getCode() != MessageCode.SUCCESS) {
			msg.setData("");
		}
		// des加密
		String data = msg.getData();
		byte[] desData = DESUtil.encrypt(data.getBytes(), DESUtil.STOCK_KEY);
		// base64加密转码
		data = Base64Util.encode(desData);
		msg.setData(data);

		// 生成HMAC-MD5信息
		String md5 = HMacMD5.getHmacMd5Str(msg.getPt().toString(),
				(data.length() / 2) + data.substring(data.length() - 5));
		msg.setMd5(md5);

		// 数据转换
		JSONObject result = JSONUtils.toJSONObject(msg);
		result.put("code", msg.getCode().getCode());
		result.put("msg", msg.getCode().getDes());

		// 推送消息到客服端
		channel.writeAndFlush(result.toString());
	}

	/**
	 * 业务消息处理
	 *  业务执行顺序为:登录验证(loginCheck)->解密消息(decodeMessage)->处理消息(processing)->响应消息(respond)
	 *  每个方法都有一个boolean变量控制是否执行响应的方法。变量命名方式为 if_开始，方法名结尾。默认为true，表示都要执行
	 * @Title: dispose
	 * @Description: 
	 * @param
	 * @return void
	 * @throws
	 */
	public void dispose() {
		// 执行初始化信息，该方法不能重写
		init();
		
		/***
		 * 以下方法为具体业务流程方法，如有需要可以直接重写以下方法
		 */
		
		// 登录验证
		loginCheck();

		// 原始消息就是失败的话就不用执行解密以及业务方法
		if (msg.getCode() != MessageCode.SUCCESS) {
			if_decode = false;
			if_processing = false;
		}

		// 第一步解密消息
		if (if_decode) {
			decodeMessage();
			// 解密后数据放在msg对象中
		}
		// 第二部处理消息, 处理时需要对返回数据进行加密
		if (if_processing) {
			try {
				processing();
			} catch (Exception e) {
				e.printStackTrace();
				msg.setError(MessageCode.DECODEERROR);
			}
		}
		// 第三步，回应消息
		if (if_respond) {
			try {
				respond();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) {
		// 请求消息提生成
		Message msg = new Message();
		
		///*****
		// 这里设置请求参数
		msg.setPt(100);
		msg.setData("{'username':'15208480237', 'password':'123456', 'device_number': 'sdfsd', 'version':'c'}");
		
		
		//*********
		
		// des加密
		String data = msg.getData();
		byte[] desData = DESUtil.encrypt(data.getBytes(), DESUtil.STOCK_KEY);
		// base64加密转码
		data = Base64Util.encode(desData);
		msg.setData(data);
		// 生成HMAC-MD5信息
		String md5 = HMacMD5.getHmacMd5Str(msg.getPt().toString(),
				(data.length() / 2) + data.substring(data.length() - 5));
		msg.setMd5(md5);

		// 数据转换
		JSONObject result = JSONUtils.toJSONObject(msg);
		result.remove("code");
		result.remove("msg");
		
		System.out.println(result);
	}

}
