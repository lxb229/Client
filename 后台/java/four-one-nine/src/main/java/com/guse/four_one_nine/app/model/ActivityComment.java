package com.guse.four_one_nine.app.model;


/** 
* @ClassName: ActivityComment
* @Description: 活动评价实体
* @author: wangkai
* @date: 2018年1月13日 下午4:23:09
*  
*/
public class ActivityComment {
	/**
	 * @Fields activity_id : 活动标识
	 */
	private Long activity_id;
	/**
	 * @Fields user_id : 评论用户
	 */
	private Long user_id;
	/**
	 * @Fields content : 评论内容
	 */
	private String content;
	/**
	 * @Fields comment_time :评论时间。yyyy-mm-dd hh:MM:ss
	 */
	private Long comment_time;

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
