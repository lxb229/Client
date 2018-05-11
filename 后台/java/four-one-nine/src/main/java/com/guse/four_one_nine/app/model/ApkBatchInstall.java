package com.guse.four_one_nine.app.model;

public class ApkBatchInstall {

	/**
	 * 装机标识
	 */
	private Long id;
	/**
	 * 手机品牌
	 */
	private String phone_brand;
	/**
	 * 手机型号
	 */
	private String phone_models;
	/**
	 * IEMI
	 */
	private String iemi;
	/**
	 * 装机时间
	 */
	private Long installed_time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone_brand() {
		return phone_brand;
	}

	public void setPhone_brand(String phone_brand) {
		this.phone_brand = phone_brand;
	}

	public String getPhone_models() {
		return phone_models;
	}

	public void setPhone_models(String phone_models) {
		this.phone_models = phone_models;
	}

	public String getIemi() {
		return iemi;
	}

	public void setIemi(String iemi) {
		this.iemi = iemi;
	}

	public Long getInstalled_time() {
		return installed_time;
	}

	public void setInstalled_time(Long installed_time) {
		this.installed_time = installed_time;
	}

}
