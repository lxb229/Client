package com.guse.platform.controller.system;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.system.LoginLogs;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.system.LoginLogsService;
import com.guse.platform.service.system.UsersService;

/**
 * 登录
 * @author nbin
 * @date 2017年7月14日 下午12:50:52 
 * @version V1.0
 */
@Controller
public class LoginController extends BaseController {
	
	@Autowired
	private UsersService userService;
	@Autowired
	private LoginLogsService loginLogsService;
	
	/**	
	* @Title: doLogin 
	* @Description:  登录
	* @param @param username
	* @param @param password
	* @param @return 
	* @return Object  
	* @throws
	 */
	@RequestMapping(value = "/system/user/login", method = RequestMethod.POST)
    public @ResponseBody Object doLogin(String loginName, String loginPass)throws Exception  {
		if(StringUtils.isBlank(loginName)){
			 return new AjaxResponse(Constant.CODE_ERROR, "请输入用户名！");
		}
		if(StringUtils.isBlank(loginPass)){
			return new AjaxResponse(Constant.CODE_ERROR, "请输入密码！");
		}
		//
		Result<Users> userResult  = userService.loginUser(loginName, loginPass);
		if(!userResult.isOk()){
			return new AjaxResponse(Constant.CODE_ERROR, userResult.getErrorMsg());
		}
		if(null == userResult.getData()){
			return new AjaxResponse(Constant.CODE_ERROR, "用户名或密码错误！");
		}
		//success
		Users user = userResult.getData();
		if(user.getUserStatus() != 1) {
			return new AjaxResponse(Constant.CODE_ERROR, "登录账号被禁用，不能登录，请联系管理员！");
		}
//		//menu
//		Result<List<MenusVo>> menuResult =  menusService.getUserMenus(user);
//		if(!menuResult.isOk()){
//			return new AjaxResponse(Constant.CODE_ERROR, menuResult.getErrorMsg());
//		}
//		if(null == menuResult.getData()){
//			return new AjaxResponse(Constant.CODE_ERROR, "用户未分配菜单权限！");
//		}
//		user.setMenuList(menuResult.getData());
		getSession().setAttribute(Constant.SESSION_LOGIN_USER, user);

		return  new AjaxResponse(user);
    }
	
	
	/**
	 * 登出
	* @Title: loginOut 
	* @Description: TODO
	* @param @param username
	* @param @param password
	* @param @return 
	* @return Object  
	* @throws
	 */
	@RequestMapping(value = "/system/user/logout", method = RequestMethod.POST)
    public @ResponseBody Object loginOut() {
		if(getSession() != null){
			getSession().invalidate();
		}
		return AjaxResponse.success(true);
    }
	
  /**
	* 获得当前用户菜单
	* @Title: menus 
	* @Description: TODO
	* @param @return 
	* @return Object  
	* @throws
	 */
	@RequestMapping(value = "/system/user/menus", method = RequestMethod.POST)
    public @ResponseBody Object menus() {
		Users user   = (Users) session.getAttribute(Constant.SESSION_LOGIN_USER);
		if(null == user){
			session.invalidate();
			return new AjaxResponse(Constant.CODE_ERROR, "登录超时请重新登录！");
		}
		return AjaxResponse.success(user.getMenuList());
    }
	
	
	/**
     * 日志列表
     * @return
     */
    @RequestMapping(value = "/system/logs/queryLogPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryLogPagelist(LoginLogs logs,PageBean pageBean) {
    	Result<PageResult<LoginLogs>> result = null;
		result = loginLogsService.queryPageList(pageBean, logs, "id");
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
	
}
