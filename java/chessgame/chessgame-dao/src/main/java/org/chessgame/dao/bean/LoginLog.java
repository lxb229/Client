package org.chessgame.dao.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录日志对象(表)
 * @author 不能
 *
 */
public class LoginLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3152453333398375347L;
	
	/**
	 * 用户唯一标识
	 */
	private String uid;
	
	/**
	 * 用户登录IP
	 */
	private String login_ip;
	
	/**
	 * 登录时间
	 */
	private Date login_time;
	
	/**
	 * 登录类型
	 *  1：iOS 2：Android
	 */
	private int login_type;
	
	/**
	 * 登录结果 
	 * 1：成功 0：失败
	 */
	private int login_result;
	
	/**
	 * 登录Key 用于加密解密
	 */
	private String login_key;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLogin_ip() {
		return login_ip;
	}

	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}

	public Date getLogin_time() {
		return login_time;
	}

	public void setLogin_time(Date login_time) {
		this.login_time = login_time;
	}

	public int getLogin_type() {
		return login_type;
	}

	public void setLogin_type(int login_type) {
		this.login_type = login_type;
	}

	public int getLogin_result() {
		return login_result;
	}

	public void setLogin_result(int login_result) {
		this.login_result = login_result;
	}

	public String getLogin_key() {
		return login_key;
	}

	public void setLogin_key(String login_key) {
		this.login_key = login_key;
	}
	
	
}
