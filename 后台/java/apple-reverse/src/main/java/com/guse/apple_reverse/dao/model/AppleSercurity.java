package com.guse.apple_reverse.dao.model;

import org.apache.ibatis.type.Alias;

/** 
* @ClassName: AppleSercurity 
* @Description: TODO
* @author Fily GUSE
* @date 2017年12月5日 下午5:52:57 
*  
*/
@Alias("appleSercurity")
public class AppleSercurity {
	/* 主键 */
	private int id;
	/* 导入批号 */
	private String batch_num;
	/* Apple Id帐号 */
	private String apple_id;
	/* 用户id */
	private int user_id;
	/* 当前的密保问题及答案和密码，示例：{"password":"Guse2017!@#3","questions":[{"id":130,"answer":"朋友3"},{"id":136,"answer":"工作3"},{"id":142,"answer":"父母3"}]} */
	private String currency_security;
	/* 修改后的密保问题及答案和密码,示例：{"newpassword":"Guse2017!@#4","new_questions":[{"id":130,"answer":"朋友3"},{"id":136,"answer":"工作3"},{"id":142,"answer":"父母3"}]} */
	private String new_sercurity;
	/* -1.修改失败,0.已经导入,1.待修改,2.修改中,3.修改成功 */
	private int status;
	public static final int STATUS_FAIL = -1;
	public static final int STATUS_IMPORT = 1;
	public static final int STATUS_SUCCESS = 3;
	public static final int STATUS_EXECTOR = 2;
	/* 状态描述 */
	private String status_comment;
	/* java执行时间 */
	private int modified_time;
	/* 导入时间 */
	private int import_time;
	/* 后台修改时间 */
	private int update_time;
	/* 操作者 */
	private String createby;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBatch_num() {
		return batch_num;
	}
	public void setBatch_num(String batch_num) {
		this.batch_num = batch_num;
	}
	public String getApple_id() {
		return apple_id;
	}
	public void setApple_id(String apple_id) {
		this.apple_id = apple_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getCurrency_security() {
		return currency_security;
	}
	public void setCurrency_security(String currency_security) {
		this.currency_security = currency_security;
	}
	public String getNew_sercurity() {
		return new_sercurity;
	}
	public void setNew_sercurity(String new_sercurity) {
		this.new_sercurity = new_sercurity;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatus_comment() {
		return status_comment;
	}
	public void setStatus_comment(String status_comment) {
		this.status_comment = status_comment;
	}
	public int getModified_time() {
		return modified_time;
	}
	public void setModified_time(int modified_time) {
		this.modified_time = modified_time;
	}
	public int getImport_time() {
		return import_time;
	}
	public void setImport_time(int import_time) {
		this.import_time = import_time;
	}
	public int getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}
	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}

}
