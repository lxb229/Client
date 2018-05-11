package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 
 * 用户登出记录
 * operation_base_logout_detail
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class OperationBaseLogoutDetail implements java.io.Serializable {
	
	private static final long serialVersionUID = 5441558222314635342L;
	
	//columns START
	/** 登出明细主键 */
	private java.lang.String obodId;
	/** 游戏ID */
	private java.lang.Long obuUserid;
	/** 游戏昵称 */
	private java.lang.String obuUserNick;
	/** 游戏登录会话标识 */
	private java.lang.String oblSessionid;
	/** 登出时间 */
	private java.util.Date obodLogoutTime;
	/** 登出类型  1=正常退出；2=超时登出；3=踢下线 */
	private java.lang.Integer obodLogoutType;
	/** 备注 */
	private java.lang.String obodRemark;
	
	public java.lang.String getObodId() {
		return obodId;
	}

	public void setObodId(java.lang.String obodId) {
		this.obodId = obodId;
	}

	public java.lang.Long getObuUserid() {
		return obuUserid;
	}

	public void setObuUserid(java.lang.Long obuUserid) {
		this.obuUserid = obuUserid;
	}

	public java.lang.String getObuUserNick() {
		return obuUserNick;
	}

	public void setObuUserNick(java.lang.String obuUserNick) {
		this.obuUserNick = obuUserNick;
	}
	public java.lang.String getOblSessionid() {
		return oblSessionid;
	}
	public void setOblSessionid(java.lang.String oblSessionid) {
		this.oblSessionid = oblSessionid;
	}
	public java.util.Date getObodLogoutTime() {
		return obodLogoutTime;
	}

	public void setObodLogoutTime(java.util.Date obodLogoutTime) {
		this.obodLogoutTime = obodLogoutTime;
	}

	public java.lang.Integer getObodLogoutType() {
		return obodLogoutType;
	}

	public void setObodLogoutType(java.lang.Integer obodLogoutType) {
		this.obodLogoutType = obodLogoutType;
	}

	public java.lang.String getObodRemark() {
		return obodRemark;
	}

	public void setObodRemark(java.lang.String obodRemark) {
		this.obodRemark = obodRemark;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getObodId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof OperationBaseLogoutDetail == false) return false;
		if(this == obj) return true;
		OperationBaseLogoutDetail other = (OperationBaseLogoutDetail)obj;
		return new EqualsBuilder()
			.append(getObodId(),other.getObodId())
			.isEquals();
	}
	
}

