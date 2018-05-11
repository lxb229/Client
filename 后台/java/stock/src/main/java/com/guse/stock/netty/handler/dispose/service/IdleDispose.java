package com.guse.stock.netty.handler.dispose.service;

import org.springframework.stereotype.Component;

import com.guse.stock.netty.handler.dispose.AbstractDispose;

/** 
* @ClassName: IdleDispose 
* @Description: 心跳协议
* @author Fily GUSE
* @date 2017年8月25日 下午5:07:52 
*  
*/
@Component
public class IdleDispose extends AbstractDispose {
	
	/** 
	* @Fields pt : 协议号
	*/
	public Integer pt = 0;
	
	
	@Override
	public void loginCheck() {
		// 心跳包不用登录验证
	}

	@Override
	public void decodeMessage() {
		// 心跳包不需要验证
	}



	@Override
	public void processing() {
		msg.setData("");
	}

}
