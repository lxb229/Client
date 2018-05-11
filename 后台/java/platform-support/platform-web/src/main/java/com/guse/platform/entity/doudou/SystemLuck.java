package com.guse.platform.entity.doudou;

import java.util.Calendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.utils.DateUtils;
import com.guse.platform.entity.system.Users;


/**
 * 
 * 
 * system_luck
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemLuck implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 表主键id */
	private java.lang.Integer id;
	/** 玩家id */
	private java.lang.Integer userId;
	/** 幸运值 */
	private java.lang.Integer luck;
	/** 幸运开始时间 */
	private java.util.Date luckStart;
	/** 幸运结束时间 */
	private java.util.Date luckEnd;
	/** 幸运总次数 */
	private java.lang.Integer luckCount;
	/** 幸运剩余次数 */
	private java.lang.Integer luckRemain;
	/** 幸运状态 */
	private java.lang.Integer luckState;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 创建人 */
	private java.lang.Integer createId;
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
		 if(userId == null){
			 vb.setMsg("请填写玩家id！");
	         return vb;
		 }
		 if(luck == null) {
			 vb.setMsg("请填写幸运值！");
	         return vb;
		 }
		 if(luckStart == null) {
			 vb.setMsg("请填写开始时间！");
	         return vb;
		 }
		 if(luckEnd == null) {
			 vb.setMsg("请填写截止时间！");
	         return vb;
		 }
		 if(luckCount == null) {
			 vb.setMsg("请填写幸运次数！");
	         return vb;
		 }
		 if(luck > 100 || luck < 0) {
			 vb.setMsg("幸运值在0-100！");
	         return vb;
		 }
		 if(luckCount > 1000 || luckCount < 0) {
			 vb.setMsg("幸运次数在0-1000！");
	         return vb;
		 }
		 if(DateUtils.getDaysByDate(luckStart, luckEnd, Calendar.DATE) > 90) {
			 vb.setMsg("幸运天数不能超过90天");
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
	public void setUserId(java.lang.Integer value) {
		this.userId = value;
	}
	
	public java.lang.Integer getUserId() {
		return this.userId;
	}
	public void setLuck(java.lang.Integer value) {
		this.luck = value;
	}
	
	public java.lang.Integer getLuck() {
		return this.luck;
	}
	public void setLuckStart(java.util.Date value) {
		this.luckStart = value;
	}
	
	public java.util.Date getLuckStart() {
		return this.luckStart;
	}
	public void setLuckEnd(java.util.Date value) {
		this.luckEnd = value;
	}
	
	public java.util.Date getLuckEnd() {
		return this.luckEnd;
	}
	public void setLuckCount(java.lang.Integer value) {
		this.luckCount = value;
	}
	
	public java.lang.Integer getLuckCount() {
		return this.luckCount;
	}
	public void setLuckState(java.lang.Integer value) {
		this.luckState = value;
	}
	
	public void setLuckRemain(java.lang.Integer value) {
		this.luckRemain = value;
	}
	
	public java.lang.Integer getLuckRemain() {
		return this.luckRemain;
	}
	
	public java.lang.Integer getLuckState() {
		return this.luckState;
	}
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	public void setCreateId(java.lang.Integer value) {
		this.createId = value;
	}
	
	public java.lang.Integer getCreateId() {
		return this.createId;
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
		if(obj instanceof SystemLuck == false) return false;
		if(this == obj) return true;
		SystemLuck other = (SystemLuck)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
}

