package com.guse.four_one_nine.app.model;



/** 
* @ClassName: UserInfo
* @Description: 用户基本信息实体
* @author: wangkai
* @date: 2018年1月11日 下午4:07:09
*  
*/
public class UserInfo {

	/**
	 * 用户标识
	 */
	private Long id;
	/**
	 * 昵称
	 */
	private String nick_name;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 头像
	 */
	private String head_picture;
	/**
	 * 封面图片。,分隔多张图片
	 */
	private String cover_picture;
	/**
	 * 性别。0woman,1man
	 */
	private Integer sex;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 生日。yyyy-mm-dd
	 */
	private String birthday;
	/**
	 * 用户来源
	 */
	private String source;
	/**
	 * 注册ip
	 */
	private String ip;
	/**
	 * 注册地点。多级地址.分隔。
	 */
	private String register_site;
	/**
	 * 注册时间.yyyy-mm-dd
	 */
	private Long register_date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHead_picture() {
		return head_picture;
	}

	public void setHead_picture(String head_picture) {
		this.head_picture = head_picture;
	}

	public String getCover_picture() {
		return cover_picture;
	}

	public void setCover_picture(String cover_picture) {
		this.cover_picture = cover_picture;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRegister_site() {
		return register_site;
	}

	public void setRegister_site(String register_site) {
		this.register_site = register_site;
	}

	public Long getRegister_date() {
		return register_date;
	}

	public void setRegister_date(Long register_date) {
		this.register_date = register_date;
	}

}
