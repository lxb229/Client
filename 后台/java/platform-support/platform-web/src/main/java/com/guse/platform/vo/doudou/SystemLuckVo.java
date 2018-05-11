package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * SystemLuckVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemLuckVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer id;
	/** 玩家id */
	private java.lang.Integer userId;
	/** 幸运值 */
	private java.lang.Double luck;
	/** 幸运开始时间 */
	private java.util.Date luckStart;
	/** 幸运结束时间 */
	private java.util.Date luckEnd;
	/** 幸运剩余次数 */
	private java.lang.Integer luckCount;
	/** 幸运状态 */
	private java.lang.Integer luckState;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 创建人 */
	private java.lang.Integer createId;
	//columns END
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setUserId(java.lang.Integer value) {
		this.userId = value;
	}
	
	public java.lang.Integer getUserId() {
		return this.userId;
	}
	public void setLuck(java.lang.Double value) {
		this.luck = value;
	}
	
	public java.lang.Double getLuck() {
		return this.luck;
	}
	public void setLuckStart(java.util.Date value) {
		this.luckStart = value;
	}
	
	public java.util.Date getLuckStart() {
		return this.luckStart;
	}
	public void setLuckEnd(java.util.Date value) {
		this.luckEnd = value;
	}
	
	public java.util.Date getLuckEnd() {
		return this.luckEnd;
	}
	public void setLuckCount(java.lang.Integer value) {
		this.luckCount = value;
	}
	
	public java.lang.Integer getLuckCount() {
		return this.luckCount;
	}
	public void setLuckState(java.lang.Integer value) {
		this.luckState = value;
	}
	
	public java.lang.Integer getLuckState() {
		return this.luckState;
	}
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	public void setCreateId(java.lang.Integer value) {
		this.createId = value;
	}
	
	public java.lang.Integer getCreateId() {
		return this.createId;
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
		if(obj instanceof SystemLuckVo == false) return false;
		if(this == obj) return true;
		SystemLuckVo other = (SystemLuckVo)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

