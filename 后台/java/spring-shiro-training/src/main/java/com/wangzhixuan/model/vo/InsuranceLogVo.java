package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;
import java.io.Serializable;

/**
 * <p>
 * 保险记录
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public class InsuranceLogVo implements Serializable {

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
     * 保险金额
     */
	private Integer insuranceNum;
    /**
     * 保险时间
     */
	private Long insuranceTime;

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

	public Integer getInsuranceNum() {
		return insuranceNum;
	}

	public void setInsuranceNum(Integer insuranceNum) {
		this.insuranceNum = insuranceNum;
	}

	public Long getInsuranceTime() {
		return insuranceTime;
	}

	public void setInsuranceTime(Long insuranceTime) {
		this.insuranceTime = insuranceTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

}
