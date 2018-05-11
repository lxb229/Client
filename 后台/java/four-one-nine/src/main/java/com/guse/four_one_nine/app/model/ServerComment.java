package com.guse.four_one_nine.app.model;



/** 
* @ClassName: ServerComment
* @Description: 服务评论实体
* @author: wangkai
* @date: 2018年1月11日 下午4:08:55
*  
*/
public class ServerComment {
	/**
	 * 服务标识
	 */
	private Long service_id;
	/**
	 * 评论用户标识
	 */
	private Long user_id;
	/**
	 * 评论内容
	 */
	private String content;

	/**
	 * 评论时间 yyyy-MM-dd hh:MM:ss
	 */
	private Long comment_time;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getComment_time() {
		return comment_time;
	}

	public void setComment_time(Long comment_time) {
		this.comment_time = comment_time;
	}
	
	

}
