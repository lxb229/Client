package com.guse.four_one_nine.controller.page;

import com.guse.four_one_nine.dao.model.SysUser;

/** 
* @ClassName: ResponseData 
* @Description: 返回消息信息
* @author Fily GUSE
* @date 2018年1月15日 下午8:07:43 
*  
*/
public class ResponseAjax {

	// 是否成功
	private boolean success = true;
	// 显示消息
	private String message;
	// 返回数据信息
	private Object data;
	// 登录用户信息
	private SysUser user;
	
	/* 构造方法 */
	public ResponseAjax(){}
	public ResponseAjax(SysUser user) {
		this.user = user;
	}
	
	/* 设置为失败 */
	public void setFailure(String message) {
		this.message = message;
		this.success = false;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}
	
}
