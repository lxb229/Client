
package com.guse.platform.controller.doudou;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.BaseController;
import com.guse.platform.service.doudou.SystemTaskService;


@Controller
@RequestMapping("/systemTask")
public class SystemTaskController extends BaseController {
	 @Autowired 
	 private SystemTaskService systemTaskService;
	    
	/**
	 * 添加用户任务
	 * @param 
	 * @return
	 */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
	public @ResponseBody Object user(HttpServletRequest request){
		
		StringBuffer jsonBuffer = getRequestJsonBuffer();
	    return systemTaskService.operateUser(jsonBuffer.toString());
	}
    
    /**
	 * 添加用户任务
	 * @param 
	 * @return
	 */
    @RequestMapping(value = "/roomCard", method = RequestMethod.POST)
	public @ResponseBody Object roomCard(HttpServletRequest request){
		
		StringBuffer jsonBuffer = getRequestJsonBuffer();
	    return systemTaskService.operateRoomCard(jsonBuffer.toString());
	}
    
    /**
	 * 添加用户任务
	 * @param 
	 * @return
	 */
    @RequestMapping(value = "/club", method = RequestMethod.POST)
	public @ResponseBody Object club(HttpServletRequest request){
		
		StringBuffer jsonBuffer = getRequestJsonBuffer();
	    return systemTaskService.operateClub(jsonBuffer.toString());
	}
    
    /**
	 * 添加用户任务
	 * @param 
	 * @return
	 */
    @RequestMapping(value = "/clubUser", method = RequestMethod.POST)
	public @ResponseBody Object clubUser(HttpServletRequest request){
		
		StringBuffer jsonBuffer = getRequestJsonBuffer();
	    return systemTaskService.operateClubUser(jsonBuffer.toString());
	}
    
    /**
	 * 玩家使用幸运值事件
	 * @param 
	 * @return
	 */
    @RequestMapping(value = "/userLuck", method = RequestMethod.POST)
	public @ResponseBody Object userLuck(HttpServletRequest request){
		
		StringBuffer jsonBuffer = getRequestJsonBuffer();
	    return systemTaskService.operateUserLuck(jsonBuffer.toString());
	}
    
}
