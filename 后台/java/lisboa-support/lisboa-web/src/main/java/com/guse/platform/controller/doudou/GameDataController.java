
package com.guse.platform.controller.doudou;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.service.doudou.GoldLogService;


@Controller
@RequestMapping("/gameData")
public class GameDataController extends BaseController {
	@Autowired
	private GoldLogService goldLogService;
	
	@RequestMapping(value = "/gameLog", method = RequestMethod.POST)
    public @ResponseBody Object gameLog(HttpServletRequest request)throws Exception  {
		InputStream inputStream = null;  
	    BufferedInputStream buf = null;  
	    StringBuffer requestJsonBuffer = null;  
	    try {  
	        inputStream = request.getInputStream();  
	        buf = new BufferedInputStream(inputStream);  
	        byte[] buffer = new byte[1024];  
	        requestJsonBuffer = new StringBuffer();  
	        int a = 0;  
	        while ((a = buf.read(buffer)) != -1){  
	            requestJsonBuffer.append(new String(buffer, 0, a, "UTF-8"));  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }finally{  
	        //关闭连接  
	        if (null != buf){  
	            try {  
	                buf.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        if (null != inputStream){  
	            try {  
	                inputStream.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	          
	    }  
	    if(requestJsonBuffer != null) {
	    	Result<Integer> result = goldLogService.saveOrUpdateGoldLog(requestJsonBuffer.toString());
	    	if (!result.isOk()) {
	    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
	    	}
	    	return new AjaxResponse(Constant.CODE_SUCCESS, "OK");
	    } else {
	    	return new AjaxResponse(Constant.CODE_ERROR, "error");
	    }
	    	
	}
	
}
