package com.weixin.paytest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.alipay.config.AlipayConfig;
import com.alipay.config.AlipayContants;

public class AliPayTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AliPayTest.class);
	
	public static void main(String[] args) {
		AliPayTest payTest = new AliPayTest();
		String result = payTest.getAlipayPagePayUrl("测试房卡100张", "100张", "0.1", "testdemo");
		System.out.println(result);
	}
	
	public String getAlipayPagePayUrl(String desc, String title, String totalAmount, String out_trade_no) {
        logger.info("######################进入支付宝 网页支付 方法################");
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayContants.url, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,AlipayConfig.sign_type); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        
    	alipayRequest.setReturnUrl(AlipayConfig.phone_return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.phone_notify_url);//在公共参数中设置回跳和通知地址

        JSONObject js = new JSONObject();
        js.put("out_trade_no",out_trade_no);
        js.put("total_amount",totalAmount);
        js.put("subject",title);
        js.put("product_code",AlipayConfig.product_code);
        
        alipayRequest.setBizContent(js.toString());

//        alipayRequest.setBizContent("{" +
//                " \"out_trade_no\":\"20150320010101002\"," +
//                " \"total_amount\":\"88.88\"," +
//                " \"subject\":\"Iphone6 16G\"," +
//                " \"product_code\":\"QUICK_WAP_PAY\"" +
//                " }");//填充业务参数
        String form="";
        try {
//            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
//        	AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest,"GET");// 改为get提交之后获取支付地址
            form = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }
	
}