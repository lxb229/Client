package com.guse.four_one_nine.controller.base;

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

import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.dao.model.SysUser;

/** 
* @ClassName: BaseController 
* @Description: 适配器 基类
* @author Fily GUSE
* @date 2018年1月19日 上午10:50:09 
*  
*/
@Controller
public class BaseController {
	protected HttpServletResponse response;
	protected HttpServletRequest  request;
    protected HttpSession         session;
    protected ResponseAjax		  respAjax = new ResponseAjax();
    
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
    protected SysUser getUserInfo() {
        return (SysUser) getSession().getAttribute(SysUser.SESSION_NAME);
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
    protected void putUserInfo(HttpServletRequest request, SysUser loginUser) {
        request.getSession().setAttribute(SysUser.SESSION_NAME, loginUser);
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
