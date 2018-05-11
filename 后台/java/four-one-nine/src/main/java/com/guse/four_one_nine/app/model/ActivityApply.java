package com.guse.four_one_nine.app.model;


/** 
* @ClassName: ActivityApply
* @Description: 活动报名实体
* @author: wangkai
* @date: 2018年1月11日 下午4:20:58
*  
*/
public class ActivityApply {
	/**
	 * @Fields activity_id : 活动标识
	 */
	private Long activity_id;
	/**
	 * @Fields user_id : 报名用户
	 */
	private Long user_id;
	/**
	 * @Fields apply_time : 报名时间。yyyy-mm-dd hh:MM:ss
	 */
	private Long apply_time;

	public Long getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(Long activity_id) {
		this.activity_id = activity_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getApply_time() {
		return apply_time;
	}

	public void setApply_time(Long apply_time) {
		this.apply_time = apply_time;
	}

}
