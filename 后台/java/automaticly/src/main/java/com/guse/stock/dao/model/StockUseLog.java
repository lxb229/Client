package com.guse.stock.dao.model;

/**
 * 出库记录对象
 * @author 不能
 *
 */
public class StockUseLog {

	/**
	 * 表主键id
	 */
	private Long id;
	
	/**
	 * 订单编号
	 */
	private String number;
	
	/**
	 * 库存id
	 */
	private Long stock_id;
	
	/**
	 * 用户id
	 */
	private Long user_id;
	
	/**
	 * 属于的uid
	 */
	private Long by_uid;
	
	/**
	 * 状态 0：使用中 1：已使用 2、6：未到账 3、4、5：可能未到账 7：自动回滚
	 */
	private int status;
	
	/**
	 * 手续费
	 */
	private Double poundage;
	
	/**
	 * 备注
	 */
	private String mark;
	
	/**
	 * 生成时间
	 */
	private Long create_time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Long getStock_id() {
		return stock_id;
	}

	public void setStock_id(Long stock_id) {
		this.stock_id = stock_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getBy_uid() {
		return by_uid;
	}

	public void setBy_uid(Long by_uid) {
		this.by_uid = by_uid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Double getPoundage() {
		return poundage;
	}

	public void setPoundage(Double poundage) {
		this.poundage = poundage;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	
	
}
