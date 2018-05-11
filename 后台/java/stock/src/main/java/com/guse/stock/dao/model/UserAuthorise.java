package com.guse.stock.dao.model;

/**
 * 用户设备信息
 * @author 不能
 *
 */
public class UserAuthorise {

	/**
	 * 主键，表id
	 */
	private Long id;
	
	/**
	 * 用户哈希值
	 */
	private String hash;
	
	/**
	 * 用户ID
	 */
	private Long user_id;
	
	/**
	 * 设备号
	 */
	private String equipment_number;
	
	/**
	 * IP地址
	 */
	private String ip;
	
	/**
	 * 登陆状态，0:强制下线，1:登录验证中，2:验证失败，3:已登录，4:已注销
	 */
	private int status;
	
	/**
	 * 登录类型，0：出库，1：入库
	 */
	private int type;
	
	/**
	 * 是否在线，0：未在线，1：已在线
	 */
	private int is_online;
	
	/**
	 * 是否授权出库，0：未授权，1：授权
	 */
	private int is_authorise;
	
	/**
	 * 登陆时间
	 */
	private Long login_time;
	
	/**
	 * 注销时间
	 */
	private Long logout_time;
	
	/**
	 * 授权时间
	 */
	private Long auth_time;
	
	/**
	 * 操作人
	 */
	private String create_by;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getEquipment_number() {
		return equipment_number;
	}

	public void setEquipment_number(String equipment_number) {
		this.equipment_number = equipment_number;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIs_online() {
		return is_online;
	}

	public void setIs_online(int is_online) {
		this.is_online = is_online;
	}

	public int getIs_authorise() {
		return is_authorise;
	}

	public void setIs_authorise(int is_authorise) {
		this.is_authorise = is_authorise;
	}

	public Long getLogin_time() {
		return login_time;
	}

	public void setLogin_time(Long login_time) {
		this.login_time = login_time;
	}

	public Long getLogout_time() {
		return logout_time;
	}

	public void setLogout_time(Long logout_time) {
		this.logout_time = logout_time;
	}

	public Long getAuth_time() {
		return auth_time;
	}

	public void setAuth_time(Long auth_time) {
		this.auth_time = auth_time;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	
}
