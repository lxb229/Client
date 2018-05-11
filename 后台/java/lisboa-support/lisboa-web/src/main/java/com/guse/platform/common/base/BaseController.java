package com.guse.platform.common.base;


import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
