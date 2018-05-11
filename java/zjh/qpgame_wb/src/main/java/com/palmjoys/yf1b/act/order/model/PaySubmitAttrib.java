package com.palmjoys.yf1b.act.order.model;

import java.util.HashMap;
import java.util.Map;

public class PaySubmitAttrib {
	//版本号(参与签名)
	public String version;
	//商户号(参与签名)
	public String customerid;
	//订单号(参与签名)
	public String sdorderno;
	//金额(元,需要保留2位小数点)(参与签名)
	public String total_fee;	
	//支付方式
	public String paytype;
	//银行编码
	public String bankcode;
	//支付成功异步回调服务器URL(参与签名)
	public String notifyurl;
	//支付成功客户端同步URL(参与签名)
	public String returnurl;
	//附加信息
	public String remark;
	//MD5加密值
	public String sign;
	//apiKey(参与签名)
	public String apikey;
	//获取码
	public String get_code = "0";
	
	public String getParams(){
		String retParam = "";
		retParam += "version="+this.version + "&";
		retParam += "customerid="+this.customerid + "&";
		retParam += "paytype="+this.paytype + "&";
		retParam += "total_fee="+this.total_fee + "&";
		retParam += "sdorderno="+this.sdorderno + "&";
		retParam += "notifyurl="+this.notifyurl + "&";
		retParam += "returnurl="+this.returnurl + "&";
		retParam += "remark="+this.remark + "&";
		retParam += "get_code="+this.get_code + "&";
		retParam += "bankcode="+this.bankcode + "&";
		retParam += "sign="+this.sign;
		return retParam;
	}
	
	public Map<String, Object> getMapParams(){
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("version", this.version);
		retMap.put("customerid", this.customerid);
		retMap.put("paytype", this.paytype);
		retMap.put("total_fee", this.total_fee);
		retMap.put("sdorderno", this.sdorderno);
		retMap.put("notifyurl", this.notifyurl);
		retMap.put("returnurl", this.returnurl);
		retMap.put("remark", this.remark);
		retMap.put("get_code", this.get_code);
		retMap.put("bankcode", this.bankcode);
		retMap.put("sign", this.sign);
		
		return retMap;
	}
	
	
	public String getSignParamStr(){
		String retParam = "";
		retParam += "version="+this.version + "&";
		retParam += "customerid="+this.customerid + "&";
		retParam += "total_fee="+this.total_fee + "&";
		retParam += "sdorderno="+this.sdorderno + "&";
		retParam += "notifyurl="+this.notifyurl + "&";
		retParam += "returnurl="+this.returnurl + "&";
		retParam += this.apikey;		
		
		return retParam;
	}
	
}
