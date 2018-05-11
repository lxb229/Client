package com.guse.four_one_nine.service;

import java.net.URLEncoder;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.guse.four_one_nine.common.HttpUtil;
import com.guse.four_one_nine.common.JSONUtil;
import com.guse.four_one_nine.dao.model.SysUser;

/** 
* @ClassName: AppInformService 
* @Description: APP端 通知交互服务类
* @author Fily GUSE
* @date 2018年1月15日 下午5:56:35 
*  
*/
@Service
public class AppInformService {
	public final static Logger logger = LoggerFactory.getLogger(AppInformService.class);
	/* app访问路径 */
	String appPath;
	
	/** 
	* @Description: 发送消息到app接口 
	* @param @param type
	* @param @param params
	* @param @return
	* @return String 
	* @throws 
	*/
	@SuppressWarnings("rawtypes")
	public JSONObject sendMessage(String type, JSONObject params) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		SysUser loginUser = (SysUser) session.getAttribute(SysUser.SESSION_NAME);
		
		logger.info("{}访问APP端口。请求地址：{}, 请求参数:{}",loginUser.getName(), type, params.toString());
		JSONObject response = new JSONObject();
		try {
			// 设置请求参数,token码 419->Base64->MD5
			StringBuffer paramStr = new StringBuffer("");
			// 其他参数
			if(params != null && !params.isEmpty()) {
				Iterator it = params.keys();
				while(it.hasNext()) {
					String key = (String)it.next();
					if(params.get(key) == null) {
						continue;
					}
					paramStr.append("&");
					paramStr.append(key);
					paramStr.append("=");
					paramStr.append(URLEncoder.encode(params.get(key).toString(), "UTF-8"));
				}
			}
			response = JSONUtil.toJSONObject(HttpUtil.sendPost(appPath + "/" + type + "?token=c5e9323e9885d0d5e238e3daac7c272e", paramStr.toString()));
		} catch (Exception e) {
			response.put("code", -1);
			response.put("msg", "请求异常");
			e.printStackTrace();
		}
		logger.info("APP端口回应消息。请求地址：{}, 回应信息:{}", type, response.toString());
		return response;
	}

	
	
	public String getAppPath() {
		return appPath;
	}
	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}
}
