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
 * 金币兑换商品
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
@TableName("gold_commodity")
public class GoldCommodity extends Model<GoldCommodity> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 商品
     */
	private Integer commodity;
    /**
     * 市场价
     */
	@TableField("market_price")
	private Integer marketPrice;
    /**
     * 兑换价
     */
	@TableField("exchange_price")
	private Integer exchangePrice;
    /**
     * 邮件内容
     */
	@TableField("email_content")
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
	@TableField("issued_time")
	private Date issuedTime;
    /**
     * 下架时间
     */
	@TableField("soldout_time")
	private Date soldoutTime;
    /**
     * 商品介绍
     */
	private String introduce;
    /**
     * 官网链接
     */
	@TableField("online_url")
	private String onlineUrl;

	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		if(id == null){
			 if(amount <= 0){
				 vb.setMsg("兑换数量必须大于0");
		         return vb;
			 } 
			 if(issuedTime == null) {
				 vb.setMsg("上架时间为空");
		         return vb; 
			 }
			 if(soldoutTime == null ) {
				 vb.setMsg("下架时间为空");
		         return vb; 
			 }
			 if(issuedTime.getTime() >= soldoutTime.getTime()) {
				 vb.setMsg("时间错误");
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

	public Integer getCommodity() {
		return commodity;
	}

	public void setCommodity(Integer commodity) {
		this.commodity = commodity;
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
	protected Serializable pkVal() {
		return this.id;
	}

}
