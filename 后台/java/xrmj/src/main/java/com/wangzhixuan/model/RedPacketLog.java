package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 红包抽奖记录
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-27
 */
@TableName("red_packet_log")
public class RedPacketLog extends Model<RedPacketLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 玩家明星号
     */
	@TableField("start_no")
	private String startNo;
    /**
     * 轮数
     */
	@TableField("round_number")
	private Integer roundNumber;
    /**
     * 红包号数
     */
	@TableField("red_packet_no")
	private Integer redPacketNo;
    /**
     * 抽奖计算次数
     */
	@TableField("lottery_number")
	private Integer lotteryNumber;
    /**
     * 保底金额
     */
	@TableField("minimum_amount")
	private BigDecimal minimumAmount;
    /**
     * 众筹金额
     */
	@TableField("crowdfunding_amount")
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
	@TableField("create_time")
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
	protected Serializable pkVal() {
		return this.id;
	}

}
