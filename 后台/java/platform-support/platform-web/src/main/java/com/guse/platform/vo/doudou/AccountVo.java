package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * SystemLuckVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class AccountVo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5040172179760343330L;
	
	//columns START
	/** 
	 * 公告操作命令码()
	 * cmd=1 冻结或解冻帐号.
	 * cmd=2 修改帐号类型
	 * cmd=3 修改绑定的手机号
	 * cmd=4 修改实名认证信息
	 * cmd=5 增加或减少房卡
	 * cmd=6 设置玩家幸运值
	 * cmd=7 赠送房卡
	 */
	private Integer cmd ;
	/** 玩家id */
	private String starNo;
	/** 状态(0=正常,-1=冻结) */
	private Integer state;
	/** 帐号类型(0=一般玩家,1=代理) */
	private Integer type;
	/** 修改后的电话 */
	private String phone ;
	/** 微信号码*/
	private java.lang.String wxNO;
	/** 名称 */
	private String name;
	/** 身份证 */
	private String cardId;
	/**
	 * 房卡(大于0=加房卡,小于0=减少房卡)
	 */
	private Integer money;
	/**
	 * 幸运值(百分比整数)
	 */
	private Integer luck;
	/**
	 * 幸运开始时间
	 */
	private Long luckStart;
	/**
	 * 幸运到期时间(毫秒时间)
	 */
	private Long luckEnd;
	/**
	 * 幸运总次数
	 */
	private Integer luckNum;
	/**
	 * 赠送房卡数量
	 */
	private Integer scAmount;
	/**
	 * 房卡赠送人
	 */
	private String starNO1;
	/**
	 * 房卡接收人
	 */
	private String starNO2;
	//columns END

	public Integer getCmd() {
		return cmd;
	}

	public void setCmd(Integer cmd) {
		this.cmd = cmd;
	}

	public String getStarNo() {
		return starNo;
	}

	public void setStarNo(String starNo) {
		this.starNo = starNo;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public java.lang.String getWxNO() {
		return wxNO;
	}

	public void setWxNO(java.lang.String wxNO) {
		this.wxNO = wxNO;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getLuck() {
		return luck;
	}

	public void setLuck(Integer luck) {
		this.luck = luck;
	}

	public Long getLuckStart() {
		return luckStart;
	}

	public void setLuckStart(Long luckStart) {
		this.luckStart = luckStart;
	}

	public Long getLuckEnd() {
		return luckEnd;
	}

	public void setLuckEnd(Long luckEnd) {
		this.luckEnd = luckEnd;
	}

	public Integer getLuckNum() {
		return luckNum;
	}

	public void setLuckNum(Integer luckNum) {
		this.luckNum = luckNum;
	}

	public Integer getScAmount() {
		return scAmount;
	}

	public void setScAmount(Integer scAmount) {
		this.scAmount = scAmount;
	}

	public String getStarNO1() {
		return starNO1;
	}

	public void setStarNO1(String starNO1) {
		this.starNO1 = starNO1;
	}

	public String getStarNO2() {
		return starNO2;
	}

	public void setStarNO2(String starNO2) {
		this.starNO2 = starNO2;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getStarNo())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AccountVo == false) return false;
		if(this == obj) return true;
		AccountVo other = (AccountVo)obj;
		return new EqualsBuilder()
			.append(getStarNo(),other.getStarNo())
			.isEquals();
	}
	
}

