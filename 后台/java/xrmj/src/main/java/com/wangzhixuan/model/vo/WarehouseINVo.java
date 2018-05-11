package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 红包领取返回对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class WarehouseINVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
     * 表主键id
     */
	private Integer id;
    /**
     * 入库单号
     */
	private String inNo;
    /**
     * 供应商
     */
	private Integer supplier;
    /**
     * 供应商名字
     */
	private String supplierName;
    /**
     * 商品
     */
	private Integer commodity;
    /**
     * 商品
     */
	private String commodityName;
    /**
     * 入库类型(0= 正常入库 1=入库冲账)
     */
	private Integer type;
    /**
     * 入库冲账对应入库
     */
	private Integer warehouseIn;
	/**
	 * 入库冲账对应入库单号
	 */
	private String warehouseInNo;
    /**
     * 入库数量
     */
	private Integer amount;
    /**
     * 价格
     */
	private BigDecimal priceAmount;
    /**
     * 备注
     */
	private String remark;
    /**
     * 入库人
     */
	private Integer createId;
    /**
     * 入库人姓名
     */
	private String createName;
    /**
     * 入库时间
     */
	private Date createTime;
    /**
     * 修改人
     */
	private Integer updateId;
    /**
     * 修改人
     */
	private String updateName;
    /**
     * 修改时间
     */
	private Date updateTime;
	/**
     * 卡号
     */
	private String cardNo;
	/**
     * 密钥
     */
	private String secretKey;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInNo() {
		return inNo;
	}

	public void setInNo(String inNo) {
		this.inNo = inNo;
	}

	public Integer getSupplier() {
		return supplier;
	}

	public void setSupplier(Integer supplier) {
		this.supplier = supplier;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getWarehouseIn() {
		return warehouseIn;
	}

	public void setWarehouseIn(Integer warehouseIn) {
		this.warehouseIn = warehouseIn;
	}

	public String getWarehouseInNo() {
		return warehouseInNo;
	}

	public void setWarehouseInNo(String warehouseInNo) {
		this.warehouseInNo = warehouseInNo;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public BigDecimal getPriceAmount() {
		return priceAmount;
	}

	public void setPriceAmount(BigDecimal priceAmount) {
		this.priceAmount = priceAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}