package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * @description：领取红包日志Vo对象
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class RedPacketLogVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
    /**
     * 玩家昵称
     */
	private String nick;
    /**
     * 玩家明星号
     */
	private String startNo;
    /**
     * 轮数
     */
	private Integer roundNumber;
    /**
     * 红包号数
     */
	private Integer redPacketNo;
    /**
     * 抽奖计算次数
     */
	private Integer lotteryNumber;
    /**
     * 保底金额
     */
	private BigDecimal minimumAmount;
    /**
     * 众筹金额
     */
	private BigDecimal crowdfundingAmount;
    /**
     * 奖金类型
     */
	private Integer type;
    /**
     * 奖金
     */
	private Integer amount;
    /**
     * 当时祝福值
     */
	private Integer wish;
    /**
     * 领取时间
     */
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public Integer getRedPacketNo() {
		return redPacketNo;
	}

	public void setRedPacketNo(Integer redPacketNo) {
		this.redPacketNo = redPacketNo;
	}

	public Integer getLotteryNumber() {
		return lotteryNumber;
	}

	public void setLotteryNumber(Integer lotteryNumber) {
		this.lotteryNumber = lotteryNumber;
	}

	public BigDecimal getMinimumAmount() {
		return minimumAmount;
	}

	public void setMinimumAmount(BigDecimal minimumAmount) {
		this.minimumAmount = minimumAmount;
	}

	public BigDecimal getCrowdfundingAmount() {
		return crowdfundingAmount;
	}

	public void setCrowdfundingAmount(BigDecimal crowdfundingAmount) {
		this.crowdfundingAmount = crowdfundingAmount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getWish() {
		return wish;
	}

	public void setWish(Integer wish) {
		this.wish = wish;
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