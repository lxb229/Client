package com.guse.platform.controller.gm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.utils.ApiAddressUtil;
import com.guse.platform.common.utils.HttpClientUtil;
import com.guse.platform.service.doudou.GameActivityRotorDetailService;
import com.guse.platform.service.doudou.GameActivityRotorStatisticsService;
import com.guse.platform.service.task.BaseUserTask;

/**
 * 账号信息
 * @author liyang
 *
 */
@Controller
@RequestMapping("/gm/player")
public class PlayerInfoController {
	private static final Logger logger = LoggerFactory.getLogger(PlayerInfoController.class);
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
	    @RequestMapping(value = "/users")
	    @ResponseBody
	    public Object users(String uid, String tel, String nick, String account_name, String ip, String imel) {
	    	logger.info("PlayerInfoController.Users():{玩家信息查询}");
	    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
	    	//请求服务端的数据
            Map<String, String> sendmap = new HashMap<String, String>();
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
             }
             return new AjaxResponse(result);
	    }
	    
	    /**
	     * 单个解冻或冻结
	     * @param uid
	     * @param state
	     * @return
	     */
	    @RequestMapping(value = "/frozen")
	    @ResponseBody
	    public Object frozenAccount(String uid, String state) {
	    	logger.info("PlayerInfoController.frozenAccount():{账号解冻，冻结}");
	    	 if("1".equals(state)){
            	state = "0";
            }else{
            	state = "1";
            }
	         String result = "";
	         try {
	        	 result = frozenOrDefreeze(uid, state);
			} catch (Exception e) {
				return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
			}
	         if(StringUtils.isEmpty(result)){
	        	 return new AjaxResponse(Constant.CODE_ERROR, "修改失败");
	         }else{
	        	 return new AjaxResponse(result);
	         }
	         
	    }
	    
	    
	    /**
	     * 批量解冻或冻结
	     * @param uids
	     * @param state
	     * @return
	     */
	    @RequestMapping(value = "/frozenBatch")
	    @ResponseBody
	    public Object frozenBatchAccount(String uids, String state) {
	    	logger.info("PlayerInfoController.frozenBatchAccount():{账号批量解冻，冻结}");
	    	//处理uid
	    	String msg = "";
	    	String[] userIds = uids.split(",");
	    	int successNum = 0;
	    	for (int i = 0; i < userIds.length; i++) {
	    		String result = frozenOrDefreeze(userIds[i], state);
	    		if(StringUtils.isEmpty(result)){
	    			msg = msg + userIds[i]+",";
	    			continue;
	    		}
	    		JSONObject jsonObject = JSON.parseObject(result);
	    		if(jsonObject.getIntValue("code")!=-1){
	    			msg = msg + userIds[i]+",";
	    		}else if(jsonObject.getIntValue("code")==-1){
	    			successNum++;
	    		}
	            logger.info(userIds[i]+"更改状态结果:{}",result);
			}
	    	AjaxResponse ajaxResponse = null;
	    	if(successNum==userIds.length){
	    		ajaxResponse = new AjaxResponse(Constant.CODE_SUCCESS, "修改成功");
	    	}else{
	    		ajaxResponse = new AjaxResponse(Constant.CODE_ERROR, msg+"修改失败!");
	    	}
	        return ajaxResponse;
	    }
	    
	    
	    private String frozenOrDefreeze(String uid, String state){
	    	 //请求服务端的数据
	    	String ipAddress = addressUtil.getIpAddress(ApiAddressUtil.HALL_SERVICE);
            Map<String, String> sendmap = new HashMap<String, String>();
            sendmap.put("mid", ApiAddressUtil.MID_PLAYER_FROZEN);
            sendmap.put("sid", sid);
            sendmap.put("uid", uid);
            sendmap.put("state", state);
            
            String result = "";
            try {
            	result = HttpClientUtil.httpPost(ipAddress, sendmap);
			} catch (Exception e) {
				logger.error("解冻冻结服务失败",e);
			}
	        return result;
	    }
}
