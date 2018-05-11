package com.palmjoys.yf1b.act.gm.service;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;
import org.springframework.stereotype.Service;
import org.treediagram.nina.console.Console;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.handler.JettyRequestHandler;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.gm.model.GMDefine;

@Service
public class GmImp_GmManager implements JettyRequestHandler{

	@Override
	public String getPath() {
		return GMDefine.GM_CMD_GMMANAGER;
	}

	@Override
	public byte[] handleRequest(String target, Request baseRequest, HttpServletRequest request) {
		Object retObj = null;
		String err = "未知错误";
		try{
			while(true){
				Console console = Console.Instance();
				String command = request.getParameter("cmd");
				if (null == console){
					break;
				}
				retObj = console.executeCommand(command);
				err = "ok";
				break;
			}			
		}catch(Exception e){
			err = "服务器异常错误";
		}
		if(err.equalsIgnoreCase("ok") == false){
			retObj = Result.valueOfError(-1, err, null);
		}else{
			if(null == retObj){
				retObj = Result.valueOfSuccess();
			}
		}
		String retStr = JsonUtils.object2String(retObj);
		byte []retBytes = null;
		try{
			retBytes = retStr.getBytes("utf8");
		}catch(Exception e){
		}
		return retBytes;
	}
}
