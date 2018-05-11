package com.alipay.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.config.AlipayContants;

public class AlipayUtil {

    private static final Logger logger = LoggerFactory.getLogger(AlipayUtil.class);

    private String APP_ID = AlipayContants.appid;
    private String APP_PRIVATE_KEY = AlipayContants.appPrivateKey;
    private String ALIPAY_PUBLIC_KEY = AlipayContants.alipayPublicKey;
    private String SIGN_TYPE = AlipayContants.signType;
    private String CHARSET = AlipayContants.charset ;
    private String URL = AlipayContants.url;
    
	public String getAlipayPagePayUrl(String out_trade_no, String desc, String title, String totalAmount, String body) {
        logger.info("######################进入支付宝 网页支付 方法################");
        AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        
        alipayRequest.setReturnUrl(AlipayContants.returnUrl);
        alipayRequest.setNotifyUrl(AlipayContants.notifyUrl);//在公共参数中设置回跳和通知地址
        
        JSONObject js = new JSONObject();
        js.put("out_trade_no",out_trade_no);
        js.put("total_amount",totalAmount);
        js.put("subject",title);
        js.put("product_code","FAST_INSTANT_TRADE_PAY");
        
        js.put("body", body);//URLEncoder.encode(body,"UTF-8")网页版编码后，会出错
        alipayRequest.setBizContent(js.toString());
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }
}
