package com.guse.four_one_nine.app.model;

/** 
* @ClassName: CashApply
* @Description: 提现申请实体
* @author: wangkai
* @date: 2018年1月11日 下午4:31:42
*  
*/
public class CashApply {
	/**
	 * @Fields cash_id : 提现申请标识
	 */
	private Long cash_id;
	/**
	 * @Fields user_id : 提现用户
	 */
	private Long user_id;
	/**
	 * @Fields money :提现金额。精确到分
	 */
	private Integer money;
	/**
	 * @Fields phone : 联系电话
	 */
	private String phone;
	/**
	 * @Fields account_type : 提现账号类型
	 */
	private Long account_type;
	/**
	 * @Fields account : 打款帐号
	 */
	private String account;
	/**
	 * @Fields apply_time : 申请时间。yyyy-mm-dd hh:MM:ss
	 */
	private Long apply_time;

	public Long getCash_id() {
		return cash_id;
	}

	public void setCash_id(Long cash_id) {
		this.cash_id = cash_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getAccount_type() {
		return account_type;
	}

	public void setAccount_type(Long account_type) {
		this.account_type = account_type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Long getApply_time() {
		return apply_time;
	}

	public void setApply_time(Long apply_time) {
		this.apply_time = apply_time;
	}

}
