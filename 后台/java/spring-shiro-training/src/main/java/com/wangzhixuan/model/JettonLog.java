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
 * 筹码日志表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
@TableName("jetton_log")
public class JettonLog extends Model<JettonLog> {

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
     * 筹码数量
     */
	@TableField("jetton_num")
	private Integer jettonNum;
    /**
     * 申请筹码时间
     */
	@TableField("jetton_time")
	private Date jettonTime;


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

	public Integer getJettonNum() {
		return jettonNum;
	}

	public void setJettonNum(Integer jettonNum) {
		this.jettonNum = jettonNum;
	}

	public Date getJettonTime() {
		return jettonTime;
	}

	public void setJettonTime(Date jettonTime) {
		this.jettonTime = jettonTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
