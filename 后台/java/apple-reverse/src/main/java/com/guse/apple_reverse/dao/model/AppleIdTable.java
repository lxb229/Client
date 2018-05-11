package com.guse.apple_reverse.dao.model;

import org.apache.ibatis.type.Alias;

/** 
* @ClassName: AppleIdTable 
* @Description: TODO
* @author Fily GUSE
* @date 2017年11月11日 下午4:12:21 
*  
*/
@Alias("appleIdTable")
public class AppleIdTable {
	/* 主键 */
	private int id;
	/* 苹果ID */
	private String apple_id;
	/* 密码 */
	private String apple_pwd;
	/* 用户标识 */
	private int user_id;
	/* 状态：-1.查询失败；0.已经导入；1.查询中；2.查询成功； */
	private int query_status;
	public static int QS_FAILURE = -1;
	public static int QS_IMPORT = 0;
	public static int QS_QUERY = 1;
	public static int QS_QUERY_SUCCESS = 2;
	/* 帐号状态  */
	private String account_status;
	/* 导入时间 */
	private int import_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getApple_id() {
		return apple_id;
	}
	public void setApple_id(String apple_id) {
		this.apple_id = apple_id;
	}
	public String getApple_pwd() {
		return apple_pwd;
	}
	public void setApple_pwd(String apple_pwd) {
		this.apple_pwd = apple_pwd;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getQuery_status() {
		return query_status;
	}
	public void setQuery_status(int query_status) {
		this.query_status = query_status;
	}
	public int getImport_time() {
		return import_time;
	}
	public void setImport_time(int import_time) {
		this.import_time = import_time;
	}
	public String getAccount_status() {
		return account_status;
	}
	public void setAccount_status(String account_status) {
		this.account_status = account_status;
	}

}
