package com.guse.four_one_nine.dao.model;

import java.util.Date;

public class CashApply {
	private Long id;
	private Long user_id;
	private Integer money;
	private String phone;
	private Long account_type;
	private String account;
	private Date apply_time;
	private Integer status;
	// 申请中
	public static final int STATUS_APPLYING = 1;
	// 审核通过
	public static final int STATUS_SUCCESS = 2;
	// 已打款
	public static final int STATUS_REMIT = 3;
	// 审核拒绝
	public static final int STATUS_FAIL = 0;
	private String auditer;
	private Date audit_time;
	private Date remit_time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getApply_time() {
		return apply_time;
	}

	public void setApply_time(Date apply_time) {
		this.apply_time = apply_time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAuditer() {
		return auditer;
	}

	public void setAuditer(String auditer) {
		this.auditer = auditer;
	}

	public Date getAudit_time() {
		return audit_time;
	}

	public void setAudit_time(Date audit_time) {
		this.audit_time = audit_time;
	}

	public Date getRemit_time() {
		return remit_time;
	}

	public void setRemit_time(Date remit_time) {
		this.remit_time = remit_time;
	}

}
