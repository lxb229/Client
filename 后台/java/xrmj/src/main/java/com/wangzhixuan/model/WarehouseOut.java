package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 出库单
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-10
 */
@TableName("warehouse_out")
public class WarehouseOut extends Model<WarehouseOut> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 出库单号
     */
	@TableField("out_no")
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
     * 出库数量
     */
	private Integer amount;
    /**
     * 冲账出库对应出库单
     */
	@TableField("warehouse_out")
	private Integer warehouseOut;
    /**
     * 备注
     */
	private String remark;
    /**
     * GM操作人
     */
	@TableField("user_id")
	private Integer userId;
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
			 if(type == 0 && amount <= 0){
				 vb.setMsg("正常出库数量必须大于0");
		         return vb;
			 } else if(type == 1 && amount >= 0) {
				 vb.setMsg("出库冲账数量必须小于0");
		         return vb; 
			 } else if(type == 2 && amount <= 0) {
				 vb.setMsg("抽奖出库数量必须大于0");
		         return vb;  
			 } else if(type == 3 && amount <= 0) {
				 vb.setMsg("兑换出库数量必须大于0");
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
