package com.guse.stock.dao.model;

/** 
* @ClassName: Stock 
* @Description: 库存
* @author Fily GUSE
* @date 2017年8月31日 上午11:08:50 
*  
*/
public class Stock {
	
	// 主键
	private Long stock_id;
	// 拥有库存用户id
	private Long user_id = 0l;
	// 生成库存用户ID
	private Long producer_id;
	// 游戏id
	private Long game_id = 0l;
	// 面值id
	private Long par_id;
	// 是否使用 0:没用 1：已用
	private int is_use = 0;
	// 是否千手库存，0：不是；1：是；
	private int is_qs = 0;
	// 是否修改凭证 0：没修改
	private int is_up = 0;
	// 是否为购买库存 1：是 0：否
	private Integer is_buy;
	// 是否已加密 0:未加密，1:已加密
	private int is_encrypted=0;
	// 凭证子表数
	private int cer_table_num=0;
	// 手续费-扣除面值?%的余额
	private double poundage;
	// 累积自动回滚次数
	private int back_rolling_num=0;
	// 生成时间
	private Long create_time;
	
	public Stock(){ }
	
	
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
	public Long getProducer_id() {
		return producer_id;
	}
	public void setProducer_id(Long producer_id) {
		this.producer_id = producer_id;
	}
	public Long getGame_id() {
		return game_id;
	}
	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}
	public Long getPar_id() {
		return par_id;
	}
	public void setPar_id(Long par_id) {
		this.par_id = par_id;
	}
	public int getIs_use() {
		return is_use;
	}
	public void setIs_use(int is_use) {
		this.is_use = is_use;
	}
	public int getIs_qs() {
		return is_qs;
	}
	public void setIs_qs(int is_qs) {
		this.is_qs = is_qs;
	}
	public int getIs_up() {
		return is_up;
	}
	public void setIs_up(int is_up) {
		this.is_up = is_up;
	}
	public Integer getIs_buy() {
		return is_buy;
	}
	public void setIs_buy(Integer is_buy) {
		this.is_buy = is_buy;
	}
	public int getIs_encrypted() {
		return is_encrypted;
	}
	public void setIs_encrypted(int is_encrypted) {
		this.is_encrypted = is_encrypted;
	}
	public int getCer_table_num() {
		return cer_table_num;
	}
	public void setCer_table_num(int cer_table_num) {
		this.cer_table_num = cer_table_num;
	}
	public double getPoundage() {
		return poundage;
	}
	public void setPoundage(double poundage) {
		this.poundage = poundage;
	}
	public int getBack_rolling_num() {
		return back_rolling_num;
	}
	public void setBack_rolling_num(int back_rolling_num) {
		this.back_rolling_num = back_rolling_num;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	
}
