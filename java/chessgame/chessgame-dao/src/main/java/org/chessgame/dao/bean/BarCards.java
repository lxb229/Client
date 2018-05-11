package org.chessgame.dao.bean;

/**
 * 杠牌堆对象
 * @author 不能
 *
 */
public class BarCards {

	/**
	 * 杠牌数据
	 */
	private String cards;
	
	/**
	 * 杠牌类型 
	 */
	private int bar_type;
	
	/**
	 * 杠牌分数
	 */
	private int bar_score;

	/**
	 * 被杠的玩家 
	 */
	private String bar_p_id;

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}

	public int getBar_type() {
		return bar_type;
	}

	public void setBar_type(int bar_type) {
		this.bar_type = bar_type;
	}

	public int getBar_score() {
		return bar_score;
	}

	public void setBar_score(int bar_score) {
		this.bar_score = bar_score;
	}

	public String getBar_p_id() {
		return bar_p_id;
	}

	public void setBar_p_id(String bar_p_id) {
		this.bar_p_id = bar_p_id;
	}


}
