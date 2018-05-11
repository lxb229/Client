package com.weixin.entity;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.weixin.utils.MD5Util;
import com.weixin.utils.WeiXinConstants;


public class UnifiedorderDto {
 
    private String appid;
    private String body;
    private String device_info;
    private String mch_id;
    private String nonce_str;
    private String notify_url;
    private String openId;
    private String out_trade_no;
    private String spbill_create_ip;
    private int total_fee;
    private String trade_type;
    private String product_id;
    private String sign;
     
    public UnifiedorderDto() {
        this.appid = WeiXinConstants.APPID;
        this.mch_id = WeiXinConstants.MCH_ID;
//        this.device_info = WeiXinConstants.DEVICE_INFO_WEB;
        this.notify_url = WeiXinConstants.NOTIFY_URL;
        this.trade_type = WeiXinConstants.TRADE_TYPE_NATIVE;
    }
 
    public String getAppid() {
        return appid;
    }
 
    public void setAppid(String appid) {
        this.appid = appid;
    }
 
    public String getBody() {
        return body;
    }
 
    public void setBody(String body) {
        this.body = body;
    }
 
    public String getDevice_info() {
        return device_info;
    }
 
    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }
 
    public String getMch_id() {
        return mch_id;
    }
 
    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }
 
    public String getNonce_str() {
        return nonce_str;
    }
 
    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }
 
    public String getNotify_url() {
        return notify_url;
    }
 
    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
 
    public String getOpenId() {
        return openId;
    }
 
    public void setOpenId(String openId) {
        this.openId = openId;
    }
 
    public String getOut_trade_no() {
        return out_trade_no;
    }
 
    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }
 
    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }
 
    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }
 
    public int getTotal_fee() {
        return total_fee;
    }
 
    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }
 
    public String getTrade_type() {
        return trade_type;
    }
 
    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
 
    public String getSign() {
        return sign;
    }
 
    public void setSign(String sign) {
        this.sign = sign;
    }
 
    public String getProduct_id() {
        return product_id;
    }
 
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String generateXMLContent() {
        String xml = "<xml>" +  
                "<appid>" + this.appid + "</appid>" +   
                "<body>" + this.body + "</body>" +   
                "<device_info>WEB</device_info>" +   
                "<mch_id>" + this.mch_id + "</mch_id>" +   
                "<nonce_str>" + this.nonce_str + "</nonce_str>" +  
                "<notify_url>" + this.notify_url + "</notify_url>" +   
                "<out_trade_no>" + this.out_trade_no + "</out_trade_no>" +   
                "<product_id>" + this.product_id + "</product_id>" +  
                "<spbill_create_ip>" + this.spbill_create_ip+ "</spbill_create_ip>" +  
                "<total_fee>" + this.total_fee + "</total_fee>" +   
                "<trade_type>" + this.trade_type + "</trade_type>" +   
                "<sign>" + this.sign + "</sign>" +   
             "</xml>";  
        return xml;
    }
     
    public String makeSign(String characterEncoding, UnifiedorderDto dto) {
      //参数：开始生成签名
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("device_info", dto.device_info);
        parameters.put("appid", dto.appid);
        parameters.put("mch_id", dto.mch_id);
        parameters.put("nonce_str", dto.nonce_str);
        parameters.put("body", dto.body);
        parameters.put("nonce_str", dto.nonce_str);
//        parameters.put("detail", dto.detail);
//        parameters.put("attach", dto.attach);
        parameters.put("out_trade_no", dto.out_trade_no);
        parameters.put("total_fee", dto.total_fee);
//        parameters.put("time_start", dto.time_start);
//        parameters.put("time_expire", dto.time_expire);
        parameters.put("notify_url", dto.notify_url);
        parameters.put("trade_type", dto.trade_type);
        parameters.put("spbill_create_ip", dto.spbill_create_ip);
        
        StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v)
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + WeiXinConstants.MD5_API_KEY);
		System.out.println("字符串拼接后是："+sb.toString());
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
        
    }
     
}