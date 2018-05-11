package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 
 * 
 * system_task
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemTask implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer id;
	/** 数据类型 */
	private java.lang.Integer taskCmd;
	/** 任务json内容 */
	private java.lang.String jsonContent;
	/** 任务状态0：待处理 1：处理成功 2：处理失败 */
	private java.lang.Integer taskStatus;
	/** 任务处理次数 */
	private java.lang.Integer taskNum;
	/** 创建时间 */
	private java.util.Date createTime;
	//columns END
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setTaskCmd(java.lang.Integer value) {
		this.taskCmd = value;
	}
	
	public java.lang.Integer getTaskCmd() {
		return this.taskCmd;
	}
	public void setJsonContent(java.lang.String value) {
		this.jsonContent = value;
	}
	
	public java.lang.String getJsonContent() {
		return this.jsonContent;
	}
	public void setTaskStatus(java.lang.Integer value) {
		this.taskStatus = value;
	}
	
	public java.lang.Integer getTaskStatus() {
		return this.taskStatus;
	}
	public void setTaskNum(java.lang.Integer value) {
		this.taskNum = value;
	}
	
	public java.lang.Integer getTaskNum() {
		return this.taskNum;
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
		if(obj instanceof SystemTask == false) return false;
		if(this == obj) return true;
		SystemTask other = (SystemTask)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

