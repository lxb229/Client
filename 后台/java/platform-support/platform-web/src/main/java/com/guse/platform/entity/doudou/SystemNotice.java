package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.entity.system.Users;


/**
 * 
 * 
 * system_notice
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemNotice implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键 */
	private java.lang.Integer snId;
	/** 公告内容 */
	private java.lang.String snContent;
	/** 模板id */
	private java.lang.Integer snTemplateId;
	/** 公告开始时间 */
	private java.util.Date snStartTime;
	/** 公告结束时间 */
	private java.util.Date snEndTime;
	/** 公告展示间隔时间 */
	private java.lang.Integer snIntervalTime;
	/** 创建人 */
	private java.lang.Integer createId;
	/** 创建时间 */
	private java.util.Date createTime;
	
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(StringUtils.isBlank(snContent)){
			 vb.setMsg("请填写公告内容！");
	         return vb;
		 }
		 if(snStartTime == null) {
			 vb.setMsg("请填写公告开始时间！");
	         return vb;
		 }
		 if(snEndTime == null) {
			 vb.setMsg("请填写公告结束时间！");
	         return vb;
		 }
		 if(snStartTime.getTime() >= snEndTime.getTime()) {
			 vb.setMsg("请填写正确的公告结束时间");
	         return vb; 
		 }
		 if(snIntervalTime == null) {
			 vb.setMsg("请填写公告展示间隔时间！");
	         return vb;
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	//columns END
	
	private Users users;
	
	private City city;
	
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public void setSnId(java.lang.Integer value) {
		this.snId = value;
	}
	
	public java.lang.Integer getSnId() {
		return this.snId;
	}
	public void setSnContent(java.lang.String value) {
		this.snContent = value;
	}
	
	public java.lang.String getSnContent() {
		return this.snContent;
	}
	public void setSnTemplateId(java.lang.Integer value) {
		this.snTemplateId = value;
	}
	
	public java.lang.Integer getSnTemplateId() {
		return this.snTemplateId;
	}
	public void setSnStartTime(java.util.Date value) {
		this.snStartTime = value;
	}
	
	public java.util.Date getSnStartTime() {
		return this.snStartTime;
	}
	public void setSnEndTime(java.util.Date value) {
		this.snEndTime = value;
	}
	
	public java.util.Date getSnEndTime() {
		return this.snEndTime;
	}
	public void setSnIntervalTime(java.lang.Integer value) {
		this.snIntervalTime = value;
	}
	
	public java.lang.Integer getSnIntervalTime() {
		return this.snIntervalTime;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSnId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemNotice == false) return false;
		if(this == obj) return true;
		SystemNotice other = (SystemNotice)obj;
		return new EqualsBuilder()
			.append(getSnId(),other.getSnId())
			.isEquals();
	}
	
}

