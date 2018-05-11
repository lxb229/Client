package com.guse.platform.common.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.config.Config;
import com.guse.platform.entity.system.Resource;
import com.guse.platform.entity.system.Users;
import com.google.gson.Gson;

/**
 * 
 * @author nbin
 * @version $Id: SessionAndRightsInterceptor.java, v 0.1 2016年8月19日 下午4:47:07
 *          nbin Exp $
 */
public class SessionAndRightsInterceptor extends HandlerInterceptorAdapter {

	private Gson gson = new Gson();
	/**
	 * 权限拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String path = request.getServletPath().toLowerCase();
		//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@yal-ceshi。。。进来了~~~~");
		if (path.matches((String) Config.NO_INTERCEPTOR_PATH)) {
			return true;
		} else {
			HttpSession session = request.getSession();
			Users user = (Users) session.getAttribute(Constant.SESSION_LOGIN_USER);
			if (user == null) {
				return noAuth(response,"未登录或登录超时！");
			}
			List<Resource> list = user.getResourceList();
			if(list == null) {
				return noAuth(response,"没有任何权限！");
			}
			for (Resource resource : list) {
				if(resource.getSrUrl().toLowerCase().equals(path)){
					return true;
				}
			}
			return noAuth(response,"没有访问权限！");
		}
		//return super.preHandle(request, response, handler);
	}
	
	private boolean noAuth(HttpServletResponse response,String msg) {
		//System.out.println("##################################################");
		if(response != null) {
			System.out.println("yal-ceshi。。true。。进来了~~~~");
			return true;//测试使用;
		}
		try {
			response.setContentType("application/json;charset=UTF-8"); 
			PrintWriter writer = response.getWriter();
			AjaxResponse result = new AjaxResponse(Constant.CODE_ERROR, msg);
			writer.write(gson.toJson(result));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;		
	}

}
