package com.guse.four_one_nine.dao.model;

import java.util.Date;

public class OrderSettlement {
	private Long id;
	private Long order_id;
	private Integer seller_income;
	private Long union_id;
	private Long clo_user;
	private Integer clo_income;
	private Date settlement_time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public Integer getSeller_income() {
		return seller_income;
	}

	public void setSeller_income(Integer seller_income) {
		this.seller_income = seller_income;
	}

	public Long getUnion_id() {
		return union_id;
	}

	public void setUnion_id(Long union_id) {
		this.union_id = union_id;
	}

	public Long getClo_user() {
		return clo_user;
	}

	public void setClo_user(Long clo_user) {
		this.clo_user = clo_user;
	}

	public Integer getClo_income() {
		return clo_income;
	}

	public void setClo_income(Integer clo_income) {
		this.clo_income = clo_income;
	}

	public Date getSettlement_time() {
		return settlement_time;
	}

	public void setSettlement_time(Date settlement_time) {
		this.settlement_time = settlement_time;
	}

}
