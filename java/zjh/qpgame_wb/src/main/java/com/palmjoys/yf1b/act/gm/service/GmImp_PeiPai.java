package com.palmjoys.yf1b.act.gm.service;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.handler.JettyRequestHandler;
import org.treediagram.nina.network.model.Result;

@Service
public class GmImp_PeiPai implements JettyRequestHandler{
	
	@Override
	public String getPath() {
		return "/gm/peipai";
	}

	@Override
	public byte[] handleRequest(String target, Request baseRequest, HttpServletRequest request) {
		String retStr = "";
		try{
			while(true){
				String ss = request.getParameter("cards");
				JSONArray jsonArry = new JSONArray(ss);
				if(jsonArry.length() != 5){
					retStr = JsonUtils.object2String(Result.valueOfError(-1, "配置牌参数错误", null));
					break;
				}
				retStr = JsonUtils.object2String(Result.valueOfSuccess("配置牌数据成功"));
				break;
			}
		}catch(Exception e){
			retStr = JsonUtils.object2String(Result.valueOfError(-1, "配置牌参数错误", null));
		}
		byte []retBytes = null;
		try{
			retBytes = retStr.getBytes("utf8");
		}catch(Exception e){
		}
		return retBytes;
	}
}
