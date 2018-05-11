package com.guse.platform.controller.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.utils.ApiAddressUtil;
import com.guse.platform.common.utils.HttpClientUtil;

/**
 * 配置管理-大厅配置
 * @author yanhua
 */
@Controller
@RequestMapping("/config/hall")
public class HallController {
	
	@Autowired
	ApiAddressUtil apiAddressUtil;
	
	/** 跑马灯列表 */
	@RequestMapping(value = "/marqueeList", method = RequestMethod.POST)
	public @ResponseBody Object marqueeList() {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_HELL_MARQUEE_LIST);
        data.put("sid", ApiAddressUtil.SID);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 跑马灯添加 */
	@RequestMapping(value = "/marqueeSave", method = RequestMethod.POST)
	@ResponseBody
	public Object marqueeAdd(String id, String start_time, String end_time, String interval, String priority, String title, String content) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_HELL_MARQUEE_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        if (id != null && id.length() != 0) {
        	data.put("pmd_id", id);
        }
        data.put("start_time", start_time);
        data.put("end_time", end_time);
        data.put("interval", interval);
        data.put("priority", priority);
        data.put("title", title);
        data.put("content", content);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 跑马灯删除 */
	@RequestMapping(value = "/marqueeDel", method = RequestMethod.POST)
	@ResponseBody
	public Object marqueeDel(String pmd_id) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_HELL_MARQUEE_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        data.put("pmd_id", pmd_id);
        data.put("is_del", "1");
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	
	
	/** 邮件列表 */
	@RequestMapping(value = "/emailList", method = RequestMethod.POST)
	public @ResponseBody Object emailList(String pindex, String psize) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_HELL_EMAIL_LIST);
        data.put("sid", ApiAddressUtil.SID);
        data.put("page", pindex);
        data.put("page_count", psize);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 邮件发送 */
	@RequestMapping(value = "/emailSend", method = RequestMethod.POST)
	@ResponseBody
	public Object emailSend(String to_rid_list, String title, String content, String reson, String send_time, String expire_time, String send_item_list, String send_item_list_name) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_HELL_EMAIL_SEND);
        data.put("sid", ApiAddressUtil.SID);
        data.put("to_rid_list", to_rid_list);
        data.put("title", title);
        data.put("content", content);
        data.put("send_time", send_time);
        data.put("expire_time", expire_time);
        data.put("send_item_list", send_item_list);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 公告列表 */
	@RequestMapping(value = "/noticeList", method = RequestMethod.POST)
	public @ResponseBody Object noticeList() {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_HELL_NOTICE_LIST);
        data.put("sid", ApiAddressUtil.SID);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 公告保存 */
	@RequestMapping(value = "/noticeSave", method = RequestMethod.POST)
	@ResponseBody
	public Object noticeAdd(String id, String start_time, String end_time, String priority, String title, String content) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_HELL_NOTICE_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        if (id != null && id.length() != 0) {
        	data.put("notice_id",id);
        }     
        data.put("open_time", start_time);
        data.put("end_time", end_time);
        data.put("order", priority);
        data.put("title", title);
        data.put("content", content);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 公告删除 */
	@RequestMapping(value = "/noticeDel", method = RequestMethod.POST)
	@ResponseBody
	public Object noticeDel(String id) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_HELL_NOTICE_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        data.put("notice_id", id);
        data.put("is_del", "1");
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
}
