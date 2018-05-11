package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 出库单Vo对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class WarehouseOutVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
     * 表主键id
     */
	private Integer id;
    /**
     * 出库单号
     */
	private String outNo;
    /**
     * 出库类型(0=正常出库 1=出库冲账 2=抽奖出库 3=兑换出库)
     */
	private Integer type;
    /**
     * 出库商品
     */
	private Integer commodity;
	/**
	 * 商品名称
	 */
	private String commodityName;
    /**
     * 出库数量
     */
	private Integer amount;
    /**
     * 冲账出库对应出库单
     */
	private Integer warehouseOut;
	/**
	 * 冲账出库对应出库单号
	 */
	private String warehouseOutNo;
    /**
     * 备注
     */
	private String remark;
    /**
     * GM操作人
     */
	private Integer userId;
	/**
	 * GM操作人姓名
	 */
	private String userName;
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

	public String getOutNo() {
		return outNo;
	}

	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCommodity() {
		return commodity;
	}

	public void setCommodity(Integer commodity) {
		this.commodity = commodity;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getWarehouseOut() {
		return warehouseOut;
	}

	public void setWarehouseOut(Integer warehouseOut) {
		this.warehouseOut = warehouseOut;
	}

	public String getWarehouseOutNo() {
		return warehouseOutNo;
	}

	public void setWarehouseOutNo(String warehouseOutNo) {
		this.warehouseOutNo = warehouseOutNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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