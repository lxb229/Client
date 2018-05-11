package com.guse.four_one_nine.app.model;

/**
 * @ClassName: TemplateUse
 * @Description: 模板使用情况实体
 * @author: wangkai
 * @date: 2018年1月11日 下午4:10:01
 * 
 */
public class TemplateUse {
	/**
	 * 模板标识
	 */
	private Long id;
	/**
	 * 推送用户
	 */
	private Long user_id;
	/**
	 * 推送时间
	 */
	private Long time;

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

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

}
