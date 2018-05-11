package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 奖池
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public class Jackpot extends Model<Jackpot> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 保底奖池
     */
	private BigDecimal minimum;
    /**
     * 众筹奖池
     */
	private BigDecimal crowdfunding;
    /**
     * 领取条件总数
     */
	@TableField("get_number")
	private Integer getNumber;
    /**
     * 每轮奖金
     */
	private Integer bonus;
    /**
     * 已投放保底金额
     */
	@TableField("sendout_minimum")
	private BigDecimal sendoutMinimum;
    /**
     * 已投放众筹金额
     */
	@TableField("sendout_crowdfunding")
	private BigDecimal sendoutCrowdfunding;
    /**
     * 总投资金额
     */
	@TableField("investment_amount")
	private BigDecimal investmentAmount;
    /**
     * 已投放金币
     */
	@TableField("sendout_gold")
	private Integer sendoutGold;
    /**
     * 已投放银币
     */
	@TableField("sendout_silver")
	private Integer sendoutSilver;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getMinimum() {
		return minimum;
	}

	public void setMinimum(BigDecimal minimum) {
		this.minimum = minimum;
	}

	public BigDecimal getCrowdfunding() {
		return crowdfunding;
	}

	public void setCrowdfunding(BigDecimal crowdfunding) {
		this.crowdfunding = crowdfunding;
	}

	public Integer getGetNumber() {
		return getNumber;
	}

	public void setGetNumber(Integer getNumber) {
		this.getNumber = getNumber;
	}

	public Integer getBonus() {
		return bonus;
	}

	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	public BigDecimal getSendoutMinimum() {
		return sendoutMinimum;
	}

	public void setSendoutMinimum(BigDecimal sendoutMinimum) {
		this.sendoutMinimum = sendoutMinimum;
	}

	public BigDecimal getSendoutCrowdfunding() {
		return sendoutCrowdfunding;
	}

	public void setSendoutCrowdfunding(BigDecimal sendoutCrowdfunding) {
		this.sendoutCrowdfunding = sendoutCrowdfunding;
	}

	public BigDecimal getInvestmentAmount() {
		return investmentAmount;
	}

	public void setInvestmentAmount(BigDecimal investmentAmount) {
		this.investmentAmount = investmentAmount;
	}

	public Integer getSendoutGold() {
		return sendoutGold;
	}

	public void setSendoutGold(Integer sendoutGold) {
		this.sendoutGold = sendoutGold;
	}

	public Integer getSendoutSilver() {
		return sendoutSilver;
	}

	public void setSendoutSilver(Integer sendoutSilver) {
		this.sendoutSilver = sendoutSilver;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
