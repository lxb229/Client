package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * CorpsVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class CorpsVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer cid;
	/** 表主键id */
	private java.lang.Integer cmd;
	/** 返回code */
	private java.lang.Integer code;
	/** 帮会创建房卡数限制 */
	private java.lang.Integer roomCardLimit;
	/** 帮会最大创建数 */
	private java.lang.Integer createMax;
	/** 帮会成员最大数 */
	private java.lang.Integer maxMemberNum;
	//columns END
	public void setCid(java.lang.Integer value) {
		this.cid = value;
	}
	
	public java.lang.Integer getCid() {
		return this.cid;
	}

	public java.lang.Integer getCmd() {
		return cmd;
	}

	public void setCmd(java.lang.Integer cmd) {
		this.cmd = cmd;
	}

	public java.lang.Integer getCode() {
		return code;
	}

	public void setCode(java.lang.Integer code) {
		this.code = code;
	}

	public java.lang.Integer getRoomCardLimit() {
		return roomCardLimit;
	}

	public void setRoomCardLimit(java.lang.Integer roomCardLimit) {
		this.roomCardLimit = roomCardLimit;
	}

	public java.lang.Integer getCreateMax() {
		return createMax;
	}

	public void setCreateMax(java.lang.Integer createMax) {
		this.createMax = createMax;
	}

	public java.lang.Integer getMaxMemberNum() {
		return maxMemberNum;
	}

	public void setMaxMemberNum(java.lang.Integer maxMemberNum) {
		this.maxMemberNum = maxMemberNum;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getCid())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CorpsVo == false) return false;
		if(this == obj) return true;
		CorpsVo other = (CorpsVo)obj;
		return new EqualsBuilder()
			.append(getCid(),other.getCid())
			.isEquals();
	}
	
}

