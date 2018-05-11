package org.chessgame.dao.bean;

/**
 * 玩法对象
 * @author 不能
 *
 */
public class PlayMethod {

	/**
	 * 自摸设置 
	 * 1：自摸加底 2：自摸加番
	 */
	private int self_drawn_pattern;
	
	/**
	 * 点杠花设置 
	 * 1：点杠花点炮 2：点杠花自摸
	 */
	private int bar_flower_pattern;

	/**
	 * 番型
	 */
	private String rate_type;

	public int getSelf_drawn_pattern() {
		return self_drawn_pattern;
	}

	public void setSelf_drawn_pattern(int self_drawn_pattern) {
		this.self_drawn_pattern = self_drawn_pattern;
	}

	public int getBar_flower_pattern() {
		return bar_flower_pattern;
	}

	public void setBar_flower_pattern(int bar_flower_pattern) {
		this.bar_flower_pattern = bar_flower_pattern;
	}

	public String getRate_type() {
		return rate_type;
	}

	public void setRate_type(String rate_type) {
		this.rate_type = rate_type;
	}
	
}
