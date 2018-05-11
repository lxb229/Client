package org.chessgame.dao.bean;

import java.io.Serializable;
import java.util.Date;

public class PlayRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1830184694598756063L;
	
	/**
	 * 房间唯一标识
	 */
	private String room_id;
	
	/**
	 * 第几局战绩
	 */
	private int record_index;
	
	/**
	 * 战绩记录状态 
	 * 1：正常结束 0：提前解散
	 */
	private int record_status;
	
	/**
	 * 开始时间
	 */
	private Date start_time;

	/**
	 * 结束时间
	 */
	private Date end_time;
	
	/**
	 * 玩家战绩小计对象
	 */
	private PlayerRecordSubtotal subtotalList;

	public String getRoom_id() {
		return room_id;
	}

	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}

	public int getRecord_index() {
		return record_index;
	}

	public void setRecord_index(int record_index) {
		this.record_index = record_index;
	}

	public int getRecord_status() {
		return record_status;
	}

	public void setRecord_status(int record_status) {
		this.record_status = record_status;
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

	public PlayerRecordSubtotal getSubtotalList() {
		return subtotalList;
	}

	public void setSubtotalList(PlayerRecordSubtotal subtotalList) {
		this.subtotalList = subtotalList;
	}

}
