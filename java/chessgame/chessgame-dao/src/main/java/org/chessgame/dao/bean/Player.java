package org.chessgame.dao.bean;

import java.io.Serializable;

/**
 * 玩家表(表)
 * @author 不能
 *
 */
public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7190064098974924221L;
	/**
	 * 玩家编号
	 */
	private int player_serial_no;
	/**
	 * 用户信息对象
	 */
	private Users user;
	/**
	 * 房卡数量
	 */
	private int roomcard_amount;
	
	
	public int getPlayer_serial_no() {
		return player_serial_no;
	}
	public void setPlayer_serial_no(int player_serial_no) {
		this.player_serial_no = player_serial_no;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public int getRoomcard_amount() {
		return roomcard_amount;
	}
	public void setRoomcard_amount(int roomcard_amount) {
		this.roomcard_amount = roomcard_amount;
	}
	
	

}
