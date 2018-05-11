package chesshttpserver.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/** 
* @ClassName: CheckVerController 
* @Description: 版本检查
* @author Fily GUSE
* @date 2017年8月16日 下午1:50:57 
*  
*/
@Controller
@Scope("prototype")
@RequestMapping("checkVer")
public class CheckVerController {
	
	 @RequestMapping  
     public @ResponseBody String index(String ver) {  
		 JSONObject obj = new JSONObject();
		 // -2错误版本-1版本过低1有新版本2已经最新
		 obj.put("type", 2);
		 // apk下载地址
		 obj.put("apkUrl", "http:www.baidu.com");
		 // ipa下载地址
		 obj.put("ipaUrl", "http:www.baidu.com");
		 // 错误信息，用于客户端显示
		 obj.put("errMsg", "nothing");
		 
         return JSON.toJSONString(obj);  
     }

}
