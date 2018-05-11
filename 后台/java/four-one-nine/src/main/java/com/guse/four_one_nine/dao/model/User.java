package com.guse.four_one_nine.dao.model;

import java.util.Date;

/** 
* @ClassName: User 
* @Description: 用户信息表
* @author Fily GUSE
* @date 2018年1月4日 下午5:57:04 
*  
*/
public class User {

	/*主键*/
	private Long user_id;
	/*昵称*/
	private String nick_name;
	/*电话*/
	private String phone;
	/*头像*/
	private String head_picture;
	/*封面图片*/
	private String cover_picture;
	/*性别。0woman,1man*/
	private Integer sex = 1;
	/*年龄*/
	private Integer age;
	/*出生日期*/
	private String birth_time;
	/*用户来源*/
	private String user_source;
	/*注册IP*/
	private String ip;
	/*所在城市*/
	private String city;
	/*注册时间*/
	private Date registe_time;
	/*实名认证.1已认证֤*/
	private Integer real_certification;
	/*卖家认证֤*/
	private Integer seller_certification;
	/*当前状态.1正常，0禁用*/
	private Integer status = 1;
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
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
	public String getBirth_time() {
		return birth_time;
	}
	public void setBirth_time(String birth_time) {
		this.birth_time = birth_time;
	}
	public String getUser_source() {
		return user_source;
	}
	public void setUser_source(String user_source) {
		this.user_source = user_source;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Date getRegiste_time() {
		return registe_time;
	}
	public void setRegiste_time(Date registe_time) {
		this.registe_time = registe_time;
	}
	public Integer getReal_certification() {
		return real_certification;
	}
	public void setReal_certification(Integer real_certification) {
		this.real_certification = real_certification;
	}
	public Integer getSeller_certification() {
		return seller_certification;
	}
	public void setSeller_certification(Integer seller_certification) {
		this.seller_certification = seller_certification;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
