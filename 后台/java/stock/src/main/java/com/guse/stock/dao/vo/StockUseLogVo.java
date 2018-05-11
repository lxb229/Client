package com.guse.stock.dao.vo;

public class StockUseLogVo {

	/**
	 * 凭证6
	 */
	private String ios_6;
	
	/**
	 * 凭证7
	 */
	private String ios_7;
	
	/**
	 * 订单编号
	 */
	private String orderid;
	
	/**
	 * 库存编号
	 */
	private Long stockid;

	public String getIos_6() {
		return ios_6;
	}

	public void setIos_6(String ios_6) {
		this.ios_6 = ios_6;
	}

	public String getIos_7() {
		return ios_7;
	}

	public void setIos_7(String ios_7) {
		this.ios_7 = ios_7;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public Long getStockid() {
		return stockid;
	}

	public void setStockid(Long stockid) {
		this.stockid = stockid;
	}
	
}
