package org.chessgame.dao.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Rooms implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7505313029118482892L;
	
	/**
	 * 房间编号
	 */
	private int room_serial_no;
	
	/**
	 * 房间类型 
	 * 1：大厅房间 2：俱乐部房间
	 */
	private int room_type;
	
	/**
	 * 俱乐部唯一标识
	 */
	private String club_id;

	/**
	 * 房间语音设置 
	 * 1：普通房 2：语音房
	 */
	private int chat_type;
	
	/**
	 * 局数
	 */
	private int room_count;
	
	/**
	 * 封顶番数
	 */
	private int rate;
	
	/**
	 * 玩法对象
	 */
	private PlayMethod playMethod;
	
	/**
	 * 房间描述说明
	 */
	private String room_description;
	
	/**
	 * 开始时间
	 */
	private Date start_time;
	
	/**
	 * 结束时间
	 */
	private Date end_time;
	
	/**
	 * 状态 
	 * 1：缺人2：满员 3：解散
	 */
	private int status;

	/**
	 * 玩家总计对象
	 */
	private List<PlayerAmount> playerAmountList;

	public int getRoom_serial_no() {
		return room_serial_no;
	}

	public void setRoom_serial_no(int room_serial_no) {
		this.room_serial_no = room_serial_no;
	}

	public int getRoom_type() {
		return room_type;
	}

	public void setRoom_type(int room_type) {
		this.room_type = room_type;
	}

	public String getClub_id() {
		return club_id;
	}

	public void setClub_id(String club_id) {
		this.club_id = club_id;
	}

	public int getChat_type() {
		return chat_type;
	}

	public void setChat_type(int chat_type) {
		this.chat_type = chat_type;
	}

	public int getRoom_count() {
		return room_count;
	}

	public void setRoom_count(int room_count) {
		this.room_count = room_count;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public PlayMethod getPlayMethod() {
		return playMethod;
	}

	public void setPlayMethod(PlayMethod playMethod) {
		this.playMethod = playMethod;
	}

	public String getRoom_description() {
		return room_description;
	}

	public void setRoom_description(String room_description) {
		this.room_description = room_description;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<PlayerAmount> getPlayerAmountList() {
		return playerAmountList;
	}

	public void setPlayerAmountList(List<PlayerAmount> playerAmountList) {
		this.playerAmountList = playerAmountList;
	}


}
