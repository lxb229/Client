package com.guse.stock.dao.model;

import org.apache.ibatis.type.Alias;

/** 
* @ClassName: GameAutomaticlyOrder 
* @Description: 订单信息
* @author Fily GUSE
* @date 2017年9月27日 下午6:23:04 
*  
*/
@Alias("gameAutomaticlyOrder")
public class GameAutomaticlyOrder {
	// 主键
	private Long id;
	// 库存编号
	private Long stock_id;
	// 订单单号
	private String order_no;
	// 操作者ID
	private Long user_id;
	// 店铺id
	private Long shop_id;
	// 用户名称
	private String user_name;
	// 店铺名称
	private String shop_name;
	// 游戏id
	private Long game_id;
	// 游戏名称
	private String game_name;
	// 面额id
	private Long par_id;
	// 面额信息
	private String par_info;
	// 区域id
	private Long zone_id;
	// 区域名称
	private String zone_name;
	//
	private Long game_user_id;
	// 使用状态:\r\n0:充值中\r\n1:设备离线(未登录)\r\n2:充值成功\r\n3:充值失败（查询status_comment字段）
	private Integer status;
	public static int STATUS_TOP_UP = 0; //充值中
	public static int STATUS_OFF_LINE = 1; //设备离线(未登录)
	public static int STATUS_SUCCESS = 2; //充值成功
	public static int STATUS_FAILURE = 3; //充值失败
	// 提示信息
	private String status_coment;
	// 第三方：1.是 0.否
	private Integer third_party;
	// 创建时间
	private Long order_time;
	// 成功(失败)时间
	private Long success_time;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStock_id() {
		return stock_id;
	}
	public void setStock_id(Long stock_id) {
		this.stock_id = stock_id;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public Long getShop_id() {
		return shop_id;
	}
	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	public Long getGame_id() {
		return game_id;
	}
	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}
	public String getGame_name() {
		return game_name;
	}
	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}
	public Long getPar_id() {
		return par_id;
	}
	public void setPar_id(Long par_id) {
		this.par_id = par_id;
	}
	public String getPar_info() {
		return par_info;
	}
	public void setPar_info(String par_info) {
		this.par_info = par_info;
	}
	public Long getZone_id() {
		return zone_id;
	}
	public void setZone_id(Long zone_id) {
		this.zone_id = zone_id;
	}
	public String getZone_name() {
		return zone_name;
	}
	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}
	public Long getGame_user_id() {
		return game_user_id;
	}
	public void setGame_user_id(Long game_user_id) {
		this.game_user_id = game_user_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatus_coment() {
		return status_coment;
	}
	public void setStatus_coment(String status_coment) {
		this.status_coment = status_coment;
	}
	public Long getOrder_time() {
		return order_time;
	}
	public void setOrder_time(Long order_time) {
		this.order_time = order_time;
	}
	public Long getSuccess_time() {
		return success_time;
	}
	public void setSuccess_time(Long success_time) {
		this.success_time = success_time;
	}
	public Integer getThird_party() {
		return third_party;
	}
	public void setThird_party(Integer third_party) {
		this.third_party = third_party;
	}

}
