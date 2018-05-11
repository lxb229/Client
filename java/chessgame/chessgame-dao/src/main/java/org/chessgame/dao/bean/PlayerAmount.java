package org.chessgame.dao.bean;

/**
 * 房间的玩家总计对象
 * @author 不能
 *
 */
public class PlayerAmount {

	/**
	 * 玩家唯一标识
	 */
	private String player_id;
	
	/**
	 * 方位
	 * 1：东 2：南 3：西 4：北
	 */
	private int orientation; 
	
	/**
	 * 房主标识 
	 * 1：房主 0：非房主
	 */
	private int house_owner;
	
	/**
	 * 总分 
	 * 基础分为0分，正负分表示输赢
	 */
	private int score_amount; 
	
	/**
	 * 大赢家标识 
	 * 1：大赢家 0：非大赢家
	 */
	private int big_winner;
	
	/**
	 * 自摸次数
	 */
	private int self_drawn;
	
	/**
	 * 接炮次数
	 */
	private int answer_gun;

	/**
	 * 点炮次数
	 */
	private int point_gun; 

	/**
	 * 暗杠次数
	 */
	private int dark_bar;

	/**
	 * 明杠次数
	 */
	private int ming_bar;

	/**
	 * 查叫次数
	 */
	private int have_call; 
	
	/**
	 * 最佳炮手标识 1：是 0：不是
	 */
	private int best_gunner;

	public String getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(String player_id) {
		this.player_id = player_id;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getHouse_owner() {
		return house_owner;
	}

	public void setHouse_owner(int house_owner) {
		this.house_owner = house_owner;
	}

	public int getScore_amount() {
		return score_amount;
	}

	public void setScore_amount(int score_amount) {
		this.score_amount = score_amount;
	}

	public int getBig_winner() {
		return big_winner;
	}

	public void setBig_winner(int big_winner) {
		this.big_winner = big_winner;
	}

	public int getSelf_drawn() {
		return self_drawn;
	}

	public void setSelf_drawn(int self_drawn) {
		this.self_drawn = self_drawn;
	}

	public int getAnswer_gun() {
		return answer_gun;
	}

	public void setAnswer_gun(int answer_gun) {
		this.answer_gun = answer_gun;
	}

	public int getPoint_gun() {
		return point_gun;
	}

	public void setPoint_gun(int point_gun) {
		this.point_gun = point_gun;
	}

	public int getDark_bar() {
		return dark_bar;
	}

	public void setDark_bar(int dark_bar) {
		this.dark_bar = dark_bar;
	}

	public int getMing_bar() {
		return ming_bar;
	}

	public void setMing_bar(int ming_bar) {
		this.ming_bar = ming_bar;
	}

	public int getHave_call() {
		return have_call;
	}

	public void setHave_call(int have_call) {
		this.have_call = have_call;
	}

	public int getBest_gunner() {
		return best_gunner;
	}

	public void setBest_gunner(int best_gunner) {
		this.best_gunner = best_gunner;
	} 



}
