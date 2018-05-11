package com.guse.four_one_nine.app.model;


/** 
* @ClassName: UserCertification
* @Description: 用户认证实体
* @author: wangkai
* @date: 2018年1月13日 下午4:07:32
*  
*/
public class UserCertification {
	/**
	 * 用户标识
	 */
	private Long user_id;

	/**
	 * 认证时间.yyyy-mm-dd
	 */
	private Long certification_date;
	/**
	 * 认证相关信息
	 */
	private String other;

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getCertification_date() {
		return certification_date;
	}

	public void setCertification_date(Long certification_date) {
		this.certification_date = certification_date;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}
