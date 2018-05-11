package com.guse.platform.controller.gm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.utils.ApiAddressUtil;
import com.guse.platform.common.utils.HttpClientUtil;
/**
 * 禁言管理
 * @author yal
 *
 */
@Controller
@RequestMapping("/gm/forbid")
public class ForbidController {
	private static final String sid = ApiAddressUtil.SID;
	@Autowired
	private ApiAddressUtil addressUtil;
	
	/**
	 * 获取龙珠探宝评论
	 * @param pageBean
	 * @return
	 */
    @RequestMapping(value = "/lztbGameWords")
    @ResponseBody
    public Object lztbGameWords(PageBean pageBean) {
    	//请求服务端的数据
    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.LZTB_SERVICE);
        Map<String, String> sendmap = new HashMap<String, String>();
        sendmap.put("mid", ApiAddressUtil.MID_LZTB_FORBID_WORDS);
        sendmap.put("sid", sid);
        sendmap.put("page", pageBean.getPageNo()+"");
        sendmap.put("page_count", pageBean.getPageSize()+"");
        return HttpClientUtil.httpPost(ipAddress, sendmap);
    }
    /**
     * 删除龙珠探宝
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delLztbWords", method = RequestMethod.POST)
    @ResponseBody
    public Object delLztbGameWords(String delIds) {
    	AjaxResponse ajax = AjaxResponse.success();
    	try {
	    	//请求服务端的数据
	    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.LZTB_SERVICE);
	        Map<String, String> sendmap = new HashMap<String, String>();
	        sendmap.put("mid", ApiAddressUtil.MID_LZTB_FORBID_DEL);
	        sendmap.put("sid", sid);
	        String[] ids = delIds.split(",");
	        if (ids != null && ids.length > 0) {
	            for (String id : ids) {
	                sendmap.put("id", id);
	                HttpClientUtil.httpPost(ipAddress, sendmap);
	            }
	        }
    	} catch (Exception e) {
			ajax = AjaxResponse.error(e.getMessage());
		}
        return ajax;
    }
    /**
	 * 获取大厅评论
	 * @param pageBean
	 * @return
	 */
    @RequestMapping(value = "/hallGameWords")
    @ResponseBody
    public Object hallGameWords(PageBean pageBean) {
    	//请求服务端的数据
    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
        Map<String, String> sendmap = new HashMap<String, String>();
        sendmap.put("mid", ApiAddressUtil.MID_HALL_FORBID_WORDS);
        sendmap.put("sid", sid);
        sendmap.put("op_type", "1");	
        sendmap.put("page", pageBean.getPageNo()+"");
        sendmap.put("page_count", pageBean.getPageSize()+"");
        return HttpClientUtil.httpPost(ipAddress, sendmap);
    }
    /**
     * 删除大厅评论
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delHallWords", method = RequestMethod.POST)
    @ResponseBody
    public Object delHallWords(String delIds) {
    	AjaxResponse ajax = AjaxResponse.success();
    	try {
	    	//请求服务端的数据
	    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
	        Map<String, String> sendmap = new HashMap<String, String>();
	        sendmap.put("mid", ApiAddressUtil.MID_HALL_FORBID_DEL);
	        sendmap.put("op_type", "2");
	        sendmap.put("sid", sid);
	        String[] ids = delIds.split(",");
	        if (ids != null && ids.length > 0) {
	            for (String id : ids) {
	                sendmap.put("rid", id);
	                HttpClientUtil.httpPost(ipAddress, sendmap);
	            }
	        }
    	} catch (Exception e) {
			ajax = AjaxResponse.error(e.getMessage());
		}
        return ajax;
    }
    /**
	 * 获取五星宏辉评论
	 * @param pageBean
	 * @return
	 */
    @RequestMapping(value = "/wxhhGameWords")
    @ResponseBody
    public Object wxhhGameWords(PageBean pageBean) {
    	//请求服务端的数据
    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.WXHH_SERVICE);
        Map<String, String> sendmap = new HashMap<String, String>();
        sendmap.put("mid", ApiAddressUtil.MID_WXHH_FORBID_WORDS);
        sendmap.put("sid", sid);
        sendmap.put("op_type", "1");
        sendmap.put("page_now", pageBean.getPageNo()+"");
        sendmap.put("page_size", pageBean.getPageSize()+"");
        return HttpClientUtil.httpPost(ipAddress, sendmap);
    }
    /**
     * 删除五星宏辉
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delWxhhWords", method = RequestMethod.POST)
    @ResponseBody
    public Object delWxhhWords(String delIds) {
    	AjaxResponse ajax = AjaxResponse.success();
    	try {
	    	//请求服务端的数据
	    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.WXHH_SERVICE);
	        Map<String, String> sendmap = new HashMap<String, String>();
	        sendmap.put("mid", ApiAddressUtil.MID_WXHH_FORBID_DEL);
	        sendmap.put("sid", sid);
	        sendmap.put("op_type", "2");
	        String[] ids = delIds.split(",");
	        if (ids != null && ids.length > 0) {
	            for (String id : ids) {
	                sendmap.put("id", id);
	                HttpClientUtil.httpPost(ipAddress, sendmap);
	            }
	        }
    	} catch (Exception e) {
			ajax = AjaxResponse.error(e.getMessage());
		}
        return ajax;
    }
    /**
	 * 获取糖果派对评论
	 * @param pageBean
	 * @return
	 */
    @RequestMapping(value = "/tgpdGameWords")
    @ResponseBody
    public Object tgpdGameWords(PageBean pageBean) {
    	//请求服务端的数据
    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.TGPD_SERVICE);  //
    	//String ipAddress = "http://192.168.0.16:52001";
        Map<String, String> sendmap = new HashMap<String, String>();
        sendmap.put("mid", ApiAddressUtil.MID_TGPD_FORBID_WORDS);
        sendmap.put("sid", sid);
        sendmap.put("page", pageBean.getPageNo()+"");
        sendmap.put("page_count", pageBean.getPageSize()+"");
        return HttpClientUtil.httpPost(ipAddress, sendmap);
    }
    /**
     * 删除糖果派对评论
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delTgpdWords", method = RequestMethod.POST)
    @ResponseBody
    public Object tgpdWordsDel(String delIds) {
    	AjaxResponse ajax = AjaxResponse.success();
    	try {
	    	//请求服务端的数据
	    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.TGPD_SERVICE);
    		//String ipAddress = "http://192.168.0.16:52001";
	        Map<String, String> sendmap = new HashMap<String, String>();
	        sendmap.put("mid", ApiAddressUtil.MID_TGPD_FORBID_DEL);
	        sendmap.put("sid", sid);
	        String[] ids = delIds.split(",");
	        if (ids != null && ids.length > 0) {
	            for (String id : ids) {
	                sendmap.put("id", id);
	                HttpClientUtil.httpPost(ipAddress, sendmap);
	            }
	        }
    	} catch (Exception e) {
			ajax = AjaxResponse.error(e.getMessage());
		}
        return ajax;
    }
}
