package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.guse.platform.common.base.ValidataBean;


/**
 * 
 * 
 * system_profit
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemProfit implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer id;
	/** 利润金额，影响系统机器人赢牌几率 */
	private java.lang.Integer profit;
	/**机器人今日盈利*/
	private Integer currWinNum;
	/** 赢-最小比例 */
	private java.lang.Integer minPercentum;
	/** 赢-最大比例 */
	private java.lang.Integer maxPercentum;
	/** 修改时间 */
	private java.util.Date updateTime;
	/**倍率胜率因子*/
	private Integer mutiRatePower;
	/**下注胜率因子*/
	private Integer betRatePower;
	//columns END
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(profit == null) {
			 vb.setMsg("利润金额为空！");
	         return vb;
		 }
		 if(minPercentum == null) {
			 vb.setMsg("最小比例为空！");
	         return vb;
		 }
		 if(minPercentum > 100 || minPercentum < 0) {
			 vb.setMsg("最小比例错误！");
	         return vb; 
		 }
		 if(maxPercentum == null) {
			 vb.setMsg("最大比例为空！");
	         return vb;
		 }
		 if(maxPercentum > 100 || maxPercentum < 0) {
			 vb.setMsg("最大比例错误！");
	         return vb; 
		 }
		 if(mutiRatePower == null) {
			 vb.setMsg("倍率胜率因子为空！");
	         return vb; 
		 }
		 if(mutiRatePower > 10 || mutiRatePower < 0) {
			 vb.setMsg("倍率胜率因子错误！");
	         return vb; 
		 }
		 if(betRatePower == null) {
			 vb.setMsg("下注胜率因子为空！");
	         return vb; 
		 }
		 if(betRatePower > 10 || betRatePower < 0) {
			 vb.setMsg("下注胜率因子错误！");
	         return vb; 
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setProfit(java.lang.Integer value) {
		this.profit = value;
	}
	
	public java.lang.Integer getProfit() {
		return this.profit;
	}
	public Integer getCurrWinNum() {
		return currWinNum;
	}

	public void setCurrWinNum(Integer currWinNum) {
		this.currWinNum = currWinNum;
	}

	public void setMinPercentum(java.lang.Integer value) {
		this.minPercentum = value;
	}
	
	public java.lang.Integer getMinPercentum() {
		return this.minPercentum;
	}
	public void setMaxPercentum(java.lang.Integer value) {
		this.maxPercentum = value;
	}
	
	public java.lang.Integer getMaxPercentum() {
		return this.maxPercentum;
	}
	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}
	
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public Integer getMutiRatePower() {
		return mutiRatePower;
	}

	public void setMutiRatePower(Integer mutiRatePower) {
		this.mutiRatePower = mutiRatePower;
	}

	public Integer getBetRatePower() {
		return betRatePower;
	}

	public void setBetRatePower(Integer betRatePower) {
		this.betRatePower = betRatePower;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemProfit == false) return false;
		if(this == obj) return true;
		SystemProfit other = (SystemProfit)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

