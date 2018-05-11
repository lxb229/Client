package com.guse.httpstock.socket;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.guse.httpstock.common.Base64Util;
import com.guse.httpstock.common.DESUtil;
import com.guse.httpstock.common.HMacMD5;
import com.guse.httpstock.common.JSONUtils;

/** 
* @ClassName: MessageCoding 
* @Description: 消息编码
* @author Fily GUSE
* @date 2017年9月5日 下午4:26:46 
*  
*/
public class MessageCoding {

	/** 
	* @Title: encryptMsg 
	* @Description: 消息加密 
	* @param @param msg
	* @return void 
	* @throws 
	*/
	public static String encryptMsg(Message msg) {
		
		// des加密
		String data = StringUtils.isNotBlank(msg.getData()) ? msg.getData() : "";
		byte[] result = DESUtil.encrypt(data.getBytes(), DESUtil.STOCK_KEY);
		// base64加密转码
		data = Base64Util.encode(result);
		msg.setData(data);

		// 生成HMAC-MD5信息
		String md5 = HMacMD5.getHmacMd5Str(msg.getPt().toString(),
				(data.length() / 2) + data.substring(data.length() - 5));
		msg.setMd5(md5);
		
		// 数据转换
		JSONObject msgJSON = JSONUtils.toJSONObject(msg);
		msgJSON.remove("code");
		msgJSON.remove("msg");
		
		return msgJSON.toString();
		
	}
	
	/** 
	* @Title: decryptMsg 
	* @Description: 解密消息 
	* @param @param msgStr
	* @param @return
	* @return Message 
	* @throws 
	*/
	public static Message decryptMsg(String msgStr) throws Exception{
		
		// 客户端数据转成业务对象
		Message msg = JSONUtils.toBean(msgStr, Message.class);
		
		// 验证hmac-md5
		String data = msg.getData();
		
		if(StringUtils.isBlank(data) || StringUtils.isBlank(msg.getMd5())) {
			throw new NullPointerException("消息包为空");
		}
		String md5 = HMacMD5.getHmacMd5Str(msg.getPt().toString(),
				(data.length() / 2) + data.substring(data.length() - 5));
		if (!md5.toUpperCase().equals(msg.getMd5().toUpperCase())) {
			throw new Exception("消息验证失败");
		} else {
			// 解码data数据
			try {
				data = new String(DESUtil.decrypt(Base64Util.decode(data),
						DESUtil.STOCK_KEY));
				msg.setData(data);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("业务数据异常");
			}
		}
		
		return msg;
	}
	
	public static void main(String[] args) {
		JSONObject data = new JSONObject();
		data.put("username", "111");
		data.put("password", "111");
		data.put("device_number", "111");
		data.put("version", "111");
		Message msg = new Message(100,data.toString());
		
		System.out.println(encryptMsg(msg));
		
		System.out.println();
	}
}
