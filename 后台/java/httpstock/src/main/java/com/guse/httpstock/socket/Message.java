package com.guse.httpstock.socket;

import java.io.Serializable;

/** 
* @ClassName: Message 
* @Description: 通信消息封装对象
* @author Fily GUSE
* @date 2017年8月25日 下午3:22:04 
*  
*/
public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 通用协议体
	 */
	// 协议号
	private Integer pt;
	// 插件版本
	private String v;
	// hmax-md5加密信息。
	// 加密key为：pt数据.加密数据为： data长度/2+data数据最后5位字符(不足5位0填充)
	private String md5;
	// 业务数据
	private String data;
	/**
	 * 响应信息协议专用
	 */
	// 响应码，0成功。默认成功
	private Long code;
	// 错误消息提示
	private String msg;
	
	public Message(){
		
	}
	
	public Message(int pt, String data) {
		this.pt = pt;
		this.data = data;
	}

	public Integer getPt() {
		return pt;
	}

	public void setPt(Integer pt) {
		this.pt = pt;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
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

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
