package com.guse.platform.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.guse.platform.TestBase;
import com.guse.platform.service.doudou.SystemProductService;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class SystemProductServiceTest extends TestBase {
	
	@Autowired
	private SystemProductService productService;
	
	
	/*
	 * 分页查询用户
	 */
	@Test
	public void WeixinNotifyUrl(){
		String parrStr = "<xml><appid><![CDATA[wxdbf0450917efac13]]></appid>" +
"<attach><![CDATA[SC011715572133444]]></attach>" +
"<bank_type><![CDATA[CFT]]></bank_type>" +
"<cash_fee><![CDATA[1]]></cash_fee>" +
"<device_info><![CDATA[WEB]]></device_info>" +
"<fee_type><![CDATA[CNY]]></fee_type>" +
"<is_subscribe><![CDATA[N]]></is_subscribe>" +
"<mch_id><![CDATA[1398631402]]></mch_id>" +
"<nonce_str><![CDATA[UXXxG6pXIJi0PKTI]]></nonce_str>" +
"<openid><![CDATA[osmfavvOxWQu8VBbcvaY1VeSGIqg]]></openid>" +
"<out_trade_no><![CDATA[2018011715572118541]]></out_trade_no>" +
"<result_code><![CDATA[SUCCESS]]></result_code>" +
"<return_code><![CDATA[SUCCESS]]></return_code>" +
"<sign><![CDATA[596142013BF961E999310AC93C86618E]]></sign>" +
"<time_end><![CDATA[20180117155734]]></time_end>" +
"<total_fee>1</total_fee>" +
"<trade_type><![CDATA[NATIVE]]></trade_type>" +
"<transaction_id><![CDATA[4200000084201801175773679037]]></transaction_id>" +
"</xml>";
		String signReturn = productService.WeixinNotifyUrl(parrStr);
		System.out.println("反向验证签名结果："+signReturn);
		
	}
	
}
