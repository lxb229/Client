package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 投资对象Vo
 * @description： InvestmentVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class InvestmentVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
    /**
     * 投资金额
     */
	private BigDecimal amount;
    /**
     * GM操作人
     */
	private Integer userId;
	/**
	 * GM操作人名称
	 */
	private String name;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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