package com.guse.four_one_nine.dao.model;

import java.util.Date;

public class ServerLike {
	private Long id;
	private Long server_id;
	private Long like_user;
	private Date like_time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getServer_id() {
		return server_id;
	}

	public void setServer_id(Long server_id) {
		this.server_id = server_id;
	}

	public Long getLike_user() {
		return like_user;
	}

	public void setLike_user(Long like_user) {
		this.like_user = like_user;
	}

	public Date getLike_time() {
		return like_time;
	}

	public void setLike_time(Date like_time) {
		this.like_time = like_time;
	}

}
