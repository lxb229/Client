package org.chessgame.dao.bean;

import java.util.List;

/**
 * 玩家战绩小计对象
 * @author 不能
 *
 */
public class PlayerRecordSubtotal {
	
	/**
	 * 玩家唯一标识
	 */
	private String player_id;
	
	/**
	 * 是否庄家 
	 * 1：庄家 0：闲家
	 */
	private int dealer;
	
	/**
	 * 分数
	 */
	private int score;

	/**
	 * 大赢家标识 
	 * 1：是 0：不是
	 */
	private int big_winner;
	
	/**
	 * 杠牌堆对象
	 */
	private List<BarCards> bar_cards_list;

	/**
	 * 碰牌数据
	 */
	private String touch_cards; 
	
	/**
	 * 手牌数据
	 */
	private String hand_cards; 
	
	/**
	 * 胡牌堆对象
	 */
	private List<HuCards> hu_cards_list;
	
	/**
	 * 查叫
	 * 1:有叫 0：没叫
	 */
	private int have_call;  

	/**
	 * 查花猪 
	 * 1：定缺打完 0：花猪
	 */
	private int flower_pig;

	public String getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(String player_id) {
		this.player_id = player_id;
	}

	public int getDealer() {
		return dealer;
	}

	public void setDealer(int dealer) {
		this.dealer = dealer;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getBig_winner() {
		return big_winner;
	}

	public void setBig_winner(int big_winner) {
		this.big_winner = big_winner;
	}

	public List<BarCards> getBar_cards_list() {
		return bar_cards_list;
	}

	public void setBar_cards_list(List<BarCards> bar_cards_list) {
		this.bar_cards_list = bar_cards_list;
	}

	public String getTouch_cards() {
		return touch_cards;
	}

	public void setTouch_cards(String touch_cards) {
		this.touch_cards = touch_cards;
	}

	public String getHand_cards() {
		return hand_cards;
	}

	public void setHand_cards(String hand_cards) {
		this.hand_cards = hand_cards;
	}

	public List<HuCards> getHu_cards_list() {
		return hu_cards_list;
	}

	public void setHu_cards_list(List<HuCards> hu_cards_list) {
		this.hu_cards_list = hu_cards_list;
	}

	public int getHave_call() {
		return have_call;
	}

	public void setHave_call(int have_call) {
		this.have_call = have_call;
	}

	public int getFlower_pig() {
		return flower_pig;
	}

	public void setFlower_pig(int flower_pig) {
		this.flower_pig = flower_pig;
	} 

}
