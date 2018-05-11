package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 道具日志Vo对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class PropLogVo implements Serializable {
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
     * 货币类型(1=房卡 2=金币 3=银币)
     */
	private Integer moneyType;
    /**
     * 数量
     */
	private Integer amount;
    /**
     * 日志类型(1=房卡购买 2=房卡游戏消耗 3=红包领取 4=刷新抽奖 5=银币抽奖 6=金币兑换)
     */
	private Integer logType;
    /**
     * 详细说明
     */
	private String content;
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

	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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