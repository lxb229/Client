package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import com.guse.platform.common.base.ValidataBean;


/**
 * 
 * 
 * mall_proxy
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class MallProxy implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/**  */
	private java.lang.Integer id;
	/** 代理商类型 */
	private java.lang.String proxyType;
	/** 代理商WX号 */
	private java.lang.String wxNO;
	/** 代理商备注 */
	private java.lang.String proxyDesc;
	//columns END
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(StringUtils.isBlank(proxyType)){
			 vb.setMsg("请填写代理商类型！");
	         return vb;
		 }
		 if(StringUtils.isBlank(wxNO)) {
			 vb.setMsg("请填写代理商WX号！");
	         return vb;
		 }
		 if(StringUtils.isBlank(proxyDesc)) {
			 vb.setMsg("请填写代理商备注！");
	         return vb;
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	
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
	public java.lang.String getWxNO() {
		return wxNO;
	}

	public void setWxNO(java.lang.String wxNO) {
		this.wxNO = wxNO;
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
		if(obj instanceof MallProxy == false) return false;
		if(this == obj) return true;
		MallProxy other = (MallProxy)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

