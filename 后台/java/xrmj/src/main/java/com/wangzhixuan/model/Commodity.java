package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public class Commodity extends Model<Commodity> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 商品编号
     */
	@TableField("commodity_no")
	private String commodityNo;
    /**
     * 条形码
     */
	@TableField("bar_code")
	private String barCode;
    /**
     * 商品类型
     */
	@TableField("commodity_type")
	private Integer commodityType;
    /**
     * 商品名称
     */
	@TableField("commodity_name")
	private String commodityName;
    /**
     * 商品规格
     */
	private String specification;
    /**
     * 商品采购价格
     */
	private BigDecimal price;
    /**
     * 库存预警
     */
	private Integer alarm;
    /**
     * 产地
     */
	private String origin;
    /**
     * 品牌
     */
	private String brand;
    /**
     * 制造商
     */
	private String manufacturer;
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


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommodityNo() {
		return commodityNo;
	}

	public void setCommodityNo(String commodityNo) {
		this.commodityNo = commodityNo;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Integer getCommodityType() {
		return commodityType;
	}

	public void setCommodityType(Integer commodityType) {
		this.commodityType = commodityType;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getAlarm() {
		return alarm;
	}

	public void setAlarm(Integer alarm) {
		this.alarm = alarm;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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
