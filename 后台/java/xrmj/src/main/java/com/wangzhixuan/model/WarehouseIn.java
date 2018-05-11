package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 入库单
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@TableName("warehouse_in")
public class WarehouseIn extends Model<WarehouseIn> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 入库单号
     */
	@TableField("in_no")
	private String inNo;
    /**
     * 供应商
     */
	private Integer supplier;
    /**
     * 商品
     */
	private Integer commodity;
    /**
     * 入库类型(0= 正常入库 1=入库冲账)
     */
	private Integer type;
    /**
     * 入库冲账对应入库
     */
	@TableField("warehouse_in")
	private Integer warehouseIn;
    /**
     * 入库数量
     */
	private Integer amount;
    /**
     * 价格
     */
	@TableField("price_amount")
	private BigDecimal priceAmount;
    /**
     * 备注
     */
	private String remark;
    /**
     * 入库人
     */
	@TableField("create_id")
	private Integer createId;
    /**
     * 入库时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 修改人
     */
	@TableField("update_id")
	private Integer updateId;
    /**
     * 修改时间
     */
	@TableField("update_time")
	private Date updateTime;
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		if(id == null){
			 if(type == 0 && amount <= 0){
				 vb.setMsg("正常入库数量必须大于0");
		         return vb;
			 } else if(type == 1 && amount >= 0) {
				 vb.setMsg("入库冲账数量必须小于0");
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

	public Integer getCommodity() {
		return commodity;
	}

	public void setCommodity(Integer commodity) {
		this.commodity = commodity;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
