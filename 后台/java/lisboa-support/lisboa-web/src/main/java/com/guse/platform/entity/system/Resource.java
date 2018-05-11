package com.guse.platform.entity.system;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import com.guse.platform.common.base.ValidataBean;



/**
 * tableName : system_resource
 * @author nbin
 * @version 1.0
 */
public class Resource{
	
	
	//columns START
	/** srId */
	private java.lang.Integer srId;
	/** 资源组，所属菜单 */
	private java.lang.Integer srGroup;
	/** 资源名称名称 */
	private java.lang.String srName;
	/** 资源URL */
	private java.lang.String srUrl;
	/** 排序 */
	private java.lang.Integer srSort;
	//columns END
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(srGroup == null){
			 vb.setMsg("请选择资源归属菜单！");
	         return vb;
		 }
		 if(StringUtils.isBlank(srName)){
			 vb.setMsg("请输入资源名称！");
	         return vb;
		 }
		 if(StringUtils.isBlank(srUrl)){
			 vb.setMsg("请输入资源请求URL！");
	         return vb;
		 }
		 vb.setFlag(true);
	     return vb; 
	}

	public void setSrId(java.lang.Integer srId) {
		this.srId = srId;
	}
	
	public java.lang.Integer getSrId() {
		return this.srId;
	}
	public void setSrGroup(java.lang.Integer srGroup) {
		this.srGroup = srGroup;
	}
	
	public java.lang.Integer getSrGroup() {
		return this.srGroup;
	}
	public void setSrName(java.lang.String srName) {
		this.srName = srName;
	}
	
	public java.lang.String getSrName() {
		return this.srName;
	}
	public void setSrUrl(java.lang.String srUrl) {
		this.srUrl = srUrl;
	}
	
	public java.lang.String getSrUrl() {
		return this.srUrl;
	}
	public void setSrSort(java.lang.Integer srSort) {
		this.srSort = srSort;
	}
	
	public java.lang.Integer getSrSort() {
		return this.srSort;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

}

