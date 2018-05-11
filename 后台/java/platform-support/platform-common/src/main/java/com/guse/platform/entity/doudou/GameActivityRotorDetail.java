package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 
 * 
 * game_activity_rotor_detail
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class GameActivityRotorDetail implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 大转盘明细id */
	private java.lang.String gardId;
	/** 游戏ID */
	private java.lang.Long obuUserid;
	/** 游戏昵称 */
	private java.lang.String obuUserNick;
	/** 投注金额 */
	private java.lang.Integer gardBettingGold;
	/** 中奖奖品：周卡、月卡、钻石5、钻石8、金币2000、金币5000、金币1W、金币5W、金币10W、金币50W、金币300W、金币1000W */
	private java.lang.String gardPrizeName;
	/** 奖品价值金额 */ 
	private java.lang.Integer gardPrizeGold;
	/** 获得道具数量 */
	private java.lang.Integer gardPrizeNumber;
	/** 中奖时间 */
	private java.util.Date gardPrizeTime;
	/** 数据创建时间 */
	private java.util.Date gardCreateTime;
	/** 是否破产;1=破产；非1=不破产 */
	private java.lang.Integer obuIsGoBroke;
	//columns END
	public void setGardId(java.lang.String value) {
		this.gardId = value;
	}
	
	public java.lang.String getGardId() {
		return this.gardId;
	}
	public void setObuUserid(java.lang.Long value) {
		this.obuUserid = value;
	}
	
	public java.lang.Long getObuUserid() {
		return this.obuUserid;
	}
	public void setObuUserNick(java.lang.String value) {
		this.obuUserNick = value;
	}
	
	public java.lang.String getObuUserNick() {
		return this.obuUserNick;
	}
	public void setGardBettingGold(java.lang.Integer value) {
		this.gardBettingGold = value;
	}
	
	public java.lang.Integer getGardBettingGold() {
		return this.gardBettingGold;
	}
	public void setGardPrizeName(java.lang.String value) {
		this.gardPrizeName = value;
	}
	
	public java.lang.String getGardPrizeName() {
		return this.gardPrizeName;
	}
	public void setGardPrizeGold(java.lang.Integer value) {
		this.gardPrizeGold = value;
	}
	
	public java.lang.Integer getGardPrizeGold() {
		return this.gardPrizeGold;
	}
	public void setGardPrizeNumber(java.lang.Integer value) {
		this.gardPrizeNumber = value;
	}
	
	public java.lang.Integer getGardPrizeNumber() {
		return this.gardPrizeNumber;
	}
	public void setGardPrizeTime(java.util.Date value) {
		this.gardPrizeTime = value;
	}
	
	public java.util.Date getGardPrizeTime() {
		return this.gardPrizeTime;
	}
	public void setGardCreateTime(java.util.Date value) {
		this.gardCreateTime = value;
	}
	
	public java.util.Date getGardCreateTime() {
		return this.gardCreateTime;
	}
	public void setObuIsGoBroke(java.lang.Integer value) {
		this.obuIsGoBroke = value;
	}
	
	public java.lang.Integer getObuIsGoBroke() {
		return this.obuIsGoBroke;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getGardId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GameActivityRotorDetail == false) return false;
		if(this == obj) return true;
		GameActivityRotorDetail other = (GameActivityRotorDetail)obj;
		return new EqualsBuilder()
			.append(getGardId(),other.getGardId())
			.isEquals();
	}
	
}

