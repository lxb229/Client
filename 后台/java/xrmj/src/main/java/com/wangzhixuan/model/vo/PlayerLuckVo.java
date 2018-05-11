package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 玩家幸运值Vo对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class PlayerLuckVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
     * 表主键id
     */
	private Integer id;
    /**
     * 玩家明星号
     */
	private String startNo;
	/**
	 * 玩家昵称
	 */
	private String nick;
    /**
     * 幸运值
     */
	private Integer luck;
    /**
     * 幸运开始时间
     */
	private Date luckStart;
    /**
     * 幸运结束时间
     */
	private Date luckEnd;
    /**
     * 幸运总次数
     */
	private Integer luckCount;
    /**
     * 幸运剩余次数
     */
	private Integer luckRemain;
    /**
     * 幸运状态
     */
	private Integer luckStatus;
    /**
     * GM操作人
     */
	private Integer userId;
	/**
	 * GM操作人姓名
	 */
	private String userName;
    /**
     * 创建时间
     */
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getLuck() {
		return luck;
	}

	public void setLuck(Integer luck) {
		this.luck = luck;
	}

	public Date getLuckStart() {
		return luckStart;
	}

	public void setLuckStart(Date luckStart) {
		this.luckStart = luckStart;
	}

	public Date getLuckEnd() {
		return luckEnd;
	}

	public void setLuckEnd(Date luckEnd) {
		this.luckEnd = luckEnd;
	}

	public Integer getLuckCount() {
		return luckCount;
	}

	public void setLuckCount(Integer luckCount) {
		this.luckCount = luckCount;
	}

	public Integer getLuckRemain() {
		return luckRemain;
	}

	public void setLuckRemain(Integer luckRemain) {
		this.luckRemain = luckRemain;
	}

	public Integer getLuckStatus() {
		return luckStatus;
	}

	public void setLuckStatus(Integer luckStatus) {
		this.luckStatus = luckStatus;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}