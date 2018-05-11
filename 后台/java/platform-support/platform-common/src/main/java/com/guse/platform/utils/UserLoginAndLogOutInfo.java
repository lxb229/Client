package com.guse.platform.utils;

import com.guse.platform.entity.doudou.OperationBaseLoginDetail;
import com.guse.platform.entity.doudou.OperationBaseLogoutDetail;

public class UserLoginAndLogOutInfo implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 类型代码 1=用户登录；2=用户登出；*/
	private int typeCode;
	
	private OperationBaseLoginDetail operationBaseLoginDetail;
	
	private OperationBaseLogoutDetail operationBaseLogoutDetail;

	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	public OperationBaseLoginDetail getOperationBaseLoginDetail() {
		return operationBaseLoginDetail;
	}

	public void setOperationBaseLoginDetail(OperationBaseLoginDetail operationBaseLoginDetail) {
		this.operationBaseLoginDetail = operationBaseLoginDetail;
	}

	public OperationBaseLogoutDetail getOperationBaseLogoutDetail() {
		return operationBaseLogoutDetail;
	}

	public void setOperationBaseLogoutDetail(OperationBaseLogoutDetail operationBaseLogoutDetail) {
		this.operationBaseLogoutDetail = operationBaseLogoutDetail;
	}
	
	

}
