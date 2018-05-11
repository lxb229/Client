package com.guse.four_one_nine.app.model;

/** 
* @ClassName: UserLogin
* @Description: 用户登录实体
* @author: wangkai
* @date: 2018年1月11日 下午4:06:55
*  
*/
public class UserLogin {
	
	/**
	 * 用户标识
	 */
	private Long user_id;
	/**
	 * 登录时间。UNIX时间戳
	 */
	private Long login_date;
	/**
	 * 登录ip信息
	 */
	private String login_ip;
	/**
	 * 登录设备
	 */
	private String login_device;
	/**
	 * 登录地点
	 */
	private String login_site;

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getLogin_date() {
		return login_date;
	}

	public void setLogin_date(Long login_date) {
		this.login_date = login_date;
	}

	public String getLogin_ip() {
		return login_ip;
	}

	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}

	public String getLogin_device() {
		return login_device;
	}

	public void setLogin_device(String login_device) {
		this.login_device = login_device;
	}

	public String getLogin_site() {
		return login_site;
	}

	public void setLogin_site(String login_site) {
		this.login_site = login_site;
	}

}
