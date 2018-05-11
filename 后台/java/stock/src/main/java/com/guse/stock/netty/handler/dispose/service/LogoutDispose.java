package com.guse.stock.netty.handler.dispose.service;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.guse.stock.netty.handler.dispose.AbstractDispose;

/** 
* @ClassName: LogoutDispose 
* @Description: 注销登录
* @author Fily GUSE
* @date 2017年8月28日 下午5:51:27 
*  
*/
@Component
public class LogoutDispose extends AbstractDispose {

	/** 
	* @Fields pt : 协议号
	*/
	public Integer pt = 200;
	
	@Override
	public void processing() {
		
		msg.setData("");
	}
	
	@Override
	public void respond() throws IOException{
		super.respond();
		// 关闭客户端
		channel.close();
	}

}
