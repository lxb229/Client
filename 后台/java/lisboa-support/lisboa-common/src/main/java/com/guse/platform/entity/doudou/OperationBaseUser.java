package com.guse.platform.entity.doudou;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.guse.platform.utils.QueryBean;
import com.guse.platform.utils.date.DateUtils;


/**
 * 
 * 
 * operation_base_user
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class OperationBaseUser extends QueryBean implements java.io.Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	//columns START
	/** 游戏ID */
	private java.lang.Long obuUserid;
	/** 登录密码 */
	private java.lang.String obuPassword;
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
	/** 注册IP */
	private java.lang.String obuRegisterIp;
	/** 注册时间 */
	private java.util.Date obuRegisterTime;
	/** 游戏账号(手机号) */
	private java.lang.String obuUsename;
	/** 游戏昵称 */
	private java.lang.String obuUserNick;
	/** 用户等级 */
	private java.lang.Integer obuLevel;
	/** 用户性别 */
	private java.lang.Integer obuSex;
	/** 魅力值 */
	private java.lang.Integer obuCharm;
	/** 头像 */
	private java.lang.String obuHead;
	/** 账号状态：正常=1、冻结=0 */
	private java.lang.Integer obuState;
	/** 活跃状态：正常、死号、回流 */
	private java.lang.Integer obuBriskState;
	/** 是否破产;1=破产；非1=不破产 */
	private java.lang.Integer obuIsGoBroke;
	/** 个性签名 */
	private java.lang.String obuSign;
	/** 会员卡天数 */
	private java.lang.Integer obuVipDays;
	/** 持有钻石 */
	private java.lang.Integer obuDiamond;
	/** 持有金币 */
	private java.lang.Long obuGold;
	/** 游戏总局数 */
	private java.lang.Integer obuInningsNum;
	/** 用户首次充值时间 首次对游戏进行付费行为的时间 */
	private java.util.Date obuFirstRechargeTime;
	/** 用户充值总次数记录 */
	private java.lang.Integer obuRechargeCount;
	/** 用户充值金额总和 */
	private java.lang.Long obuRechargeAmount;
	/** 最后登录时间 */
	private java.util.Date obuLastLoginTime;
	/** 最后登录IP */
	private java.lang.String obuLastLoginIp;
	/** 最后登录地域 */
	private java.lang.String obuLastLoginRegion;
	/** 是否机器人 */
	private java.lang.Integer obuIsRobots;
	/** 是否游客 */
	private java.lang.Integer obuIsTourist;
	
	//纠正时间
	private Date correctTime;
	
	//当前所在位置(游戏)
	private int oboInGame;
	
	//当前玩家所在房间（当玩家玩游戏时）
	private int oboRoom;
	//columns END
	
	/** 今日充值总金额*/
	private String todayTotalAmount; 
	
	private String vip;
	
	//历史排行用到的属性
	private Long todayRechargeAmount;//今日充值金额
	private Long sevenRechargeAmount;//近七天充值金额
	private Long notRechargeDay;//未充值天数
	private Long notLonginDay;//未登录天数
	 //sql条件
	private Date longinTimeStart;//登录时间条件开始
	private Date longinTimeEnd;//登录时间条件结束
	
	private java.lang.Long amountStart;
	private java.lang.Long amountEnd;
	
	public String getVip() {
		return vip;
	}
	public void setVip(String vip) {
		this.vip = vip;
	}
	public String getTodayTotalAmount() {
		return todayTotalAmount;
	}
	public void setTodayTotalAmount(String todayTotalAmount) {
		this.todayTotalAmount = todayTotalAmount;
	}
	public void setObuUserid(java.lang.Long value) {
		this.obuUserid = value;
	}
	
	public java.lang.Long getObuUserid() {
		return this.obuUserid;
	}
	public void setObuPassword(java.lang.String value) {
		this.obuPassword = value;
	}
	
	public java.lang.String getObuPassword() {
		return this.obuPassword;
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
	public void setObuRegisterIp(java.lang.String value) {
		this.obuRegisterIp = value;
	}
	public java.lang.String getObuRegisterIp() {
		return this.obuRegisterIp;
	}
	public void setObuRegisterTime(java.util.Date value) {
		this.obuRegisterTime = value;
	}
	public java.util.Date getObuRegisterTime() {
		return this.obuRegisterTime;
	}
	public void setObuUsename(java.lang.String value) {
		this.obuUsename = value;
	}
	
	public java.lang.String getObuUsename() {
		return this.obuUsename;
	}
	public void setObuUserNick(java.lang.String value) {
		this.obuUserNick = value;
	}
	public java.lang.String getObuUserNick() {
		return this.obuUserNick;
	}
	public void setObuLevel(java.lang.Integer value) {
		this.obuLevel = value;
	}
	public java.lang.Integer getObuLevel() {
		return this.obuLevel;
	}
	public void setObuSex(java.lang.Integer value) {
		this.obuSex = value;
	}
	
	public java.lang.Integer getObuSex() {
		return this.obuSex;
	}
	public void setObuCharm(java.lang.Integer value) {
		this.obuCharm = value;
	}
	public java.lang.Integer getObuCharm() {
		return this.obuCharm;
	}
	public void setObuHead(java.lang.String value) {
		this.obuHead = value;
	}
	public java.lang.String getObuHead() {
		return this.obuHead;
	}
	public void setObuState(java.lang.Integer value) {
		this.obuState = value;
	}
	public java.lang.Integer getObuState() {
		return this.obuState;
	}
	public void setObuBriskState(java.lang.Integer value) {
		this.obuBriskState = value;
	}
	
	public java.lang.Integer getObuBriskState() {
		return this.obuBriskState;
	}
	public void setObuIsGoBroke(java.lang.Integer value) {
		this.obuIsGoBroke = value;
	}
	public java.lang.Integer getObuIsGoBroke() {
		return this.obuIsGoBroke;
	}
	public void setObuSign(java.lang.String value) {
		this.obuSign = value;
	}
	
	public java.lang.String getObuSign() {
		return this.obuSign;
	}
	public void setObuVipDays(java.lang.Integer value) {
		this.obuVipDays = value;
	}
	public java.lang.Integer getObuVipDays() {
		return this.obuVipDays;
	}
	public void setObuDiamond(java.lang.Integer value) {
		this.obuDiamond = value;
	}
	public java.lang.Integer getObuDiamond() {
		return this.obuDiamond;
	}
	public void setObuGold(java.lang.Long value) {
		this.obuGold = value;
	}
	public java.lang.Long getObuGold() {
		return this.obuGold;
	}
	public void setObuInningsNum(java.lang.Integer value) {
		this.obuInningsNum = value;
	}
	public java.lang.Integer getObuInningsNum() {
		return this.obuInningsNum;
	}
	public void setObuFirstRechargeTime(java.util.Date value) {
		this.obuFirstRechargeTime = value;
	}
	
	public java.util.Date getObuFirstRechargeTime() {
		return this.obuFirstRechargeTime;
	}
	public void setObuRechargeCount(java.lang.Integer value) {
		this.obuRechargeCount = value;
	}
	
	public java.lang.Integer getObuRechargeCount() {
		return this.obuRechargeCount;
	}
	public void setObuRechargeAmount(java.lang.Long value) {
		this.obuRechargeAmount = value;
	}
	public java.lang.Long getObuRechargeAmount() {
		return this.obuRechargeAmount;
	}
	public void setObuLastLoginTime(java.util.Date value) {
		this.obuLastLoginTime = value;
	}
	public java.util.Date getObuLastLoginTime() {
		return this.obuLastLoginTime;
	}
	public void setObuLastLoginIp(java.lang.String value) {
		this.obuLastLoginIp = value;
	}
	public java.lang.String getObuLastLoginIp() {
		return this.obuLastLoginIp;
	}
	public void setObuLastLoginRegion(java.lang.String value) {
		this.obuLastLoginRegion = value;
	}
	public java.lang.String getObuLastLoginRegion() {
		return this.obuLastLoginRegion;
	}
	public void setObuIsRobots(java.lang.Integer value) {
		this.obuIsRobots = value;
	}
	public java.lang.Integer getObuIsRobots() {
		return this.obuIsRobots;
	}
	public void setObuIsTourist(java.lang.Integer value) {
		this.obuIsTourist = value;
	}
	public java.lang.Integer getObuIsTourist() {
		return this.obuIsTourist;
	}
	public int getOboInGame() {
		return oboInGame;
	}
	public void setOboInGame(int oboInGame) {
		this.oboInGame = oboInGame;
	}
	public int getOboRoom() {
		return oboRoom;
	}
	public void setOboRoom(int oboRoom) {
		this.oboRoom = oboRoom;
	}
	public Date getCorrectTime() {
		return correctTime;
	}
	public void setCorrectTime(Date correctTime) {
		this.correctTime = correctTime;
	}
	public Long getTodayRechargeAmount() {
		return todayRechargeAmount;
	}
	public void setTodayRechargeAmount(Long todayRechargeAmount) {
		this.todayRechargeAmount = todayRechargeAmount;
	}
	public Long getSevenRechargeAmount() {
		return sevenRechargeAmount;
	}
	public void setSevenRechargeAmount(Long sevenRechargeAmount) {
		this.sevenRechargeAmount = sevenRechargeAmount;
	}
	public Long getNotRechargeDay() {
		return notRechargeDay;
	}
	public void setNotRechargeDay(Long notRechargeDay) {
		this.notRechargeDay = notRechargeDay;
	}
	public Long getNotLonginDay() {
		return notLonginDay;
	}
	public void setNotLonginDay(Long notLonginDay) {
		this.notLonginDay = notLonginDay;
	}
	public Date getLonginTimeStart() {
		return longinTimeStart;
	}
	public void setLonginTimeStart(Date longinTimeStart) {
		this.longinTimeStart = longinTimeStart;
	}
	public Date getLonginTimeEnd() {
		return longinTimeEnd;
	}
	public void setLonginTimeEnd(Date longinTimeEnd) {
		if(longinTimeEnd!=null){
			longinTimeEnd = DateUtils.StrToDate(DateUtils.DateToStr(longinTimeEnd, DateUtils.format)+" 23:59:59", DateUtils.sdf);
		}
		this.longinTimeEnd = longinTimeEnd;
	}
	public java.lang.Long getAmountStart() {
		return amountStart;
	}
	public void setAmountStart(java.lang.Long amountStart) {
		this.amountStart = amountStart;
	}
	public java.lang.Long getAmountEnd() {
		return amountEnd;
	}
	public void setAmountEnd(java.lang.Long amountEnd) {
		this.amountEnd = amountEnd;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getObuUserid())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof OperationBaseUser == false) return false;
		if(this == obj) return true;
		OperationBaseUser other = (OperationBaseUser)obj;
		return new EqualsBuilder()
			.append(getObuUserid(),other.getObuUserid())
			.isEquals();
	}
	
}

