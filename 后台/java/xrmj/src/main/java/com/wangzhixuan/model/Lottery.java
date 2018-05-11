package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 百轮抽奖
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public class Lottery extends Model<Lottery> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 奖项等级
     */
	@TableField("awards_lv")
	private Integer awardsLv;
    /**
     * 奖品数量
     */
	private Integer amount;
    /**
     * 剩余数量
     */
	private Integer residue;
    /**
     * 百分比
     */
	private BigDecimal percentage;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAwardsLv() {
		return awardsLv;
	}

	public void setAwardsLv(Integer awardsLv) {
		this.awardsLv = awardsLv;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getResidue() {
		return residue;
	}

	public void setResidue(Integer residue) {
		this.residue = residue;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
