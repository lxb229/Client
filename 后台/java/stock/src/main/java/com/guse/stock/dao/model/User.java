package com.guse.stock.dao.model;

/**
 * 用户信息
 * @author 不能
 *
 */
public class User {
	
	/**
	 * 表主键id
	 */
	private Long id;
	
	/**
	 * 子账号功能
	 */
	private Long pid;
	
	/**
	 * 哈希值
	 */
	private String hash;
	
	/**
	 * 姓名
	 */
	private String realname;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 
	 */
	private String auth_key;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 手机号码
	 */
	private String mobile;
	
	/**
	 * 头像
	 */
	private String thumb;
	
	/**
	 * 
	 */
	private int status;
	
	/**
	 * 状态,0,禁用，1启用
	 */
	private int state;
	
	/**
	 * 创建时间
	 */
	private Long created_at;
	
	/**
	 * 修改时间
	 */
	private Long updated_at;
	
	/**
	 * 1，是员工，2，商家
	 */
	private String role;
	
	/**
	 * 用户登录的sessionid
	 */
	private String sessionid;
	
	/**
	 * 0:下线，1:在线中
	 */
	private int is_online;
	
	/**
	 * 商家申请退店前的角色
	 */
	private String auth;
	
	/**
	 * token
	 */
	private String token;
	
	/**
	 * 1：后端用户，2：前端用户
	 */
	private int isbackend;
	
	/**
	 * 删除标识 0:删除，1未删除
	 */
	private int delflag;
	
	/**
	 * 是否为vip：1:VIP用户，0:普通用户
	 */
	private int is_vip;
	
	/**
	 * 是否禁止出入库，0：允许，1：禁止
	 */
	private int is_forbid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAuth_key() {
		return auth_key;
	}

	public void setAuth_key(String auth_key) {
		this.auth_key = auth_key;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}

	public Long getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Long updated_at) {
		this.updated_at = updated_at;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public int getIs_online() {
		return is_online;
	}

	public void setIs_online(int is_online) {
		this.is_online = is_online;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getIsbackend() {
		return isbackend;
	}

	public void setIsbackend(int isbackend) {
		this.isbackend = isbackend;
	}

	public int getDelflag() {
		return delflag;
	}

	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}

	public int getIs_vip() {
		return is_vip;
	}

	public void setIs_vip(int is_vip) {
		this.is_vip = is_vip;
	}

	public int getIs_forbid() {
		return is_forbid;
	}

	public void setIs_forbid(int is_forbid) {
		this.is_forbid = is_forbid;
	}
	
}
