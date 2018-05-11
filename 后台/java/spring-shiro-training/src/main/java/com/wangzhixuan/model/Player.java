package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 玩家表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
@TableName("player")
public class Player extends Model<Player> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 玩家id/玩家明星号
     */
	@TableField("play_id")
	private String playId;
    /**
     * 玩家账户状态
     */
	@TableField("play_status")
	private Integer playStatus;
    /**
     * 注册时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 玩家昵称
     */
	@TableField("play_nick")
	private String playNick;
    /**
     * 注册类型
     */
	@TableField("create_type")
	private Integer createType;
    /**
     * 登录次数
     */
	@TableField("login_num")
	private Integer loginNum;
    /**
     * 连续登录天数
     */
	@TableField("login_days")
	private Integer loginDays;
    /**
     * 上次登录时间
     */
	@TableField("last_login_time")
	private Date lastLoginTime;
    /**
     * 创建次数
     */
	@TableField("create_room_num")
	private Integer createRoomNum;
    /**
     * 加入次数
     */
	@TableField("join_room_num")
	private Integer joinRoomNum;
    /**
     * 牌局次数
     */
	@TableField("join_party_num")
	private Integer joinPartyNum;
    /**
     * 筹码流水
     */
	@TableField("jetton_num")
	private Integer jettonNum;
    /**
     * 盈利流水
     */
	@TableField("profit_num")
	private Integer profitNum;
    /**
     * 2日存留
     */
	@TableField("two_days_keep")
	private Integer twoDaysKeep;
    /**
     * 3日存留
     */
	@TableField("three_days_keep")
	private Integer threeDaysKeep;
    /**
     * 7日存留
     */
	@TableField("seven_days_keep")
	private Integer sevenDaysKeep;


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

	public Integer getPlayStatus() {
		return playStatus;
	}

	public void setPlayStatus(Integer playStatus) {
		this.playStatus = playStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPlayNick() {
		return playNick;
	}

	public void setPlayNick(String playNick) {
		this.playNick = playNick;
	}

	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	public Integer getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(Integer loginNum) {
		this.loginNum = loginNum;
	}

	public Integer getLoginDays() {
		return loginDays;
	}

	public void setLoginDays(Integer loginDays) {
		this.loginDays = loginDays;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getCreateRoomNum() {
		return createRoomNum;
	}

	public void setCreateRoomNum(Integer createRoomNum) {
		this.createRoomNum = createRoomNum;
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

	public Integer getProfitNum() {
		return profitNum;
	}

	public void setProfitNum(Integer profitNum) {
		this.profitNum = profitNum;
	}

	public Integer getTwoDaysKeep() {
		return twoDaysKeep;
	}

	public void setTwoDaysKeep(Integer twoDaysKeep) {
		this.twoDaysKeep = twoDaysKeep;
	}

	public Integer getThreeDaysKeep() {
		return threeDaysKeep;
	}

	public void setThreeDaysKeep(Integer threeDaysKeep) {
		this.threeDaysKeep = threeDaysKeep;
	}

	public Integer getSevenDaysKeep() {
		return sevenDaysKeep;
	}

	public void setSevenDaysKeep(Integer sevenDaysKeep) {
		this.sevenDaysKeep = sevenDaysKeep;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
