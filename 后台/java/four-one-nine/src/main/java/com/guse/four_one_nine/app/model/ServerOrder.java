package com.guse.four_one_nine.app.model;

/**
 * @ClassName: ServerOrder
 * @Description: 服务订单实体
 * @author: wangkai
 * @date: 2018年1月11日 下午4:50:32
 * 
 */
public class ServerOrder {
	/**
	 * 订单标识
	 */
	private Long order_id;
	/**
	 * 服务标识
	 */
	private Long server_id;
	/**
	 * 工会标识
	 */
	private Long union_id;
	/**
	 * 小费金额
	 */
	private Integer tip_money;
	/**
	 * 付款方式
	 */
	private Integer pay_type;
	/**
	 * 购买用户标识
	 */
	private Long user_id;
	/**
	 * 购买数量
	 */
	private Integer buy_num;
	/**
	 * 总价
	 */
	private Integer total;
	/**
	 * 购买时间。yyyy-mm-dd hh:MM:ss
	 */
	private Long buy_time;

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public Long getServer_id() {
		return server_id;
	}

	public void setServer_id(Long server_id) {
		this.server_id = server_id;
	}

	public Long getUnion_id() {
		return union_id;
	}

	public void setUnion_id(Long union_id) {
		this.union_id = union_id;
	}

	public Integer getTip_money() {
		return tip_money;
	}

	public void setTip_money(Integer tip_money) {
		this.tip_money = tip_money;
	}

	public Integer getPay_type() {
		return pay_type;
	}

	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
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

	public Long getBuy_time() {
		return buy_time;
	}

	public void setBuy_time(Long buy_time) {
		this.buy_time = buy_time;
	}

}
