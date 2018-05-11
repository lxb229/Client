package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 
 * 
 * operation_base_login_detail
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class OperationBaseLoginDetail implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 登录明细主键 */
	private java.lang.String oblId;
	/** 游戏ID */
	private java.lang.Long obuUserid;
	/** 游戏昵称 */
	private java.lang.String obuUserNick;
	/** 游戏账号(手机号) */
	private java.lang.String obuUsename;
	/** 游戏登录会话标识 */
	private java.lang.String oblSessionid;
	/** 登录时间 */
	private java.util.Date oblLoginTime;
	/** 登录IP */
	private java.lang.String oblLoginIp;
	/** 登录区域 */
	private java.lang.String oblLoginRegion;
	/** 下载渠道 */
	private java.lang.String obdDloadChannel;
	/** IOS、安卓 */
	private java.lang.String obaOsType;
	/** 操作系统名称 */
	private java.lang.String obaOsName;
	/** 设备类型：机型,如：OPPO R9 */
	private java.lang.String obaDeviceModel;
	/** 设备标识 */
	private java.lang.String obaDeviceId;
	/** 设备内存 */
	private java.lang.Integer obaDeviceMemory;
	/** 设备网络 */
	private java.lang.String obaDeviceNet;
	/** 设备分辨率 */
	private java.lang.String obaDeviceResolution;
	/** 运营商 */
	private java.lang.String obaTelcos;
	//columns END
	public void setOblId(java.lang.String value) {
		this.oblId = value;
	}
	
	public java.lang.String getOblId() {
		return this.oblId;
	}
	public void setObuUserid(java.lang.Long value) {
		this.obuUserid = value;
	}
	
	public java.lang.Long getObuUserid() {
		return this.obuUserid;
	}
	public void setObuUserNick(java.lang.String value) {
		this.obuUserNick = value;
	}
	
	public java.lang.String getObuUserNick() {
		return this.obuUserNick;
	}
	public void setObuUsename(java.lang.String value) {
		this.obuUsename = value;
	}
	
	public java.lang.String getObuUsename() {
		return this.obuUsename;
	}
	public void setOblLoginTime(java.util.Date value) {
		this.oblLoginTime = value;
	}
	
	public java.lang.String getOblSessionid() {
		return oblSessionid;
	}

	public void setOblSessionid(java.lang.String oblSessionid) {
		this.oblSessionid = oblSessionid;
	}
	
	public java.util.Date getOblLoginTime() {
		return this.oblLoginTime;
	}
	public void setOblLoginIp(java.lang.String value) {
		this.oblLoginIp = value;
	}
	
	public java.lang.String getOblLoginIp() {
		return this.oblLoginIp;
	}
	public void setOblLoginRegion(java.lang.String value) {
		this.oblLoginRegion = value;
	}
	
	public java.lang.String getOblLoginRegion() {
		return this.oblLoginRegion;
	}
	public void setObdDloadChannel(java.lang.String value) {
		this.obdDloadChannel = value;
	}
	
	public java.lang.String getObdDloadChannel() {
		return this.obdDloadChannel;
	}
	public void setObaOsType(java.lang.String value) {
		this.obaOsType = value;
	}
	
	public java.lang.String getObaOsType() {
		return this.obaOsType;
	}
	public void setObaOsName(java.lang.String value) {
		this.obaOsName = value;
	}
	
	public java.lang.String getObaOsName() {
		return this.obaOsName;
	}
	public void setObaDeviceModel(java.lang.String value) {
		this.obaDeviceModel = value;
	}
	
	public java.lang.String getObaDeviceModel() {
		return this.obaDeviceModel;
	}
	public void setObaDeviceId(java.lang.String value) {
		this.obaDeviceId = value;
	}
	
	public java.lang.String getObaDeviceId() {
		return this.obaDeviceId;
	}
	public void setObaDeviceMemory(java.lang.Integer value) {
		this.obaDeviceMemory = value;
	}
	
	public java.lang.Integer getObaDeviceMemory() {
		return this.obaDeviceMemory;
	}
	public void setObaDeviceNet(java.lang.String value) {
		this.obaDeviceNet = value;
	}
	
	public java.lang.String getObaDeviceNet() {
		return this.obaDeviceNet;
	}
	public void setObaDeviceResolution(java.lang.String value) {
		this.obaDeviceResolution = value;
	}
	
	public java.lang.String getObaDeviceResolution() {
		return this.obaDeviceResolution;
	}
	public void setObaTelcos(java.lang.String value) {
		this.obaTelcos = value;
	}
	
	public java.lang.String getObaTelcos() {
		return this.obaTelcos;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getOblId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof OperationBaseLoginDetail == false) return false;
		if(this == obj) return true;
		OperationBaseLoginDetail other = (OperationBaseLoginDetail)obj;
		return new EqualsBuilder()
			.append(getOblId(),other.getOblId())
			.isEquals();
	}
	
}

