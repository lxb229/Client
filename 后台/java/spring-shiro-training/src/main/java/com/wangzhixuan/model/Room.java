package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 房间表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
public class Room extends Model<Room> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 房间ID
     */
	@TableField("room_id")
	private Integer roomId;
    /**
     * 房间名称
     */
	@TableField("room_name")
	private String roomName;
    /**
     * 房主
     */
	@TableField("house_owner")
	private String houseOwner;
    /**
     * 房主昵称
     */
	@TableField("owner_name")
	private String ownerName;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 解散时间
     */
	@TableField("disappear_time")
	private Date disappearTime;
    /**
     * 时长设置
     */
	@TableField("time_set")
	private Integer timeSet;
    /**
     * 大小盲
     */
	@TableField("size_blind")
	private String sizeBlind;
    /**
     * 携入筹码设置
     */
	@TableField("jetton_set")
	private Integer jettonSet;
    /**
     * 保险
     */
	private Integer insurance;
    /**
     * 闭眼盲
     */
	private Integer straddle;
    /**
     * 局数
     */
	@TableField("party_num")
	private Integer partyNum;
    /**
     * 进入房间人数
     */
	@TableField("join_room_num")
	private Integer joinRoomNum;
    /**
     * 入局人数
     */
	@TableField("join_party_num")
	private Integer joinPartyNum;
    /**
     * 筹码数量
     */
	@TableField("jetton_num")
	private Integer jettonNum;
    /**
     * 房间流水
     */
	@TableField("financial_water")
	private Integer financialWater;
    /**
     * 保险流水
     */
	@TableField("insurance_water")
	private Integer insuranceWater;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getDisappearTime() {
		return disappearTime;
	}

	public void setDisappearTime(Date disappearTime) {
		this.disappearTime = disappearTime;
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

	public Integer getPartyNum() {
		return partyNum;
	}

	public void setPartyNum(Integer partyNum) {
		this.partyNum = partyNum;
	}

	public Integer getJoinRoomNum() {
		return joinRoomNum;
	}

	public void setJoinRoomNum(Integer joinRoomNum) {
		this.joinRoomNum = joinRoomNum;
	}

	public Integer getJoinPartyNum() {
		return joinPartyNum;
	}

	public void setJoinPartyNum(Integer joinPartyNum) {
		this.joinPartyNum = joinPartyNum;
	}

	public Integer getJettonNum() {
		return jettonNum;
	}

	public void setJettonNum(Integer jettonNum) {
		this.jettonNum = jettonNum;
	}

	public Integer getFinancialWater() {
		return financialWater;
	}

	public void setFinancialWater(Integer financialWater) {
		this.financialWater = financialWater;
	}

	public Integer getInsuranceWater() {
		return insuranceWater;
	}

	public void setInsuranceWater(Integer insuranceWater) {
		this.insuranceWater = insuranceWater;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
