package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;
import java.io.Serializable;

/**
 * <p>
 * 下注记录
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public class BetLogVo implements Serializable {

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
     * 第几局
     */
	private Integer partyNo;
    /**
     * 下注数量
     */
	private Integer betNum;
    /**
     * 下注时间
     */
	private Long betTime;

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

	public Long getBetTime() {
		return betTime;
	}

	public void setBetTime(Long betTime) {
		this.betTime = betTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

}
