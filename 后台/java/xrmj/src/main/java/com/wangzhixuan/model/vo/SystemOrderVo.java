package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 订单Vo对象
 * @description：SystemOrderVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class SystemOrderVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 购买渠道(1=线下购买 2=APP内购 3=网页购买 4=公众号购买)
     */
	private Integer purchaseType;
    /**
     * 订单编号
     */
	private String orderNo;
    /**
     * Gm操作人姓名
     */
	private String name;
    /**
     * gm操作人
     */
	private Integer userId;
	/**
	 * 玩家昵称
	 */
	private String nick;
    /**
     * 玩家明星号
     */
	private String startNo;
    /**
     * 支付金额
     */
	private BigDecimal payPrice;
    /**
     * 支付方式
     */
	private Integer payType;
    /**
     * 付款状态
     */
	private Integer payStatus;
    /**
     * 付款时间
     */
	private Date payTime;
    /**
     * 房卡单价
     */
	private BigDecimal roomcardPrice;
    /**
     * 房卡数量
     */
	private Integer roomcardAmount;
    /**
     * 创建时间
     */
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(Integer purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public BigDecimal getRoomcardPrice() {
		return roomcardPrice;
	}

	public void setRoomcardPrice(BigDecimal roomcardPrice) {
		this.roomcardPrice = roomcardPrice;
	}

	public Integer getRoomcardAmount() {
		return roomcardAmount;
	}

	public void setRoomcardAmount(Integer roomcardAmount) {
		this.roomcardAmount = roomcardAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}