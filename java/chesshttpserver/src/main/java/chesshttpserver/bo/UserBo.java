package chesshttpserver.bo;

import java.io.Serializable;

public class UserBo implements Serializable {

	/** 
     *  
     */
	private static final long serialVersionUID = 2928923917001675021L;

	private String userNo;

	private String userName;

	private String phone;

	private String sex;

	private String password;

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}