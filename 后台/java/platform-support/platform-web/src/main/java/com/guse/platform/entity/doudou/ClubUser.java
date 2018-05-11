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
 * club_user
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class ClubUser implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer cuId;
	/** 俱乐部id */
	private java.lang.Integer clubId;
	/** 人员id */
	private java.lang.Integer cuUserId;
	/** 人员类型 1：群主 0：普通群员 */
	private java.lang.String cuType;
	/** 加入俱乐部时间 */
	private java.util.Date cuCreateTime;
	/** 当前状态 1：有效 0：退出 */
	private java.lang.String cuState;
	//columns END
	
	private Users users;

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		if(cuId == null){
			 if(clubId == null){
				 vb.setMsg("请填写俱乐部编号！");
		         return vb;
			 }
			 if(cuUserId == null){
				 vb.setMsg("请填写俱乐部玩家！");
		         return vb;
			 }
		}
		vb.setFlag(true);
	    return vb; 
	}
	
	public void setCuId(java.lang.Integer value) {
		this.cuId = value;
	}
	
	public java.lang.Integer getCuId() {
		return this.cuId;
	}
	public void setClubId(java.lang.Integer value) {
		this.clubId = value;
	}
	
	public java.lang.Integer getClubId() {
		return this.clubId;
	}
	public void setCuUserId(java.lang.Integer value) {
		this.cuUserId = value;
	}
	
	public java.lang.Integer getCuUserId() {
		return this.cuUserId;
	}
	public void setCuType(java.lang.String value) {
		this.cuType = value;
	}
	
	public java.lang.String getCuType() {
		return this.cuType;
	}
	public void setCuCreateTime(java.util.Date value) {
		this.cuCreateTime = value;
	}
	
	public java.util.Date getCuCreateTime() {
		return this.cuCreateTime;
	}
	public void setCuState(java.lang.String value) {
		this.cuState = value;
	}
	
	public java.lang.String getCuState() {
		return this.cuState;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getCuId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ClubUser == false) return false;
		if(this == obj) return true;
		ClubUser other = (ClubUser)obj;
		return new EqualsBuilder()
			.append(getCuId(),other.getCuId())
			.isEquals();
	}
	
}

