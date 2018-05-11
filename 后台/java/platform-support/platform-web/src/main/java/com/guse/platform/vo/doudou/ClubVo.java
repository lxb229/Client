package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * ClubVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class ClubVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer cid;
	/** 俱乐部编号 */
	private java.lang.Integer cno;
	/** 俱乐部创建人 */
	private java.lang.Integer cuserId;
	/** 俱乐部创建时间 */
	private java.util.Date ccreateTime;
	/** 俱乐部状态 1：启用 0：禁用 */
	private java.lang.Integer cstate;
	//columns END
	public void setCid(java.lang.Integer value) {
		this.cid = value;
	}
	
	public java.lang.Integer getCid() {
		return this.cid;
	}
	public void setCno(java.lang.Integer value) {
		this.cno = value;
	}
	
	public java.lang.Integer getCno() {
		return this.cno;
	}
	public void setCuserId(java.lang.Integer value) {
		this.cuserId = value;
	}
	
	public java.lang.Integer getCuserId() {
		return this.cuserId;
	}
	public void setCcreateTime(java.util.Date value) {
		this.ccreateTime = value;
	}
	
	public java.util.Date getCcreateTime() {
		return this.ccreateTime;
	}
	public void setCstate(java.lang.Integer value) {
		this.cstate = value;
	}
	
	public java.lang.Integer getCstate() {
		return this.cstate;
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
		if(obj instanceof ClubVo == false) return false;
		if(this == obj) return true;
		ClubVo other = (ClubVo)obj;
		return new EqualsBuilder()
			.append(getCid(),other.getCid())
			.isEquals();
	}
	
}

