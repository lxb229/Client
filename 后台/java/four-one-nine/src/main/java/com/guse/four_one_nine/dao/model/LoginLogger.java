package com.guse.four_one_nine.dao.model;

import java.util.Date;

public class LoginLogger {
	private Long id;
	private Long user_id;
	private Date login_date;
	private String login_ip;
	private String login_device;
	private String login_site;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Date getLogin_date() {
		return login_date;
	}

	public void setLogin_date(Date login_date) {
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
