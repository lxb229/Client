package com.guse.httpstock.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.httpstock.socket.zk.CuratorUtil;

/** 
* @ClassName: StockController 
* @Description: 接口相关访问信息
* @author Fily GUSE
* @date 2017年9月15日 下午3:43:11 
*  
*/
@Controller  
@Scope("prototype")  
@RequestMapping("stock")
public class StockController {
	
	/** 
	* @Title: getStockAddresssMore 
	* @Description: 获取 全部地址
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("addAll")  
    public @ResponseBody String getStockAddresssMore() { 
		
		return CuratorUtil.getStockAdds().toString();
    }
	
	/** 
	* @Title: getStockAddresss 
	* @Description: 根据负载均衡获取一个 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("address")  
    public @ResponseBody String getStockAddresss() { 
		return CuratorUtil.doSelect();
    }
	


}
