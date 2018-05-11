package com.guse.platform.common.base;


import java.beans.PropertyEditorSupport;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.guse.platform.entity.system.Users;

/**
 * @author nbin
 */
@Controller
public class BaseController {
    
	protected HttpServletResponse response;
	protected HttpServletRequest  request;
    protected HttpSession         session;
    protected AjaxResponse        ajaxResponse    = new AjaxResponse();
	
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
    	this.request = request;
        this.response = response;
    }
    
    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest();
    }

    protected HttpSession getSession() {
        HttpServletRequest request = getRequest();
        return request.getSession();
    }
    
    public String getBasePath() {
    	String basePath = request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + request.getContextPath();
    	return basePath;
    }
    
    public String getRemortIP() {  
		String ip = request.getHeader("x-forwarded-for"); 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("WL-Proxy-Client-IP"); 
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("DDMJ-Real-IP"); 
		} 
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getRemoteAddr(); 
		} 
		
		if(ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")){
			//根据网卡获取本机配置的IP地址  
			InetAddress inetAddress = null;  
			try {  
				inetAddress = InetAddress.getLocalHost();  
			} catch (UnknownHostException e) {  
				e.printStackTrace();  
			}  
			ip = inetAddress.getHostAddress();  
		}
		//对于通过多个代理的情况，第一个IP为客户端真实的IP地址，多个IP按照','分割  
		if(null != ip && ip.length() > 15){  
			if(ip.indexOf(",") > 0){  
				ip = ip.substring(0, ip.indexOf(","));  
			}  
		}
		return ip; 
	 } 
    
	public Map<String, String> getParamsMap() {
    	Map<String, String> map = new HashMap<String, String>();  
        Enumeration paramNames = request.getParameterNames();  
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();  
  
            String[] paramValues = request.getParameterValues(paramName);  
            if (paramValues.length == 1) {  
                String paramValue = paramValues[0];  
                if (paramValue.length() != 0) {  
                    map.put(paramName, paramValue);  
                }  
            }  
        }  
        return map;
    }
    
    /**
	 * 获取游戏服务器推送的数据
	 * @param request
	 * @return
	 */
    public StringBuffer getRequestJsonBuffer() {
    	InputStream inputStream = null;  
    	BufferedInputStream buf = null;  
    	StringBuffer requestJsonBuffer = null;  
    	try {  
    		inputStream = request.getInputStream();  
    		buf = new BufferedInputStream(inputStream);  
    		byte[] buffer = new byte[1024];  
    		requestJsonBuffer = new StringBuffer();  
    		int a = 0;  
    		while ((a = buf.read(buffer)) != -1){  
    			requestJsonBuffer.append(new String(buffer, 0, a, "UTF-8"));  
    		}  
    	} catch (Exception e) {  
    		e.printStackTrace();  
    	}finally{  
    		//关闭连接  
    		if (null != buf){  
    			try {  
    				buf.close();  
    			} catch (IOException e) {  
    				e.printStackTrace();  
    			}  
    		}  
    		if (null != inputStream){  
    			try {  
    				inputStream.close();  
    			} catch (IOException e) {  
    				e.printStackTrace();  
    			}  
    		}  
    	}  
    	return requestJsonBuffer;
    }
    
    /**
     * 获取用户
     * @Title: getUserInfo 
     * @Description: TODO
     * @param @return 
     * @return UsersVo  
     * @throws
    */
    protected Users getUserInfo() {
        return (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    }
    /**
     * 设置用户
     * @Title: putUserInfo 
     * @Description: TODO
     * @param @param request
     * @param @param loginUser 
     * @return void  
     * @throws
     */
    protected void putUserInfo(HttpServletRequest request, Users loginUser) {
        request.getSession().setAttribute(Constant.SESSION_LOGIN_USER, loginUser);
    }
    
    // 重新格式化时间
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyDateEditor());
    }
    
    class MyDateEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            if (text == null || text.equals("")) {
                setValue(date);
                return;
            }
            try {
                date = format.parse(text);
            } catch (ParseException e) {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    date = format.parse(text);
                } catch (ParseException e1) {
                    format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = format.parse(text);
                    } catch (ParseException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            setValue(date);
        }
    }
}
