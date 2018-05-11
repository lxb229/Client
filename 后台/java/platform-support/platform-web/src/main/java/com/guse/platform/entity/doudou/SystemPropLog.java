package com.guse.platform.entity.doudou;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.entity.system.Users;


/**
 * 
 * 
 * system_prop_log
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class SystemPropLog implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 增减道具ID */
	private java.lang.Integer splId;
	/** 道具增减数量 */
	private java.lang.Integer splAmount;
	/** 操作人 */
	private java.lang.Integer splOprtuser;
	/**  */
	private java.util.Date splTime;
	/**  */
	private java.lang.String splContent;
	/** 操作类型 1：购买 2：发放 3：扣除 4：赠送 5：游戏消耗 */
	private java.lang.Integer splType;
	/** 生成时间 */
	private Date createTime;
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(splOprtuser == null){
			 vb.setMsg("操作人为空！");
	         return vb;
		 }
		 if(splAmount == null){
			 vb.setMsg("增减数量为空！");
	         return vb;
		 }
		 if(splType == null){
			 vb.setMsg("操作类型为空！");
	         return vb;
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	
	//columns END
	
	private Users oprtuser;
	
	public Users getOprtuser() {
		return oprtuser;
	}

	public void setOprtuser(Users oprtuser) {
		this.oprtuser = oprtuser;
	}

	public void setSplId(java.lang.Integer value) {
		this.splId = value;
	}
	
	public java.lang.Integer getSplId() {
		return this.splId;
	}
	public void setSplAmount(java.lang.Integer value) {
		this.splAmount = value;
	}
	
	public java.lang.Integer getSplAmount() {
		return this.splAmount;
	}
	public void setSplOprtuser(java.lang.Integer value) {
		this.splOprtuser = value;
	}
	
	public java.lang.Integer getSplOprtuser() {
		return this.splOprtuser;
	}
	public void setSplTime(java.util.Date value) {
		this.splTime = value;
	}
	
	public java.util.Date getSplTime() {
		return this.splTime;
	}
	public void setSplContent(java.lang.String value) {
		this.splContent = value;
	}
	
	public java.lang.String getSplContent() {
		return this.splContent;
	}
	public void setSplType(java.lang.Integer value) {
		this.splType = value;
	}
	
	public java.lang.Integer getSplType() {
		return this.splType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSplId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SystemPropLog == false) return false;
		if(this == obj) return true;
		SystemPropLog other = (SystemPropLog)obj;
		return new EqualsBuilder()
			.append(getSplId(),other.getSplId())
			.isEquals();
	}
	
}

