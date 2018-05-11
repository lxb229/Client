package com.guse.four_one_nine.app.model;


/** 
* @ClassName: ActivityVisit
* @Description: 活动访问实体
* @author: wangkai
* @date: 2018年1月11日 下午4:17:26
*  
*/
public class ActivityVisit {
	/**
	 * @Fields activity_id :活动标识
	 */
	private Long activity_id;
	/**
	 * @Fields user_id : 用户标识
	 */
	private String user_id;
	/**
	 * @Fields visit_time : 浏览时间。yyyy-mm-dd hh:MM:ss
	 */
	private Long visit_time;

	public Long getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(Long activity_id) {
		this.activity_id = activity_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Long getVisit_time() {
		return visit_time;
	}

	public void setVisit_time(Long visit_time) {
		this.visit_time = visit_time;
	}

}
