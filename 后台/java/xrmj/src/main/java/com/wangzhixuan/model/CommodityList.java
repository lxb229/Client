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
 * 库存商品列表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-09
 */
@TableName("commodity_list")
public class CommodityList extends Model<CommodityList> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 入库单
     */
	@TableField("warehouse_in")
	private Integer warehouseIn;
    /**
     * 出库单
     */
	@TableField("warehouse_out")
	private Integer warehouseOut;
    /**
     * 商品
     */
	private Integer commodity;
    /**
     * 供应商
     */
	private Integer supplier;
    /**
     * 卡号
     */
	@TableField("card_no")
	private String cardNo;
    /**
     * 密钥
     */
	@TableField("secret_key")
	private String secretKey;
    /**
     * 商品状态(0=有效 1=无效)
     */
	private Integer status;
    /**
     * 兑换码
     */
	@TableField("redeem_code")
	private String redeemCode;
    /**
     * 使用状态(0=未使用 1=冻结中 2=已使用)
     */
	@TableField("use_status")
	private Integer useStatus;
    /**
     * 入库时间
     */
	@TableField("create_time")
	private Date createTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWarehouseIn() {
		return warehouseIn;
	}

	public void setWarehouseIn(Integer warehouseIn) {
		this.warehouseIn = warehouseIn;
	}

	public Integer getWarehouseOut() {
		return warehouseOut;
	}

	public void setWarehouseOut(Integer warehouseOut) {
		this.warehouseOut = warehouseOut;
	}

	public Integer getCommodity() {
		return commodity;
	}

	public void setCommodity(Integer commodity) {
		this.commodity = commodity;
	}

	public Integer getSupplier() {
		return supplier;
	}

	public void setSupplier(Integer supplier) {
		this.supplier = supplier;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRedeemCode() {
		return redeemCode;
	}

	public void setRedeemCode(String redeemCode) {
		this.redeemCode = redeemCode;
	}

	public Integer getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
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
