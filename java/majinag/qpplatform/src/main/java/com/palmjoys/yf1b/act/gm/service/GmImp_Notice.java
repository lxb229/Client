package com.palmjoys.yf1b.act.gm.service;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.handler.JettyRequestHandler;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.gm.model.GMDefine;
import com.palmjoys.yf1b.act.message.manager.NoticeManager;
import com.palmjoys.yf1b.act.message.service.MessageService;

@Service
public class GmImp_Notice implements JettyRequestHandler{
	@Autowired
	private MessageService messageService;
	@Autowired
	private NoticeManager noticeManager;

	@Override
	public String getPath() {
		return GMDefine.GM_CMD_NOTICE;
	}

	@Override
	public byte[] handleRequest(String target, Request baseRequest, HttpServletRequest request) {
		int err = GMDefine.GM_ERR_GM_CMD;
		Object retObj = null;
		try{
			String sCmd = request.getParameter("cmd");
			int nCmd = Integer.parseInt(sCmd);
			switch(nCmd){
			case 1://添加
				retObj = addNotice(baseRequest, request);
				break;
			case 2://修改
				retObj = modfiyNotice(baseRequest, request);
				break;
			case 3://删除
				retObj = deleteNotice(baseRequest, request);
				break;
			}			
		}catch(Exception e){
			err = GMDefine.GM_ERR_SVR_EXCEPTION;
		}
		
		if(null == retObj){
			retObj = Result.valueOfError(err, GMDefine.Err2Msg(err), null);
		}
		String retStr = JsonUtils.object2String(retObj);
		byte []retBytes = null;
		try{
			retBytes = retStr.getBytes("utf8");
		}catch(Exception e){
		}
		return retBytes;
	}

	private Object addNotice(Request baseRequest, HttpServletRequest request){
		String sNoticeId = request.getParameter("noticeId");
		String sTemplateId = request.getParameter("templateId");
		String noticeParams = request.getParameter("noticeParams");
		String startTime = request.getParameter("startTime");
		String vialdTime = request.getParameter("vialdTime");
		String sRepate = request.getParameter("repate");
				
		int noticeGmId = Integer.parseInt(sNoticeId);
		int templateId = Integer.parseInt(sTemplateId);
		String []params = noticeParams.split(",");
		int repate = Integer.parseInt(sRepate);
		messageService.message_notice_New(templateId, noticeGmId, params, startTime, vialdTime, repate);
		return Result.valueOfSuccess();
	}
	
	private Object modfiyNotice(Request baseRequest, HttpServletRequest request){
		String sNoticeGmId = request.getParameter("noticeId");
		int noticeGmId = Integer.parseInt(sNoticeGmId);
		long fNoticeId = noticeManager.findOfGmNoticeId(noticeGmId);
		if(fNoticeId == 0){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}else{
			String noticeParams = request.getParameter("noticeParams");
			String startTime = request.getParameter("startTime");
			String vialdTime = request.getParameter("vialdTime");
			String sRepate = request.getParameter("repate");				
			String []params = noticeParams.split("&");
			int repate = Integer.parseInt(sRepate);
			messageService.message_notice_Modfiy(""+fNoticeId, params, startTime, vialdTime, repate);
		}
		return Result.valueOfSuccess();
	}
	
	private Object deleteNotice(Request baseRequest, HttpServletRequest request){
		String sNoticeGmId = request.getParameter("noticeId");
		int noticeGmId = Integer.parseInt(sNoticeGmId);
		long fNoticeId = noticeManager.findOfGmNoticeId(noticeGmId);
		if(fNoticeId == 0){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}else{
			messageService.message_notice_Delete(""+fNoticeId);
		}
		return Result.valueOfSuccess();
	}
	
	
}
