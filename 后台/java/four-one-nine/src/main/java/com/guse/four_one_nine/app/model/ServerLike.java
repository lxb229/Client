package com.guse.four_one_nine.app.model;

/**
 * @ClassName: ServerLike
 * @Description: 服务点赞实体
 * @author: wangkai
 * @date: 2018年1月11日 下午4:07:13
 * 
 */
public class ServerLike {
	/**
	 * @Fields service_id : 服务标识
	 */
	private Long service_id;
	/**
	 * @Fields user_id : 点赞用户标识
	 */
	private Long user_id;

	/**
	 * @Fields like_time :点赞时间 yyyy-MM-dd hh:MM:ss
	 */
	private Long like_time;

	public Long getService_id() {
		return service_id;
	}

	public void setService_id(Long service_id) {
		this.service_id = service_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getLike_time() {
		return like_time;
	}

	public void setLike_time(Long like_time) {
		this.like_time = like_time;
	}

}
