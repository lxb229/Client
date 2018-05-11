
package com.guse.platform.controller.doudou;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.SystemOrderService;
import com.guse.platform.service.doudou.SystemProductService;
import com.guse.platform.vo.doudou.PayVo;

@Controller
@RequestMapping("/weixin")
public class WeiXinController extends BaseController {
	
	@Autowired
	private SystemProductService productService;
	@Autowired
	private SystemOrderService orderService;
    /**
     * 下订单
     * @return
     */
    @RequestMapping(value = "/topay", method = RequestMethod.POST)
    @ResponseBody
    public Object topay(Integer spId, Model model) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	Result<PayVo> result = productService.buyProductByWX(spId, user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 手机下订单
     * @return
     */
    @RequestMapping(value = "/phonetopay", method = RequestMethod.POST)
    @ResponseBody
    public Object phonetopay(Integer spId, Model model) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
//    	String server_ip = WeiXinConstants.SERVER_IP;
    	String server_ip = getRemortIP();
    	System.out.println("客户端ip地址："+server_ip);
    	Result<PayVo> result = productService.phoneBuyProductByWX(server_ip,spId, user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 微信支付回调
     * @return
     */
    @RequestMapping(value = "/weixinNotifyUrl", method = RequestMethod.POST)
    @ResponseBody
    public Object weixinNotifyUrl(HttpServletRequest request) throws Exception {
        StringBuffer jsonBuffer = getRequestJsonBuffer();
        String result = productService.WeixinNotifyUrl(jsonBuffer.toString());
    	return result;
    }
    
    /**
     * 微信支付回调
     * @return
     */
    @RequestMapping(value = "/phoneWeixinNotifyUrl", method = RequestMethod.POST)
    @ResponseBody
    public Object phoneWeixinNotifyUrl(HttpServletRequest request) throws Exception {
        StringBuffer jsonBuffer = getRequestJsonBuffer();
        String result = productService.WeixinNotifyUrl(jsonBuffer.toString());
        return result;
    }
    
    /**
     * 查询订单状态
     * @return
     */
    @RequestMapping(value = "/ajaxstatus", method = RequestMethod.GET)
    @ResponseBody
    public Object ajaxstatus(String out_trade_no) throws Exception {
    	Result<Integer> result = orderService.seachStatus(out_trade_no);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    
}
