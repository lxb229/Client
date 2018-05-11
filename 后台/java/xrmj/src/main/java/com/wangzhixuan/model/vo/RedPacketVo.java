package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.Date;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * @description：UserVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class RedPacketVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**红包领取-红包号数*/
	private Integer redNo;
	/**红包领取-货币类型*/
	private Integer moneyType;
	/**红包领取-红包数量*/
	private Integer amount;
	/**红包领取-玩家明星号*/
	private String startNo;
	/**红包领取-领取时间*/
	private Date createTime;
	
	public Integer getRedNo() {
		return redNo;
	}

	public void setRedNo(Integer redNo) {
		this.redNo = redNo;
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

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
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