package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * CityVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class CityVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer id;
	/** 省份id */
	private java.lang.Integer pid;
	/** 省份名称 */
	private java.lang.String name;
	/**  */
	private java.lang.Integer level;
	/**  */
	private java.lang.Integer sort;
	//columns END
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setPid(java.lang.Integer value) {
		this.pid = value;
	}
	
	public java.lang.Integer getPid() {
		return this.pid;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	public void setLevel(java.lang.Integer value) {
		this.level = value;
	}
	
	public java.lang.Integer getLevel() {
		return this.level;
	}
	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}
	
	public java.lang.Integer getSort() {
		return this.sort;
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
		if(obj instanceof CityVo == false) return false;
		if(this == obj) return true;
		CityVo other = (CityVo)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

