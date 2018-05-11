package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 供应商
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public class Supplier extends Model<Supplier> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 供应商公司名字
     */
	@TableField("company_name")
	private String companyName;
    /**
     * 供应商名字
     */
	@TableField("supplier_name")
	private String supplierName;
    /**
     * 供应商性别
     */
	@TableField("supplier_sex")
	private Integer supplierSex;
    /**
     * 供应商电话
     */
	@TableField("supplier_phone")
	private String supplierPhone;
    /**
     * 供应商微信
     */
	@TableField("supplier_wechat")
	private String supplierWechat;
    /**
     * 供应商QQ
     */
	@TableField("supplier_qq")
	private String supplierQq;
    /**
     * 供应商邮箱
     */
	@TableField("supplier_email")
	private String supplierEmail;
    /**
     * 供应商官网
     */
	@TableField("supplier_url")
	private String supplierUrl;
    /**
     * 供应商公司地址
     */
	@TableField("company_address")
	private String companyAddress;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getSupplierSex() {
		return supplierSex;
	}

	public void setSupplierSex(Integer supplierSex) {
		this.supplierSex = supplierSex;
	}

	public String getSupplierPhone() {
		return supplierPhone;
	}

	public void setSupplierPhone(String supplierPhone) {
		this.supplierPhone = supplierPhone;
	}

	public String getSupplierWechat() {
		return supplierWechat;
	}

	public void setSupplierWechat(String supplierWechat) {
		this.supplierWechat = supplierWechat;
	}

	public String getSupplierQq() {
		return supplierQq;
	}

	public void setSupplierQq(String supplierQq) {
		this.supplierQq = supplierQq;
	}

	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}

	public String getSupplierUrl() {
		return supplierUrl;
	}

	public void setSupplierUrl(String supplierUrl) {
		this.supplierUrl = supplierUrl;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
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
