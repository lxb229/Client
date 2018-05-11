package com.guse.platform.entity.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.entity.system.Users;



/**
 * 
 * 
 * system_agency
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemAgency implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/**  */
	private java.lang.Integer saId;
	/** 用户id */
	private java.lang.Integer saUserId;
	/** 城市区域id */
	private java.lang.Integer saCityId;
	/** 代理开始时间 */
	private java.util.Date agencyStart;
	/** 代理结束时间 */
	private java.util.Date agencyEnd;
	/** 代理状态 1：启用 0：禁用 */
	private java.lang.Integer agencyState;
	/** 成为代理时间 */
	private java.util.Date saCreateTime;
	/** 小于10万提成比例 */
	private java.lang.Double saLessOne;
	/** 10万到20万提成比例 */
	private java.lang.Double saOneToTwo;
	/** 20万到30万提成比例 */
	private java.lang.Double saTwoToThree;
	/** 30万到40万提成比例 */
	private java.lang.Double saThreeToFour;
	/** 超过40万提成比例 */
	private java.lang.Double saGreaterFoure;
	//columns END
	
	private String seacherParm;
	
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

	public String getSeacherParm() {
		return seacherParm;
	}

	public void setSeacherParm(String seacherParm) {
		this.seacherParm = seacherParm;
	}

	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(saCityId == null){
			 vb.setMsg("请选择代理区域！");
	         return vb;
		 }
		 if(saUserId == null){
			 vb.setMsg("请输入代理id！");
	         return vb;
		 }
		 if(saLessOne == null){
			 vb.setMsg("请设置小于10万提成比例！");
	         return vb;
		 }
		 if(saOneToTwo == null){
			 vb.setMsg("请设置10万到20万提成比例！");
	         return vb;
		 }
		 if(saTwoToThree == null){
			 vb.setMsg("请设置20万到30万提成比例！");
	         return vb;
		 }
		 if(saThreeToFour == null){
			 vb.setMsg("请设置30万到40万提成比例！");
	         return vb;
		 }
		 if(saGreaterFoure == null){
			 vb.setMsg("请设置超过40万提成比例！");
	         return vb;
		 }
		 if(agencyStart.getTime() > agencyEnd.getTime()) {
			 vb.setMsg("请设置正确的代理时间");
	         return vb; 
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	
	public void setSaId(java.lang.Integer value) {
		this.saId = value;
	}
	
	public java.lang.Integer getSaId() {
		return this.saId;
	}
	public void setSaUserId(java.lang.Integer value) {
		this.saUserId = value;
	}
	
	public java.lang.Integer getSaUserId() {
		return this.saUserId;
	}
	public void setSaCityId(java.lang.Integer value) {
		this.saCityId = value;
	}
	
	public java.lang.Integer getSaCityId() {
		return this.saCityId;
	}
	public java.util.Date getAgencyStart() {
		return agencyStart;
	}

	public void setAgencyStart(java.util.Date agencyStart) {
		this.agencyStart = agencyStart;
	}

	public java.util.Date getAgencyEnd() {
		return agencyEnd;
	}

	public void setAgencyEnd(java.util.Date agencyEnd) {
		this.agencyEnd = agencyEnd;
	}

	public java.lang.Integer getAgencyState() {
		return agencyState;
	}

	public void setAgencyState(java.lang.Integer agencyState) {
		this.agencyState = agencyState;
	}

	public void setSaCreateTime(java.util.Date value) {
		this.saCreateTime = value;
	}
	
	public java.util.Date getSaCreateTime() {
		return this.saCreateTime;
	}
	public void setSaLessOne(java.lang.Double value) {
		this.saLessOne = value;
	}
	
	public java.lang.Double getSaLessOne() {
		return this.saLessOne;
	}
	public void setSaOneToTwo(java.lang.Double value) {
		this.saOneToTwo = value;
	}
	
	public java.lang.Double getSaOneToTwo() {
		return this.saOneToTwo;
	}
	public void setSaTwoToThree(java.lang.Double value) {
		this.saTwoToThree = value;
	}
	
	public java.lang.Double getSaTwoToThree() {
		return this.saTwoToThree;
	}
	public void setSaThreeToFour(java.lang.Double value) {
		this.saThreeToFour = value;
	}
	
	public java.lang.Double getSaThreeToFour() {
		return this.saThreeToFour;
	}
	public void setSaGreaterFoure(java.lang.Double value) {
		this.saGreaterFoure = value;
	}
	
	public java.lang.Double getSaGreaterFoure() {
		return this.saGreaterFoure;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSaId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemAgency == false) return false;
		if(this == obj) return true;
		SystemAgency other = (SystemAgency)obj;
		return new EqualsBuilder()
			.append(getSaId(),other.getSaId())
			.isEquals();
	}
	
}

