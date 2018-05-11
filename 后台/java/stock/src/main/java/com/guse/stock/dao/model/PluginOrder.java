package com.guse.stock.dao.model;

/**
 * 插件购买订单
 * @author 不能
 *
 */
public class PluginOrder {
	
	/**
	 * 表主键id
	 */
	private Long id;
	
	/**
	 * 用户ID
	 */
	private Long user_id;
	
	/**
	 * 插件ID
	 */
	private Long plugin_id;
	
	/**
	 * 订单名称
	 */
	private String order_name;
	
	/**
	 * 订单编号
	 */
	private String order_number;
	
	/**
	 * 付费模式（1：月，2：年）
	 */
	private int pay_pat;
	
	/**
	 * 购买数量
	 */
	private Long number;
	
	/**
	 * 有效期开始时间
	 */
	private Long start_time;
	
	/**
	 * 有效期到期时间
	 */
	private Long end_time;
	
	/**
	 * 支付方式
	 */
	private int pay_type;
	
	/**
	 * 支付金额
	 */
	private Double amount;
	
	/**
	 * 创建时间
	 */
	private Long create_time;
	
	/**
	 * 1：插件商城插件，2，库存管理里的订购服务
	 */
	private int type;

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

	public Long getPlugin_id() {
		return plugin_id;
	}

	public void setPlugin_id(Long plugin_id) {
		this.plugin_id = plugin_id;
	}

	public String getOrder_name() {
		return order_name;
	}

	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public int getPay_pat() {
		return pay_pat;
	}

	public void setPay_pat(int pay_pat) {
		this.pay_pat = pay_pat;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Long getStart_time() {
		return start_time;
	}

	public void setStart_time(Long start_time) {
		this.start_time = start_time;
	}

	public Long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
