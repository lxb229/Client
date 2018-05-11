package com.guse.stock.dao.model;

/** 
* @ClassName: Stock 
* @Description: 库存统计表
* @author Fily GUSE
* @date 
*  
*/
public class StockCounts {
	
	/**
	 * 主键
	 */ 
	private Long id;
	
	/**
	 * 游戏ID
	 */
	private Long game_id;
	
	/**
	 * 面值档位id
	 */
	private Long par_id;
	
	/**
	 * 用户ID
	 */
	private Long user_id;
	
	/**
	 * 总数
	 */
	private Long total;

	/**
	 * 冻结数量
	 */
	private Long locked_cnt;
	
	/**
	 * 已使用数量
	 */
	private Long used_cnt;
	
	/**
	 * 已售数量
	 */
	private Long sold_cnt;
	
	/**
	 * 已购数量
	 */
	private Long buy_cnt;
	
	public StockCounts(){
		
	}
	public StockCounts(long user_id, long game_id, long par_id, long total) {
		this.user_id = user_id;
		this.game_id = game_id;
		this.par_id = par_id;
		this.total = total;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getLocked_cnt() {
		return locked_cnt;
	}

	public void setLocked_cnt(Long locked_cnt) {
		this.locked_cnt = locked_cnt;
	}

	public Long getUsed_cnt() {
		return used_cnt;
	}

	public void setUsed_cnt(Long used_cnt) {
		this.used_cnt = used_cnt;
	}

	public Long getSold_cnt() {
		return sold_cnt;
	}

	public void setSold_cnt(Long sold_cnt) {
		this.sold_cnt = sold_cnt;
	}

	public Long getBuy_cnt() {
		return buy_cnt;
	}

	public void setBuy_cnt(Long buy_cnt) {
		this.buy_cnt = buy_cnt;
	}
	
}
