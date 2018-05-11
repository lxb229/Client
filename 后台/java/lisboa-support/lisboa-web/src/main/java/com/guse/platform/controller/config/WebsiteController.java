package com.guse.platform.controller.config;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.utils.ApiAddressUtil;

/**
 * 配置管理-官网新闻配置
 * @author yanhua
 */
@Controller
@RequestMapping("/config/website")
public class WebsiteController {
	private static final Logger logger = LoggerFactory.getLogger(WebsiteController.class);
	//通行证
	private static final String sid = ApiAddressUtil.SID;
	@Autowired
	private ApiAddressUtil addressUtil;
	
	
	
	/**
	 * 获取账号信息
	 * @param uid
	 * @param tel
	 * @param nick
	 * @param account_name
	 * @param ip
	 * @param imel
	 * @return
	 */
    @RequestMapping(value = "/news")
    @ResponseBody
    public Object news() {
    	logger.info("PlayerInfoController.Users():{玩家信息查询}");
    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
    	//请求服务端的数据
        /*Map<String, String> sendmap = new HashMap<String, String>();
    	 sendmap.put("mid", ApiAddressUtil.MID_PLAYER_INFO);
         sendmap.put("sid", sid);
         sendmap.put("uid", uid);
         sendmap.put("nick", nick);
         sendmap.put("account_name", account_name);
         sendmap.put("ip", ip);
         sendmap.put("tel", tel);
         sendmap.put("imel", imel);
         String result = "";
         try {
        	 result = HttpClientUtil.httpPost(ipAddress, sendmap);
		} catch (Exception e) {
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
         logger.info("用户信息返回结果:{}",result);
         if(StringUtils.isEmpty(result)){
        	 return new AjaxResponse(Constant.CODE_ERROR, "调用接口失败");
         }*/
    	
    	
    	
    	
         return new AjaxResponse("");
    }
	

}
