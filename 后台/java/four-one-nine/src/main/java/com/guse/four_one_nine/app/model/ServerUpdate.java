package com.guse.four_one_nine.app.model;

/**
 * @ClassName: ServerUpdate
 * @Description: 服务更新实体
 * @author: wangkai
 * @date: 2018年1月11日 下午4:45:21
 * 
 */
public class ServerUpdate {
	/**
	 * @Fields server_id : 服务标识
	 */
	private Long server_id;
	/**
	 * @Fields user_id : 发布用户
	 */
	private Long user_id;
	/**
	 * @Fields name : 服务名称
	 */
	private String name;
	/**
	 * @Fields classify_id : 服务分类标识
	 */
	private Long classify_id;
	/**
	 * @Fields price : 服务单价.精确到分
	 */
	private Integer price;
	/**
	 * @Fields unit : 计量单位
	 */
	private String unit;
	/**
	 * @Fields describe : 服务描述
	 */
	private String describe;
	/**
	 * @Fields picture : 服务图片
	 */
	private String picture;
	/**
	 * @Fields status : 服务状态。1正常，0停用
	 */
	private Integer status;

	public Long getServer_id() {
		return server_id;
	}

	public void setServer_id(Long server_id) {
		this.server_id = server_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getClassify_id() {
		return classify_id;
	}

	public void setClassify_id(Long classify_id) {
		this.classify_id = classify_id;
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

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
