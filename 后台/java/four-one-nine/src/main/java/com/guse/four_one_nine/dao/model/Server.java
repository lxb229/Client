package com.guse.four_one_nine.dao.model;

import java.util.Date;

public class Server {

	// 保存sessIon标识
	public static final String SESSION_NAME = "server";
	private Long id;
	private Long publish_user;
	private Date publish_time;
	private String name;
	private Integer price;
	private String unit;
	private String remarks;
	private String picture;
	private Long classify_id;
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPublish_user() {
		return publish_user;
	}

	public void setPublish_user(Long publish_user) {
		this.publish_user = publish_user;
	}

	public Date getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(Date publish_time) {
		this.publish_time = publish_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Long getClassify_id() {
		return classify_id;
	}

	public void setClassify_id(Long classify_id) {
		this.classify_id = classify_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	

}
