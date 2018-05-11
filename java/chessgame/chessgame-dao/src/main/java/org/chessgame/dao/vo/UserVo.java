package org.chessgame.dao.vo;

/**
 * 用户VO
 * @author 不能
 *
 */
public class UserVo {
	
	/**
	 * 用户唯一标识
	 */
	private String uid;
	
	/**
	 * 用户昵称
	 */
	private String nickname;
	
	/**
	 * 头像 游客无头像
	 */
	private String head_portrait;
	
	/**
	 * 用户性别 
	 * 1：男 0：女
	 */
	private String sex;
	
	/**
	 * 用户状态 
	 * 1：普通用户
	 */
	private String user_type;
	
	/**
	 * 身份证号码
	 */
	private String id_card_number;
	
	/**
	 * 用户有效状态 
	 * 1：有效 0：无效
	 */
	private String status;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHead_portrait() {
		return head_portrait;
	}

	public void setHead_portrait(String head_portrait) {
		this.head_portrait = head_portrait;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getId_card_number() {
		return id_card_number;
	}

	public void setId_card_number(String id_card_number) {
		this.id_card_number = id_card_number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
