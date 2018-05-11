package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 红包领取返回对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class SilverCommodityVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
     * 表主键id
     */
	private Integer id;
    /**
     * 商品
     */
	private Integer commodity;
	/**
	 * 商品名称
	 */
	private String commodityName;
    /**
     * 商品等级
     */
	private Integer awardLv;
	/**
	 * 等级名称
	 */
	private String lvName;
    /**
     * 邮件内容
     */
	private String emailContent;
    /**
     * 上架时间
     */
	private Date issuedTime;
    /**
     * 下架时间
     */
	private Date soldoutTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getAwardLv() {
		return awardLv;
	}

	public void setAwardLv(Integer awardLv) {
		this.awardLv = awardLv;
	}

	public String getLvName() {
		return lvName;
	}

	public void setLvName(String lvName) {
		this.lvName = lvName;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public Date getIssuedTime() {
		return issuedTime;
	}

	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
	}

	public Date getSoldoutTime() {
		return soldoutTime;
	}

	public void setSoldoutTime(Date soldoutTime) {
		this.soldoutTime = soldoutTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}