package com.guse.four_one_nine.dao.model;

public class ActivityInfo {
	private Long id;
	private Long activty_id;
	private String channel_name;
	private String channel_url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActivty_id() {
		return activty_id;
	}

	public void setActivty_id(Long activty_id) {
		this.activty_id = activty_id;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getChannel_url() {
		return channel_url;
	}

	public void setChannel_url(String channel_url) {
		this.channel_url = channel_url;
	}

}
