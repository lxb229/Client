package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * MallProxyVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class MallProxyVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/**  */
	private java.lang.Integer id;
	/**  */
	private java.lang.String proxyType;
	/**  */
	private java.lang.String wxNo;
	/**  */
	private java.lang.String proxyDesc;
	//columns END
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setProxyType(java.lang.String value) {
		this.proxyType = value;
	}
	
	public java.lang.String getProxyType() {
		return this.proxyType;
	}
	public void setWxNo(java.lang.String value) {
		this.wxNo = value;
	}
	
	public java.lang.String getWxNo() {
		return this.wxNo;
	}
	public void setProxyDesc(java.lang.String value) {
		this.proxyDesc = value;
	}
	
	public java.lang.String getProxyDesc() {
		return this.proxyDesc;
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
		if(obj instanceof MallProxyVo == false) return false;
		if(this == obj) return true;
		MallProxyVo other = (MallProxyVo)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

