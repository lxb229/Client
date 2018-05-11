package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 系统订单
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@TableName("system_order")
public class SystemOrder extends Model<SystemOrder> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 购买渠道(1=线下购买 2=APP内购 3=网页购买 4=公众号购买)
     */
	@TableField("purchase_type")
	private Integer purchaseType;
    /**
     * 订单编号
     */
	@TableField("order_no")
	private String orderNo;
    /**
     * gm操作人
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 玩家明星号
     */
	@TableField("start_no")
	private String startNo;
    /**
     * 支付金额
     */
	@TableField("pay_price")
	private BigDecimal payPrice;
    /**
     * 支付方式(1=APP内购 2=支付宝 3=微信 4=银行转账)
     */
	@TableField("pay_type")
	private Integer payType;
    /**
     * 付款状态
     */
	@TableField("pay_status")
	private Integer payStatus;
    /**
     * 付款时间
     */
	@TableField("pay_time")
	private Date payTime;
    /**
     * 房卡单价
     */
	@TableField("roomcard_price")
	private BigDecimal roomcardPrice;
    /**
     * 房卡数量
     */
	@TableField("roomcard_amount")
	private Integer roomcardAmount;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
	
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		if(id == null){
			 if(StringUtils.isBlank(startNo)){
				 vb.setMsg("订单-玩家明星号为空");
		         return vb;
			 }
			 if(purchaseType == null) {
				 vb.setMsg("购买渠道为空");
		         return vb;
			 }
			 if(StringUtils.isBlank(orderNo)) {
				 vb.setMsg("订单号为空");
		         return vb;
			 }
			 if(userId == null) {
				 vb.setMsg("GM操作人为空");
		         return vb; 
			 }
			 if(payPrice == null) {
				 vb.setMsg("付款金额为空");
		         return vb; 
			 }
			 if(roomcardAmount == null) {
				 vb.setMsg("购买房卡数量为空");
		         return vb; 
			 }
			 
		}
		vb.setFlag(true);
	    return vb; 
	}


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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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
	protected Serializable pkVal() {
		return this.id;
	}

}
