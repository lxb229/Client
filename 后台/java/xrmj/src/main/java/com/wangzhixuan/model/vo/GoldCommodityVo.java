package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 兑换奖品对象Vo
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class GoldCommodityVo implements Serializable {
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
     * 市场价
     */
	private Integer marketPrice;
    /**
     * 兑换价
     */
	private Integer exchangePrice;
    /**
     * 邮件内容
     */
	private String emailContent;
    /**
     * 兑换数量
     */
	private Integer amount;
    /**
     * 剩余可兑换数量
     */
	private Integer residue;
    /**
     * 上架时间
     */
	private Date issuedTime;
    /**
     * 下架时间
     */
	private Date soldoutTime;
    /**
     * 商品介绍
     */
	private String introduce;
    /**
     * 官网链接
     */
	private String onlineUrl;
	
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

	public Integer getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Integer marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Integer getExchangePrice() {
		return exchangePrice;
	}

	public void setExchangePrice(Integer exchangePrice) {
		this.exchangePrice = exchangePrice;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getResidue() {
		return residue;
	}

	public void setResidue(Integer residue) {
		this.residue = residue;
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

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getOnlineUrl() {
		return onlineUrl;
	}

	public void setOnlineUrl(String onlineUrl) {
		this.onlineUrl = onlineUrl;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}