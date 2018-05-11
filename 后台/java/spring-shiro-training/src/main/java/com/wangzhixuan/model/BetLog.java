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
 * 下注记录
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
@TableName("bet_log")
public class BetLog extends Model<BetLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
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
     * 下注数量
     */
	@TableField("bet_num")
	private Integer betNum;
    /**
     * 下注时间
     */
	@TableField("bet_time")
	private Date betTime;


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

	public Integer getBetNum() {
		return betNum;
	}

	public void setBetNum(Integer betNum) {
		this.betNum = betNum;
	}

	public Date getBetTime() {
		return betTime;
	}

	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
