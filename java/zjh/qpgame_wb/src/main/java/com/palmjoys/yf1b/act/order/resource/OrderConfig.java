package com.palmjoys.yf1b.act.order.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("order")
public class OrderConfig {
	@ResourceId
	private int id;
	//充值采用的标识
	private int payType;
	//充值数据提交地址
	private String payUrl;
	//商户号
	private String sellerNO;
	//商户Key
	private String sellerKEY;
	//支付成功异步回调服务器URL
	private String ubotzurl;
	//wx支付类型
	private String wxPay;
	//支付宝支付类型
	private String aliPay;
	
	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getPayUrl() {
		return payUrl;
	}
	
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	
	public String getSellerNO() {
		return sellerNO;
	}
	
	public void setSellerNO(String sellerNO) {
		this.sellerNO = sellerNO;
	}
	
	public String getSellerKEY() {
		return sellerKEY;
	}
	
	public void setSellerKEY(String sellerKEY) {
		this.sellerKEY = sellerKEY;
	}

	public String getUbotzurl() {
		return ubotzurl;
	}

	public void setUbotzurl(String ubotzurl) {
		this.ubotzurl = ubotzurl;
	}

	public String getWxPay() {
		return wxPay;
	}

	public void setWxPay(String wxPay) {
		this.wxPay = wxPay;
	}

	public String getAliPay() {
		return aliPay;
	}

	public void setAliPay(String aliPay) {
		this.aliPay = aliPay;
	}	
	

}
