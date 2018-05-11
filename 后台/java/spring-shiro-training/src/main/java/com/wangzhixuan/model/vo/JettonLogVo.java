package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;

import java.io.Serializable;

/**
 * <p>
 * 筹码日志表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public class JettonLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 玩家id
     */
	private String playerId;
    /**
     * 房间id
     */
	private Integer roomId;
    /**
     * 房主id
     */
	private String houseOwner;
    /**
     * 筹码数量
     */
	private Integer jettonNum;
    /**
     * 申请筹码时间
     */
	private Long jettonTime;

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

	public Long getJettonTime() {
		return jettonTime;
	}

	public void setJettonTime(Long jettonTime) {
		this.jettonTime = jettonTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
