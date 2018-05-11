package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 银币抽奖奖池
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@TableName("silver_jackpot")
public class SilverJackpot extends Model<SilverJackpot> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 奖项名称
     */
	@TableField("award_name")
	private String awardName;
    /**
     * 奖项等级
     */
	@TableField("award_lv")
	private Integer awardLv;
    /**
     * 奖项数量
     */
	private Integer amount;
    /**
     * 奖项剩余数量
     */
	private Integer residue;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public Integer getAwardLv() {
		return awardLv;
	}

	public void setAwardLv(Integer awardLv) {
		this.awardLv = awardLv;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
