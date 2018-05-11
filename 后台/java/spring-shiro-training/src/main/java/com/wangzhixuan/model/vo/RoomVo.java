package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;

import java.io.Serializable;

/**
 * <p>
 * 房间表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
public class RoomVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房间ID
     */
	private Integer roomId;
    /**
     * 房间名称
     */
	private String roomName;
    /**
     * 房主
     */
	private String houseOwner;
    /**
     * 房主昵称
     */
	private String ownerName;
    /**
     * 创建时间
     */
	private Long createTime;
    /**
     * 时长设置
     */
	private Integer timeSet;
    /**
     * 大小盲
     */
	private String sizeBlind;
    /**
     * 携入筹码设置
     */
	private Integer jettonSet;
    /**
     * 保险
     */
	private Integer insurance;
    /**
     * 闭眼盲
     */
	private Integer straddle;
    
	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getHouseOwner() {
		return houseOwner;
	}

	public void setHouseOwner(String houseOwner) {
		this.houseOwner = houseOwner;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getTimeSet() {
		return timeSet;
	}

	public void setTimeSet(Integer timeSet) {
		this.timeSet = timeSet;
	}

	public String getSizeBlind() {
		return sizeBlind;
	}

	public void setSizeBlind(String sizeBlind) {
		this.sizeBlind = sizeBlind;
	}

	public Integer getJettonSet() {
		return jettonSet;
	}

	public void setJettonSet(Integer jettonSet) {
		this.jettonSet = jettonSet;
	}

	public Integer getInsurance() {
		return insurance;
	}

	public void setInsurance(Integer insurance) {
		this.insurance = insurance;
	}

	public Integer getStraddle() {
		return straddle;
	}

	public void setStraddle(Integer straddle) {
		this.straddle = straddle;
	}
	
	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
