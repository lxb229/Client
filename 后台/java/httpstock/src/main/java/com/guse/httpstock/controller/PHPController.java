package com.guse.httpstock.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.httpstock.socket.Message;
import com.guse.httpstock.socket.StockScoket;

/** 
* @ClassName: IndexController 
* @Description: php访问信息
* @author Fily GUSE
* @date 2017年9月5日 下午3:07:55 
*  
*/
@Controller  
@Scope("prototype")  
@RequestMapping("php")
public class PHPController {
	
	@RequestMapping("tapeout")  
    public @ResponseBody String tapeout(ModelMap modelMap,HttpServletRequest request) { 
		// 获取参数
		String uid = request.getParameter("uid");
		
		Message msg = new Message(20,"{\"user_id\":"+uid+"}");
		
		return StockScoket.sendMsg(msg);
    }

}
