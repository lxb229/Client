package com.guse.four_one_nine.app.model;

/** 
* @ClassName: OrderStatus
* @Description: 订单状态实体
* @author: wangkai
* @date: 2018年1月13日 下午6:04:52
*  
*/
public class OrderStatus {
	/**
	 * 服务标识
	 */
	private Long order_id;
	/**
	 * 操作类型。1开始服务，2服务完成
	 */
	private Integer action;

	/**
	 * 变更时间。yyyy-mm-dd hh:MM:ss
	 */
	private Long alert_time;

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public Long getAlert_time() {
		return alert_time;
	}

	public void setAlert_time(Long alert_time) {
		this.alert_time = alert_time;
	}

}
