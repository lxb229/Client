package com.guse.four_one_nine.dao.model;

import java.util.Date;

public class ServerOrder {
	private Long id;
	private Long server_id;
	private Long union_id;
	private Long buy_user;
	private Integer buy_num;
	private Integer total;
	private Integer server_money;
	private Integer tip_money;
	private Date buy_time;
	private Integer status;
	private Date finish_time;
	private Integer pay_type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getServer_id() {
		return server_id;
	}

	public void setServer_id(Long server_id) {
		this.server_id = server_id;
	}

	public Long getBuy_user() {
		return buy_user;
	}

	public void setBuy_user(Long buy_user) {
		this.buy_user = buy_user;
	}

	public Integer getBuy_num() {
		return buy_num;
	}

	public void setBuy_num(Integer buy_num) {
		this.buy_num = buy_num;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getServer_money() {
		return server_money;
	}

	public void setServer_money(Integer server_money) {
		this.server_money = server_money;
	}

	public Integer getTip_money() {
		return tip_money;
	}

	public void setTip_money(Integer tip_money) {
		this.tip_money = tip_money;
	}

	public Date getBuy_time() {
		return buy_time;
	}

	public void setBuy_time(Date buy_time) {
		this.buy_time = buy_time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(Date finish_time) {
		this.finish_time = finish_time;
	}

	public Integer getPay_type() {
		return pay_type;
	}

	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}

	public Long getUnion_id() {
		return union_id;
	}

	public void setUnion_id(Long union_id) {
		this.union_id = union_id;
	}

}
