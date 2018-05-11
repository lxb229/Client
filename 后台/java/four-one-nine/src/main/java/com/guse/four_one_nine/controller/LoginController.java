package com.guse.four_one_nine.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.dao.model.SysUser;
import com.guse.four_one_nine.service.SysUserService;

/** 
* @ClassName: LoginController 
* @Description: 登录适配器
* @author Fily GUSE
* @date 2018年1月16日 上午11:28:03 
*  
*/
@Controller
@Scope("prototype")
@RequestMapping("login")
public class LoginController {
	
	@Autowired
	SysUserService service;
	
	/** 
	* @Description: TODO 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/page")
	public String page() {
		
		return "login";
	}
	
	/** 
     * 登录 
     * @param session  HttpSession 
     * @param username  用户名 
     * @param password  密码 
     * @return 
     */  
    @RequestMapping(value="/login")
    @ResponseBody
    public ResponseAjax login(HttpSession session,String username,String password) throws Exception{  
    	ResponseAjax response = new ResponseAjax();
    	if(StringUtils.isBlank(username)) {
    		response.setFailure("请输入用户名!");
    		return response;
    	}
    	if(StringUtils.isBlank(password)) {
    		response.setFailure("请输入密码!");
    		return response;
    	}
    	SysUser user = service.getUser(username, password);
    	if(user != null) {
    		//在Session里保存信息  
            session.setAttribute("username", username);
            session.setAttribute(SysUser.SESSION_NAME, user);
            session.setAttribute("login_name", user.getName());
    	} else {
    		response.setFailure("用户名或密码错误!");
    	}
        return response; 
    }  
      
    /** 
     * 退出系统 
     * @param session 
     *          Session 
     * @return 
     * @throws Exception 
     */  
    @RequestMapping(value="/logout")  
    public String logout(HttpSession session) throws Exception{  
        //清除Session  
        session.invalidate();  
          
        return "redirect:/login/page";  
    }  

}
