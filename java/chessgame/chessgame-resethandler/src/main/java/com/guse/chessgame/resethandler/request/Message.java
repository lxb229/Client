package com.guse.chessgame.resethandler.request;

import java.io.Serializable;

import net.sf.json.JSONObject;

/** 
* @ClassName: Message 
* @Description: 请求消息
* @author Fily GUSE
* @date 2017年8月11日 下午3:14:56 
*  
*/
public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** 
	 * 服务器用户返回客服端请求
	* @Fields flag : 0或1，0表示请求失败。
	*/
	private Integer flag;
	
	/** 
	* @Fields pid 通信协议
	*/
	private Integer pid;
	/** 
	* @Fields time : 请求时间
	*/
	private String time;
	/** 
	* @Fields md5 : 加密验证：md5生成值(pid+data)
	*/
	private String md5;
	/** 
	 *服务器响应失败时data的json对象为固定格式：
	 *	type(int)客户端消息提示类型，0是不提示，1是弹框提示，2是漂浮提示
	 *	msg(String)错误信息
	* @Fields data : 业务数据
	*/
	private String data;
	
	public Message(){}
	
	public Message(Integer flag ,int pid, String time, String md5, String data) {
		this.flag = flag;
		this.pid = pid;
		this.time = time;
		this.md5 = md5;
		this.data = data;
	}
	
	/** 
	* @Title: setErrorData 
	* @Description: 设置失败信息 
	* @param @param type
	* @param @param msg
	* @return void 
	* @throws 
	*/
	public void setErrorData(int type, String msg) {
		this.flag = 0;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", type);
		jsonObject.put("msg", msg);
		this.data = jsonObject.toString();
	}
	
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
	

}
