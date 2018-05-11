package org.chessgame.dao.bean;

import java.io.Serializable;
import java.util.Date;

public class PlayerRoomcardLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 998442555685265191L;
	
	/**
	 * 玩家唯一标识
	 */
	private String player_id;
	
	/**
	 * 房卡数 
	 * 正数增加库存；负数减少库存
	 */
	private int roomcard_count;
	
	/**
	 * 房间唯一标识
	 */
	private String room_id; 
	
	/**
	 * 产品唯一标识
	 */
	private String product_id;
	
	/**
	 * 玩家赠送：另一个玩家唯一标识
	 */
	private String by_player_id;

	/**
	 * 创建时间
	 */
	private Date create_time;

	public String getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(String player_id) {
		this.player_id = player_id;
	}

	public int getRoomcard_count() {
		return roomcard_count;
	}

	public void setRoomcard_count(int roomcard_count) {
		this.roomcard_count = roomcard_count;
	}

	public String getRoom_id() {
		return room_id;
	}

	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getBy_player_id() {
		return by_player_id;
	}

	public void setBy_player_id(String by_player_id) {
		this.by_player_id = by_player_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
