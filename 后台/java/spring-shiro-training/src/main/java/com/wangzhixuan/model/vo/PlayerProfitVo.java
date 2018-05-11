package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;
import java.io.Serializable;

/**
 * <p>
 * 玩家盈亏记录
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
public class PlayerProfitVo implements Serializable {

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
     * 用户盈亏
     */
	private Integer playerProfit;
    /**
     * 盈利时间
     */
	private Long profitTime;


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

	public Integer getPlayerProfit() {
		return playerProfit;
	}

	public void setPlayerProfit(Integer playerProfit) {
		this.playerProfit = playerProfit;
	}

	public Long getProfitTime() {
		return profitTime;
	}

	public void setProfitTime(Long profitTime) {
		this.profitTime = profitTime;
	}
	
	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

}
