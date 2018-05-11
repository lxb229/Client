package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 桌子保险配置
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@TableName("table_insurance")
public class TableInsurance extends Model<TableInsurance> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 牌数
     */
	@TableField("card_num")
	private Integer cardNum;
    /**
     * 赔率
     */
	private String rate;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCardNum() {
		return cardNum;
	}

	public void setCardNum(Integer cardNum) {
		this.cardNum = cardNum;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
