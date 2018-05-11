package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * MallItemVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class MallItemVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer itemId;
	/** 售价 */
	private java.lang.Integer sellPrice;
	/** 房卡数 */
	private java.lang.Integer roomCardNum;
	//columns END
	public void setItemId(java.lang.Integer value) {
		this.itemId = value;
	}
	
	public java.lang.Integer getItemId() {
		return this.itemId;
	}
	public void setSellPrice(java.lang.Integer value) {
		this.sellPrice = value;
	}
	
	public java.lang.Integer getSellPrice() {
		return this.sellPrice;
	}
	public void setRoomCardNum(java.lang.Integer value) {
		this.roomCardNum = value;
	}
	
	public java.lang.Integer getRoomCardNum() {
		return this.roomCardNum;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getItemId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MallItemVo == false) return false;
		if(this == obj) return true;
		MallItemVo other = (MallItemVo)obj;
		return new EqualsBuilder()
			.append(getItemId(),other.getItemId())
			.isEquals();
	}
	
}

