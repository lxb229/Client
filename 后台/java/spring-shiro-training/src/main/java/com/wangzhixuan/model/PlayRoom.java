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
 * 玩家在房间中的明细
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
@TableName("play_room")
public class PlayRoom extends Model<PlayRoom> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键表id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 玩家id
     */
	@TableField("play_id")
	private String playId;
    /**
     * 玩家昵称
     */
	@TableField("play_nick")
	private String playNick;
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
     * 房主名称
     */
	@TableField("owner_name")
	private String ownerName;
    /**
     * 加入房间时间
     */
	@TableField("join_time")
	private Date joinTime;
    /**
     * 房间创建时间
     */
	@TableField("room_create_time")
	private Date roomCreateTime;
    /**
     * 房间解散时间
     */
	@TableField("room_disappaear_time")
	private Date roomDisappaearTime;
    /**
     * 房间局数
     */
	@TableField("room_party_num")
	private Integer roomPartyNum;
    /**
     * 玩家加入局数
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
     * 玩家在房间的流水
     */
	@TableField("player_financial_water")
	private Integer playerFinancialWater;
    /**
     * 保险流水
     */
	@TableField("insurance_water")
	private Integer insuranceWater;
    /**
     * 玩家保险流水
     */
	@TableField("player_insurance_water")
	private Integer playerInsuranceWater;
    /**
     * 玩家盈利
     */
	@TableField("profit_num")
	private Integer profitNum;
    /**
     * 玩家保险盈利
     */
	@TableField("insurance_profit_num")
	private Integer insuranceProfitNum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlayId() {
		return playId;
	}

	public void setPlayId(String playId) {
		this.playId = playId;
	}

	public String getPlayNick() {
		return playNick;
	}

	public void setPlayNick(String playNick) {
		this.playNick = playNick;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public Date getRoomCreateTime() {
		return roomCreateTime;
	}

	public void setRoomCreateTime(Date roomCreateTime) {
		this.roomCreateTime = roomCreateTime;
	}

	public Date getRoomDisappaearTime() {
		return roomDisappaearTime;
	}

	public void setRoomDisappaearTime(Date roomDisappaearTime) {
		this.roomDisappaearTime = roomDisappaearTime;
	}

	public Integer getRoomPartyNum() {
		return roomPartyNum;
	}

	public void setRoomPartyNum(Integer roomPartyNum) {
		this.roomPartyNum = roomPartyNum;
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

	public Integer getPlayerFinancialWater() {
		return playerFinancialWater;
	}

	public void setPlayerFinancialWater(Integer playerFinancialWater) {
		this.playerFinancialWater = playerFinancialWater;
	}

	public Integer getInsuranceWater() {
		return insuranceWater;
	}

	public void setInsuranceWater(Integer insuranceWater) {
		this.insuranceWater = insuranceWater;
	}

	public Integer getPlayerInsuranceWater() {
		return playerInsuranceWater;
	}

	public void setPlayerInsuranceWater(Integer playerInsuranceWater) {
		this.playerInsuranceWater = playerInsuranceWater;
	}

	public Integer getProfitNum() {
		return profitNum;
	}

	public void setProfitNum(Integer profitNum) {
		this.profitNum = profitNum;
	}

	public Integer getInsuranceProfitNum() {
		return insuranceProfitNum;
	}

	public void setInsuranceProfitNum(Integer insuranceProfitNum) {
		this.insuranceProfitNum = insuranceProfitNum;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
