package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * SystemFeedbackVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemFeedbackVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer id;
	/** 用户id */
	private java.lang.Integer userId;
	/** 联系方式 */
	private java.lang.String contactWay;
	/** 内容 */
	private java.lang.String content;
	/** 创建时间 */
	private java.util.Date createTime;
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
	public void setContactWay(java.lang.String value) {
		this.contactWay = value;
	}
	
	public java.lang.String getContactWay() {
		return this.contactWay;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	
	public java.lang.String getContent() {
		return this.content;
	}
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
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
		if(obj instanceof SystemFeedbackVo == false) return false;
		if(this == obj) return true;
		SystemFeedbackVo other = (SystemFeedbackVo)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

