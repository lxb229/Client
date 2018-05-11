package com.guse.four_one_nine.app.model;

public class SettlementInfo {
	/**
	 * 订单信息
	 */
	private Long order_id;
	/**
	 * 卖家收入
	 */
	private Integer seller_income;
	/**
	 * 	会长收入
	 */
	private Integer clo_income;
	/**
	 * 工会标识
	 */
	private Long union_id;
	/**
	 * 会长标识
	 */
	private Long union_clo_id;

	/**
	 * 结算时间yyyy-MM-dd hh:MM:ss
	 */
	private Long settlement_time;

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

	public Integer getClo_income() {
		return clo_income;
	}

	public void setClo_income(Integer clo_income) {
		this.clo_income = clo_income;
	}

	public Long getUnion_id() {
		return union_id;
	}

	public void setUnion_id(Long union_id) {
		this.union_id = union_id;
	}

	public Long getUnion_clo_id() {
		return union_clo_id;
	}

	public void setUnion_clo_id(Long union_clo_id) {
		this.union_clo_id = union_clo_id;
	}

	public Long getSettlement_time() {
		return settlement_time;
	}

	public void setSettlement_time(Long settlement_time) {
		this.settlement_time = settlement_time;
	}
	
	

}
