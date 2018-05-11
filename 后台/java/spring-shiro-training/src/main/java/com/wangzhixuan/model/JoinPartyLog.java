package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 玩家入局记录
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
@TableName("join_party_log")
public class JoinPartyLog extends Model<JoinPartyLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 玩家id
     */
	@TableField("player_id")
	private String playerId;
    /**
     * 房间id
     */
	@TableField("room_id")
	private Integer roomId;
    /**
     * 房主id
     */
	@TableField("house_owner")
	private String houseOwner;
    /**
     * 第几局
     */
	@TableField("party_no")
	private Integer partyNo;
    /**
     * 该局开始时间
     */
	@TableField("party_time")
	private Date partyTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getHouseOwner() {
		return houseOwner;
	}

	public void setHouseOwner(String houseOwner) {
		this.houseOwner = houseOwner;
	}

	public Integer getPartyNo() {
		return partyNo;
	}

	public void setPartyNo(Integer partyNo) {
		this.partyNo = partyNo;
	}

	public Date getPartyTime() {
		return partyTime;
	}

	public void setPartyTime(Date partyTime) {
		this.partyTime = partyTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
