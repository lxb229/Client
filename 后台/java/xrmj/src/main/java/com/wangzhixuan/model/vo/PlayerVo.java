package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;

import java.io.Serializable;

/**
 * <p>
 * 玩家表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public class PlayerVo implements Serializable {

    private static final long serialVersionUID = 1L;
	
	 /**
     * 玩家明星号
     */
	private String starNo;
    /**
     * 头像
     */
	private String headImg;
    /**
     * 性别
     */
	private Integer sex;
    /**
     * 玩家昵称
     */
	private String nick;
    /**
     * 玩家账号状态(0=正常 1=冻结)
     */
	private Integer status;
    /**
     * 注册ip
     */
	private String createIp;
    /**
     * 账号类型(0=普通玩家 1=群主)
     */
	private Integer type;
    /**
     * 电话号码
     */
	private String phone;
    /**
     * 真实姓名
     */
	private String realName;
    /**
     * 身份证号码
     */
	private String cardNo;
    /**
     * 注册时间
     */
	private Long createTime;

	public String getStarNo() {
		return starNo;
	}

	public void setStarNo(String starNo) {
		this.starNo = starNo;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
