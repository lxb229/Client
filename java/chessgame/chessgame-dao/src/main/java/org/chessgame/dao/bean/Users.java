package org.chessgame.dao.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户对象(表)
 * @author 不能
 *
 */
public class Users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 851447839724964272L;
	
	/**
	 * 用户唯一标识
	 */
	private String uid;
	
	/**
	 * 登录类型 
	 * 1：微信登录 2：游客登录
	 */
	private int utype;
	
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
	private int sex;
	
	/**
	 * 登录状态 
	 * 1：在线 0：离线
	 */
	private int online_state;
	
	/**
	 * 用户状态 
	 * 1：普通用户
	 */
	private int user_type;
	
	/**
	 * 真实姓名
	 */
	private String real_name;
	
	/**
	 * 身份证号码
	 */
	private String id_card_number;
	
	/**
	 * 用户有效状态 
	 * 1：有效 0：无效
	 */
	private int status;
	
	/**
	 * 用户注册IP
	 */
	private String create_ip;
	
	/**
	 * 注册时间
	 */
	private Date create_time;
	
	/**
	 * 推荐人
	 */
	private String recommend_id;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getUtype() {
		return utype;
	}

	public void setUtype(int utype) {
		this.utype = utype;
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

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getOnline_state() {
		return online_state;
	}

	public void setOnline_state(int online_state) {
		this.online_state = online_state;
	}

	public int getUser_type() {
		return user_type;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getId_card_number() {
		return id_card_number;
	}

	public void setId_card_number(String id_card_number) {
		this.id_card_number = id_card_number;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreate_ip() {
		return create_ip;
	}

	public void setCreate_ip(String create_ip) {
		this.create_ip = create_ip;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getRecommend_id() {
		return recommend_id;
	}

	public void setRecommend_id(String recommend_id) {
		this.recommend_id = recommend_id;
	}
	
}
