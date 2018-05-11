package com.guse.platform.controller.config;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.utils.ApiAddressUtil;
import com.guse.platform.common.utils.HttpClientUtil;

/**
 * 配置管理-中奖展示配置
 * @author yanhua
 */
@Controller
@RequestMapping("/config/winning")
public class WinningController {
	private static final Logger logger = LoggerFactory.getLogger(WinningController.class);
	@Autowired
	ApiAddressUtil apiAddressUtil;
	
	
	/** 龙珠探宝-中奖信息列表 */
	@RequestMapping(value = "/lztbList", method = RequestMethod.POST)
	public @ResponseBody Object lztbList() {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.LZTB_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_LZTB_LIST);
        data.put("sid", ApiAddressUtil.SID);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 龙珠探宝-中奖信息保存 */
	@RequestMapping(value = "/lztbSave", method = RequestMethod.POST)
	@ResponseBody
	public Object lztbSave(String format_id, String format, String bs_min, String bs_max, String award_gold_min, String award_gold_max, String style_type, String is_open, String type, String is_del, String showPlace) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.LZTB_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_LZTB_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        if (format_id != null && format_id.length() != 0) {
        	data.put("format_id", format_id);
        }   
        data.put("format", format);
        data.put("bs_min", bs_min);
        data.put("bs_max", bs_max);
        data.put("award_gold_min", award_gold_min);
        data.put("award_gold_max", award_gold_max);
        data.put("style_type", style_type);
        data.put("is_open", is_open);
        data.put("type", type);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 龙珠探宝-中奖信息删除 */
	@RequestMapping(value = "/lztbDel", method = RequestMethod.POST)
	@ResponseBody
	public Object lztbDel(String id,String type) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.LZTB_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_LZTB_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        data.put("format_id", id);
        data.put("is_del", "1");
        data.put("type", type);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	
	
	/** 五星宏辉-中奖信息列表 */
	@RequestMapping(value = "/wxhhList", method = RequestMethod.POST)
	public @ResponseBody Object wxhhList() {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.WXHH_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_WXHH_LIST);
        data.put("sid", ApiAddressUtil.SID);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 五星宏辉-中奖信息保存 */
	@RequestMapping(value = "/wxhhSave", method = RequestMethod.POST)
	@ResponseBody
	public Object wxhhSave(String format_id, String award_gold_min, String award_gold_max, String is_open_gold_limit, String win_gold_limit, String type,
            String style_type, String show_channel, String is_open, String format) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.WXHH_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_WXHH_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        if(format_id != null && !format_id.equals("")) {
            data.put("format_id", format_id);        	
        }
        data.put("format", format);
        data.put("award_gold_min", award_gold_min);
        data.put("award_gold_max", award_gold_max);
        if ("true".equals(is_open_gold_limit)) {
        	is_open_gold_limit = "1";
        	data.put("win_gold_limit", win_gold_limit);
        }
        data.put("style_type", style_type);
        data.put("type", type);
        data.put("is_open_gold_limit", is_open_gold_limit);
        data.put("show_channel", show_channel);
        data.put("is_open", is_open);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 五星宏辉-中奖信息删除 */
	@RequestMapping(value = "/wxhhDel", method = RequestMethod.POST)
	@ResponseBody
	public Object wxhhDel(String id) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.WXHH_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_WXHH_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        data.put("is_del", "1");
        data.put("format_id", id);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}


	
	/** 水浒传-中奖信息列表 */
	@RequestMapping(value = "/shzList", method = RequestMethod.POST)
	public @ResponseBody Object shzList() {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.SHZ_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_SHZ_LIST);
        data.put("sid", ApiAddressUtil.SID);
        data.put("op_type", "1");
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 水浒传-中奖信息保存 */
	@RequestMapping(value = "/shzSave", method = RequestMethod.POST)
	@ResponseBody
	public Object shzSave(String id,String multipleMin, String multipleMax, String goldMin, String goldMax, String youXian, 
			String showStyle, String showType,String showPlace, String state, String format) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.SHZ_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_SHZ_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        if(id != null && !id.equals("")) {
        	//修改
        	data.put("op_type", "2");
        	data.put("id", id);
        } else {
        	//新增
            data.put("op_type", "4");
		}
        data.put("multipleMin", multipleMin);
        data.put("multipleMax", multipleMax);
        data.put("goldMin", goldMin);
        data.put("goldMax", goldMax);
        data.put("youXian", youXian);
        data.put("showStyle", showStyle);
        data.put("showType", showType);
        data.put("showPlace", showPlace);
        data.put("state", state);
        data.put("format", format);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 水浒传-中奖信息删除 */
	@RequestMapping(value = "/shzDel", method = RequestMethod.POST)
	@ResponseBody
	public Object shzDel(String id) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.SHZ_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_SHZ_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        data.put("op_type", "3");
        data.put("id", id);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	

	/** 捕鱼达人-中奖信息列表 */
	@RequestMapping(value = "/bydrList", method = RequestMethod.POST)
	public @ResponseBody Object bydrList() {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.BYDR_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_BYDR_LIST);
        data.put("sid", ApiAddressUtil.SID);
        data.put("operateType", "0");
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 捕鱼达人-中奖信息保存 */
	@RequestMapping(value = "/bydrSave", method = RequestMethod.POST)
	@ResponseBody
	public Object bydrSave(String id, String multipleMin, String multipleMax, String goldMin, String goldMax, String broadcastStyle, String awardType,
	        String state, String broadcastType, String msg,String type) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.BYDR_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_BYDR_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        if(id == null || id.equals("")) {
        	//新增
        	data.put("operateType", "1");
        } else {
        	//修改
        	data.put("operateType", "3");
        	data.put("id", id);
		}
        data.put("multipleMin", multipleMin);
        data.put("multipleMax", multipleMax);
        data.put("goldMin", goldMin);
        data.put("goldMax", goldMax);
        data.put("type", type);
        data.put("broadcastStyle", broadcastStyle);
        data.put("awardType",awardType);
        data.put("broadcastType", broadcastType);
        data.put("state", state);
        data.put("msg", msg);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 捕鱼达人-中奖信息删除 */
	@RequestMapping(value = "/bydrDel", method = RequestMethod.POST)
	@ResponseBody
	public Object bydrDel(String id) {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.BYDR_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_BYDR_SAVE);
        data.put("sid", ApiAddressUtil.SID);
        data.put("operateType", "2");
        data.put("id", id);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	
	/** 糖果派对-中奖信息列表 */
	@RequestMapping(value = "/tgpdList", method = RequestMethod.POST)
	public @ResponseBody Object tgpdList() {
		String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.TGPD_SERVICE);
    	//请求服务端的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("mid", ApiAddressUtil.MID_CONF_WINNING_TGPD_LIST);
        data.put("sid", ApiAddressUtil.SID);
        String result = HttpClientUtil.httpPost(ipAddress, data);

		return new AjaxResponse(result);
	}
	
	/** 糖果派对-中奖信息删除*/
    @RequestMapping("/tgpdDel")
    @ResponseBody
    public Object tgpdDel(String format_id, String type) throws ParseException {
    	String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.TGPD_SERVICE);
         //请求服务端的数据
    	Map<String, String> data = new HashMap<String, String>();
    	data.put("mid", ApiAddressUtil.MID_CONF_WINNING_TGPD_SAVE);
    	data.put("sid", ApiAddressUtil.SID);
    	data.put("format_id", format_id);
    	data.put("is_del", "1");
    	data.put("type", type);
        String result = HttpClientUtil.httpPost(ipAddress, data);
        return new AjaxResponse(result); 
    }
    
    /** 糖果派对-中奖信息保存 */
    @RequestMapping("/tgpdSave")
    @ResponseBody
    public Object tgpdSave(String format_id, String bs_min, String bs_max, String award_gold_min, String award_gold_max, String style_type, String type, String is_open, String format, String player_address, String award_type) throws ParseException {
    	logger.info("添加糖果派对跑马灯信息");
    	String ipAddress = apiAddressUtil.getIpAddress(ApiAddressUtil.TGPD_SERVICE);
        //请求服务端的数据
    	 Map<String, String> data = new HashMap<String, String>();
    	 data.put("mid", ApiAddressUtil.MID_CONF_WINNING_TGPD_SAVE);
     	 data.put("sid", ApiAddressUtil.SID);
    	 data.put("bs_min", bs_min);
    	 data.put("bs_max", bs_max);
    	 data.put("award_gold_min", award_gold_min);
    	 data.put("award_gold_max", award_gold_max);
    	 data.put("style_type", style_type);
    	 data.put("type", type);
    	 data.put("is_open", is_open);
    	 data.put("format", format);
    	 data.put("player_address", player_address);
    	 data.put("award_type", award_type);
    	 data.put("is_del", "0");
        if (format_id != null) {
        	data.put("format_id", format_id);
        }
        String result = HttpClientUtil.httpPost(ipAddress, data);
        return new AjaxResponse(result);
    }

}
