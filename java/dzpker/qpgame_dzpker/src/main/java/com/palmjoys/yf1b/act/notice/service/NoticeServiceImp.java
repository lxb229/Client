package com.palmjoys.yf1b.act.notice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.model.Result;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.notice.entity.NoticeEntity;
import com.palmjoys.yf1b.act.notice.manager.NoticeManager;
import com.palmjoys.yf1b.act.notice.model.NoticeAttrib;
import com.palmjoys.yf1b.act.notice.model.NoticeDefine;
import com.palmjoys.yf1b.act.notice.model.NoticeParamAttrib;
import com.palmjoys.yf1b.act.notice.resource.NoticeTemplateConfig;

@Service
public class NoticeServiceImp implements NoticeService{
	@Autowired
	private NoticeManager noticeManager;
	@Static
	private Storage<Integer, NoticeTemplateConfig> templateCfgs;
	
	
	@Override
	public Object notice_List() {
		List<NoticeAttrib> retList = noticeManager.getNoticeList(0);
		return Result.valueOfSuccess(retList);
	}

	@Override
	public Object notice_query_content(String noticeId) {
		NoticeEntity entry = noticeManager.load(Long.valueOf(noticeId));
		if(null == entry){
			return Result.valueOfError(NoticeDefine.NOTICE_ERROR_UNEXIST);
		}
		NoticeAttrib retVo = NoticeAttrib.valueOf(entry);
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object notice_modfiy(String noticeId, String []params, 
			String startTime, String vialdTime, int repate) {
		
		int paramLen = 0;
		if(null == params){
			return Result.valueOfError(NoticeDefine.NOTICE_ERROR_TEMPLATE_PARAM);
		}				
		paramLen = params.length;
		
		long sTime = Long.valueOf(startTime);
		if(sTime == 0){
			sTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		}
		long eTime = sTime + (Long.valueOf(vialdTime).longValue())*1000;
		
		noticeManager.lock();
		int errId = 0;
		try{
			while(true){
				NoticeEntity entry = noticeManager.load(Long.valueOf(noticeId));
				if(null == entry){
					errId = NoticeDefine.NOTICE_ERROR_UNEXIST;
					break;
				}
				
				entry.setStartTime(sTime);
				entry.setEndTime(eTime);
				entry.setIntervalTime(repate);
				List<NoticeParamAttrib> contentParams = entry.getContentList();
				int paramIndex = 0;
				for(NoticeParamAttrib param : contentParams){
					if(param.type == 2){
						if(paramIndex < paramLen){
							param.content = params[paramIndex];						
						}
						paramIndex++;
					}
				}
				entry.setContentList(contentParams);
				
				break;
			}
		}finally{
			noticeManager.unLock();
		}
		
		if(errId < 0){
			return Result.valueOfError(errId);
		}
		
		return notice_List();
	}

	@Override
	public Object notice_delete(String noticeId) {
		noticeManager.lock();
		int errId = 0;
		try{
			while(true){
				NoticeEntity entry = noticeManager.load(Long.valueOf(noticeId));
				if(null == entry){
					errId = NoticeDefine.NOTICE_ERROR_UNEXIST;
					break;
				}
				noticeManager.remove(entry.getId());
				break;
			}
		}finally{
			noticeManager.unLock();
		}
		if(errId < 0){
			return Result.valueOfError(errId);
		}
		
		return notice_List();
	}

	@Override
	public Object notice_new(int templateId, int exNoticeId, String []params, String startTime, 
			String vialdTime, int repate) {
		
		int paramLen = 0;
		if(null == params){
			return Result.valueOfError(NoticeDefine.NOTICE_ERROR_TEMPLATE_PARAM);
		}
		NoticeTemplateConfig theCfg = templateCfgs.get(templateId, false);
		if(null == theCfg){
			return Result.valueOfError(NoticeDefine.NOTICE_ERROR_TEMPLATE_PARAM);
		}		
		paramLen = params.length;
		long sTime = Long.valueOf(startTime);
		if(sTime == 0){
			sTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		}
		long eTime = sTime + (Long.valueOf(vialdTime).longValue())*1000;
		
		noticeManager.lock();
		try{			
			NoticeEntity entry = noticeManager.loadOrCreate();			
			entry.setNoticeTemplateId(templateId);
			entry.setStartTime(sTime);
			entry.setEndTime(eTime);
			entry.setIntervalTime(repate);
			entry.setNextTime(sTime);
			entry.setNotice_gm_Id(exNoticeId); 
			
			List<NoticeParamAttrib> contentParams = entry.getContentList();			
			List<NoticeParamAttrib> cfgParams = theCfg.getStyle();
			int paramIndex = 0;
			for(NoticeParamAttrib param : cfgParams){
				NoticeParamAttrib theParam = new NoticeParamAttrib();
				if(param.type == 0){
					//不变参数
					theParam.type = 0;
					theParam.content = param.content;
					theParam.color.addAll(param.color);
					
					contentParams.add(theParam);
				}else if(param.type == 2){
					theParam.type = 2;
					
					if(paramIndex < paramLen){
						theParam.content = params[paramIndex];
					}
					paramIndex++;
					theParam.color.addAll(param.color);
					
					contentParams.add(theParam);
				}
			}
			
			entry.setContentList(contentParams);
		}finally{
			noticeManager.unLock();
		}
		return notice_List();
	}

	@Override
	public Object notice_new2(int templateId, int exNoticeId, String[] params) {
		int paramLen = 0;
		if(null == params){
			return Result.valueOfError(NoticeDefine.NOTICE_ERROR_TEMPLATE_PARAM);
		}
		NoticeTemplateConfig theCfg = templateCfgs.get(templateId, false);
		if(null == theCfg){
			return Result.valueOfError(NoticeDefine.NOTICE_ERROR_TEMPLATE_UNEXIST);
		}		
		paramLen = params.length;
		
		long sTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		long vaildTime = theCfg.getUnvalidTime()*1000;
		int repeat = theCfg.getRepeat();
		long eTime = sTime + vaildTime;
			
		noticeManager.lock();
		try{
			NoticeEntity entry = noticeManager.loadOrCreate();			
			entry.setNoticeTemplateId(templateId);
			entry.setStartTime(sTime);
			entry.setEndTime(eTime);
			entry.setIntervalTime(repeat);
			entry.setNextTime(sTime);
			entry.setNotice_gm_Id(exNoticeId); 
			
			List<NoticeParamAttrib> contentParams = entry.getContentList();
			List<NoticeParamAttrib> cfgParams = theCfg.getStyle();
			int paramIndex = 0;
			
			for(NoticeParamAttrib param : cfgParams){
				NoticeParamAttrib theParam = new NoticeParamAttrib();
				if(param.type == 0){
					//不变参数
					theParam.type = 0;
					theParam.content = param.content;
					theParam.color.addAll(param.color);
					
					contentParams.add(theParam);
				}else if(param.type == 2){
					theParam.type = 2;
					
					if(paramIndex < paramLen){
						theParam.content = params[paramIndex];
					}
					paramIndex++;					
					theParam.color.addAll(param.color);
					
					contentParams.add(theParam);
				}
			}
			entry.setContentList(contentParams);
		}finally{
			noticeManager.unLock();
		}
		
		return notice_List();
	}
}
