package com.guse.platform.utils;

import com.guse.platform.entity.doudou.OperationBaseUser;

/**
 * 用户信息类型（MQ发送、接收）
 * @author yanhua
 *
 */
public class UserInfoType implements java.io.Serializable {
	private static final long serialVersionUID = -8574271852843088757L;
	
	/** 类型代码 1=用户注册信息；2=用户更新信息；3=用户信息纠正 */
	private int typeCode;
	/** 类型名称 */
	private String typeName;
	/** 用户信息对象 */
	private OperationBaseUser userInfo;

	
	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public OperationBaseUser getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(OperationBaseUser userInfo) {
		this.userInfo = userInfo;
	}
	
	
}
