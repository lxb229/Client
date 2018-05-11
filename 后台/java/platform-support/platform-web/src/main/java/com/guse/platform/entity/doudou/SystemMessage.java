package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.guse.platform.entity.system.Users;


/**
 * 
 * 
 * system_message
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemMessage implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键 */
	private java.lang.Integer smId;
	/** 邮件内容 */
	private java.lang.String smContent;
	/** 邮件创建人 */
	private java.lang.Integer createId;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 邮件类型 */
	private java.lang.Integer smType;
	/** 邮件接收人 */
	private java.lang.Integer smUserId;
	/** 邮件状态 */
	private java.lang.String smState;
	//columns END
	
	private Users createUser;
	private Users smUser;
	
	public Users getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Users createUser) {
		this.createUser = createUser;
	}

	public Users getSmUser() {
		return smUser;
	}

	public void setSmUser(Users smUser) {
		this.smUser = smUser;
	}

	public void setSmId(java.lang.Integer value) {
		this.smId = value;
	}
	
	public java.lang.Integer getSmId() {
		return this.smId;
	}
	public void setSmContent(java.lang.String value) {
		this.smContent = value;
	}
	
	public java.lang.String getSmContent() {
		return this.smContent;
	}
	public void setCreateId(java.lang.Integer value) {
		this.createId = value;
	}
	
	public java.lang.Integer getCreateId() {
		return this.createId;
	}
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	public void setSmType(java.lang.Integer value) {
		this.smType = value;
	}
	
	public java.lang.Integer getSmType() {
		return this.smType;
	}
	public void setSmUserId(java.lang.Integer value) {
		this.smUserId = value;
	}
	
	public java.lang.Integer getSmUserId() {
		return this.smUserId;
	}
	public void setSmState(java.lang.String value) {
		this.smState = value;
	}
	
	public java.lang.String getSmState() {
		return this.smState;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSmId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemMessage == false) return false;
		if(this == obj) return true;
		SystemMessage other = (SystemMessage)obj;
		return new EqualsBuilder()
			.append(getSmId(),other.getSmId())
			.isEquals();
	}
	
}

