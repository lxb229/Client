package com.guse.platform.vo.system;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;



/**
 * MenusVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class AccountVo implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3149308570011558182L;
	
	//columns START
	//明星号
	private String starNO;
	//角色呢称
	private String nick;
	//状态(0=正常,-1=冻结)
	private int state;
	//创建时间
	private String createTime;
	//角色房卡
	private int roomCard;
	//帮定的手机号
	private String phone;
	//最后在线时间
	private String lastTime;
	// 最后在线时间
	private Date theLastTime;
	// 增减金币操作 1：增加金币 -1：减少金币
	private int jbState;
	// 增减金币数量
	private int jbAmount;
	// 登录密码
	private String loginPass;
	//columns END
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public String getStarNO() {
		return starNO;
	}

	public void setStarNO(String starNO) {
		this.starNO = starNO;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getRoomCard() {
		return roomCard;
	}

	public void setRoomCard(int roomCard) {
		this.roomCard = roomCard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public Date getTheLastTime() {
		return theLastTime;
	}

	public void setTheLastTime(Date theLastTime) {
		this.theLastTime = theLastTime;
	}

	public int getJbState() {
		return jbState;
	}

	public void setJbState(int jbState) {
		this.jbState = jbState;
	}

	public int getJbAmount() {
		return jbAmount;
	}

	public void setJbAmount(int jbAmount) {
		this.jbAmount = jbAmount;
	}

	public String getLoginPass() {
		return loginPass;
	}

	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getStarNO())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AccountVo == false) return false;
		if(this == obj) return true;
		AccountVo other = (AccountVo)obj;
		return new EqualsBuilder()
			.append(getStarNO(),other.getStarNO())
			.isEquals();
	}
	
}

