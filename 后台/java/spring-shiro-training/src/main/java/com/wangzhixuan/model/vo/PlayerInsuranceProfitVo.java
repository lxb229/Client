package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;
import java.io.Serializable;

/**
 * <p>
 * 玩家保险盈亏
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
public class PlayerInsuranceProfitVo implements Serializable {

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
     * 保险盈亏
     */
	private Integer insuranceProfit;
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

	public Integer getInsuranceProfit() {
		return insuranceProfit;
	}

	public void setInsuranceProfit(Integer insuranceProfit) {
		this.insuranceProfit = insuranceProfit;
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
