package com.guse.four_one_nine.app.model;

/**
 * @ClassName: SensitiveUse
 * @Description: 敏感字使用实体
 * @author: wangkai
 * @date: 2018年1月11日 下午4:08:33
 * 
 */
public class SensitiveUse {
	/**
	 * 敏感词标识
	 */
	private Long id;
	/**
	 * 使用用户
	 */
	private Long user_id;

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

}
