package com.guse.four_one_nine.dao.model;

import java.util.Date;

public class ServerComment {
	private Long id;
	private Long server_id;
	private Long comment_user;
	private String content;
	private Date comment_time;

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

	public Long getComment_user() {
		return comment_user;
	}

	public void setComment_user(Long comment_user) {
		this.comment_user = comment_user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getComment_time() {
		return comment_time;
	}

	public void setComment_time(Date comment_time) {
		this.comment_time = comment_time;
	}
}
