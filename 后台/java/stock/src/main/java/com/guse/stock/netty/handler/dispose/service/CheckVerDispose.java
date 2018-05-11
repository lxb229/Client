package com.guse.stock.netty.handler.dispose.service;

import org.springframework.stereotype.Component;

import com.guse.stock.netty.handler.dispose.AbstractDispose;

/** 
* @ClassName: CheckVerDispose 
* @Description: 版本监测
* @author Fily GUSE
* @date 2017年8月28日 下午5:49:55 
*  
*/
@Component
public class CheckVerDispose extends AbstractDispose {

	/** 
	* @Fields pt : 协议号
	*/
	public Integer pt = 6;
	
	@Override
	public void processing() {
		// TODO Auto-generated method stub

	}

}
