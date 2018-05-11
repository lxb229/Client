package com.weixin.paytest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.weixin.entity.Unifiedorder;
import com.weixin.utils.HttpXmlUtils;
import com.weixin.utils.ParseXMLUtils;
import com.weixin.utils.RandCharsUtils;
import com.weixin.utils.WXSignUtils;
import com.weixin.utils.WeiXinConstants;

public class WeixinPayTest {
	
	private static final Logger logger = LoggerFactory.getLogger(WeixinPayTest.class);
	
	public static void main(String[] args) {
		WeixinPayTest payTest = new WeixinPayTest();
		String result = payTest.getWeixinPagePayUrl("182.150.139.38", "测试房卡100张", "100张", 10, "testdemo");
		System.out.println(result);
	}
	
	
	public String getWeixinPagePayUrl(String ip, String desc, String title, Integer totalAmount, String attachData){
        String passback_params="";
        try {
            passback_params = URLDecoder.decode(attachData,"UTF-8"); //附加数据
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //参数组
        String appid = WeiXinConstants.APPID;
        String mch_id = WeiXinConstants.MCH_ID;
        String nonce_str = RandCharsUtils.getRandomString(16);
        String body = title;           //商品描述
        String detail = desc;             //商品详情
        String attach = passback_params;      //附加数据   自定义数据
        String out_trade_no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(int)(Math.random()*90000+10000);           //订单号
        int total_fee = totalAmount;//单位是分，即是0.01元            //订单总金额，单位为分
        String spbill_create_ip = ip;              //用户端实际ip
        String time_start = RandCharsUtils.timeStart();
        String time_expire = RandCharsUtils.timeExpire();
        String notify_url = WeiXinConstants.PHONE_NOTIFY_URL;              //接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
        String trade_type = "MWEB";


        //参数：开始生成签名
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("appid", appid);
        parameters.put("mch_id", mch_id);
        parameters.put("nonce_str", nonce_str);
        parameters.put("body", body);
        //parameters.put("nonce_str", nonce_str);
        parameters.put("detail", detail);
        parameters.put("attach", attach);
        parameters.put("out_trade_no", out_trade_no);
        parameters.put("total_fee", total_fee);
        parameters.put("time_start", time_start);
        parameters.put("time_expire", time_expire);
        parameters.put("notify_url", notify_url);
        parameters.put("trade_type", trade_type);
        parameters.put("spbill_create_ip", spbill_create_ip);

        String sign = WXSignUtils.createSign("UTF-8", parameters);
        logger.info("签名是："+sign);


        Unifiedorder unifiedorder = new Unifiedorder();
        unifiedorder.setAppid(appid);
        unifiedorder.setMch_id(mch_id);
        unifiedorder.setNonce_str(nonce_str);
        unifiedorder.setSign(sign);
        unifiedorder.setBody(body);
        unifiedorder.setDetail(detail);
        unifiedorder.setAttach(attach);
        unifiedorder.setOut_trade_no(out_trade_no);
        unifiedorder.setTotal_fee(total_fee);
        unifiedorder.setSpbill_create_ip(spbill_create_ip);
        unifiedorder.setTime_start(time_start);
        unifiedorder.setTime_expire(time_expire);
        unifiedorder.setNotify_url(notify_url);
        unifiedorder.setTrade_type(trade_type);


        //构造xml参数
        String xmlInfo = HttpXmlUtils.xmlInfo(unifiedorder);

        String wxUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

        String method = "POST";

        String weixinPost = HttpXmlUtils.httpsRequest(wxUrl, method, xmlInfo).toString();

        logger.info(weixinPost);
        JSONObject result = ParseXMLUtils.jdomParseXml(weixinPost);

        return result.getString("mweb_url");
    }
}