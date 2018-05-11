package com.guse.stock.dao.model;

/** 
* @ClassName: StockLog 
* @Description: 入库记录
* @author Fily GUSE
* @date 2017年8月31日 上午11:33:07 
*  
*/
public class StockLog {
	// 主键
	private Long id;
	// 库存编号
	private Long stock_id;
	// 库存录入人ID
	private Long producer_id;
	// 游戏id
	private Long game_id;
	// 面值id
	private Long par_id;
	// 录入时间
	private Long create_time;
	
	public StockLog() { }
	
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
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	
}
