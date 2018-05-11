package org.chessgame.dao.bean;

/**
 * 胡牌堆对象
 * @author 不能
 *
 */
public class HuCards {
	
	/**
	 * 胡牌数据
	 */
	private String cards;
	
	/**
	 * 第几位胡牌
	 */
	private int hu_index; 

	/**
	 * 是否自摸 
	 * 1：是 0：不是
	 */
	private int self_drawn;
	
	/**
	 * 胡牌类型
	 */
	private int hu_type;
	
	/**
	 * 胡牌番数
	 */
	private int hu_rate;

	/**
	 * 点炮玩家
	 */
	private String hu_p_id;

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}

	public int getHu_index() {
		return hu_index;
	}

	public void setHu_index(int hu_index) {
		this.hu_index = hu_index;
	}

	public int getSelf_drawn() {
		return self_drawn;
	}

	public void setSelf_drawn(int self_drawn) {
		this.self_drawn = self_drawn;
	}

	public int getHu_type() {
		return hu_type;
	}

	public void setHu_type(int hu_type) {
		this.hu_type = hu_type;
	}

	public int getHu_rate() {
		return hu_rate;
	}

	public void setHu_rate(int hu_rate) {
		this.hu_rate = hu_rate;
	}

	public String getHu_p_id() {
		return hu_p_id;
	}

	public void setHu_p_id(String hu_p_id) {
		this.hu_p_id = hu_p_id;
	}



}
