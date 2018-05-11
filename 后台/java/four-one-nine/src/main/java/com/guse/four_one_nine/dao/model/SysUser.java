package com.guse.four_one_nine.dao.model;

/** 
* @ClassName: SysUser 
* @Description: 系统用户信息
* @author Fily GUSE
* @date 2018年1月16日 下午2:52:45 
*  
*/
public class SysUser {
	
	// 保存sessIon标识
	public static final String SESSION_NAME = "login_SysUser";
	
	/* 用户标识 */
	private Long id;
	/* 姓名 */
	private String name;
	/* 电话 */
	private String phone;
	/* 用户名 */
	private String username;
	/* 密码 */
	private String password;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
