package com.palmjoys.yf1b.act.notice.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.notice.entity.NoticeEntity;
import com.palmjoys.yf1b.act.notice.manager.NoticeManager;
import com.palmjoys.yf1b.act.notice.model.NoticeAttrib;
import com.palmjoys.yf1b.act.notice.model.NoticeGmVo;
import com.palmjoys.yf1b.act.notice.model.NoticeParamAttrib;
import com.palmjoys.yf1b.act.notice.service.NoticeService;

@Component
@ConsoleBean
public class NoticeCommand {
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private NoticeManager noticeManager;
	
	
	/**
	 * 获取公告列表
	 * */
	@ConsoleCommand(name = "gm_notice_list", description = "获取公告列表")
	public Object gm_notice_list(){
		List<NoticeGmVo> retNoticeData = new ArrayList<NoticeGmVo>();
		List<NoticeAttrib> retList = noticeManager.getNoticeList(1);
		for(NoticeAttrib noticeAttrib : retList){
			NoticeEntity entry = noticeManager.load(Long.valueOf(noticeAttrib.msgId));
			if(null != entry){
				NoticeGmVo notice = new NoticeGmVo();
				notice.msgId = noticeAttrib.msgId;
				notice.startTime = String.valueOf(entry.getStartTime());
				notice.endTime = String.valueOf(entry.getEndTime());
				notice.intervalTime = entry.getIntervalTime();
				notice.noticeTemplateId = entry.getNoticeTemplateId();
				notice.content = "";
				List<NoticeParamAttrib> params = noticeAttrib.contents;
				for(NoticeParamAttrib paramAttrib : params){
					if(paramAttrib.type == 2){
						notice.content += paramAttrib.content;
					}
				}
				retNoticeData.add(notice);
			}			
		}
		return Result.valueOfSuccess(retNoticeData);
	}
	
	/**
	 * 查询指定公告
	 * noticeId 公告Id
	 * */
	@ConsoleCommand(name = "gm_notice_query", description = "查询指定公告")
	public Object gm_notice_query(String noticeId){
		return noticeService.notice_query_content(noticeId);
	}
	
	/**
	 * 修改公告
	 * noticeId 公告Id
	 * params 公告变参内容
	 * startTime 公告生效时间(毫秒时间戳)
	 * vialdTime 公告有效时长(秒)
	 * repate 公告重复频率时间(秒,0=只播一次)
	 * */
	@ConsoleCommand(name = "gm_message_notice_modfiy", description = "修改公告")
	public Object gm_message_notice_modfiy(String noticeId, String []params, String startTime, 
			String vialdTime, int repate){
		return noticeService.notice_modfiy(noticeId, params, startTime, vialdTime, repate);
	}
	
	/**
	 * 删除公告
	 * noticeId 公告Id
	 * */
	@ConsoleCommand(name = "gm_message_notice_delete", description = "删除公告")
	public Object gm_message_notice_delete(String noticeId){
		return noticeService.notice_delete(noticeId);
	}
	
	/**
	 * 添加公告
	 * templateId 公告模板Id
	 * params 公告变参内容
	 * startTime 公告生效时间(毫秒时间戳)
	 * vialdTime 公告有效时长(秒)
	 * repate 公告重复频率时间(秒,0=只播一次)
	 * */
	@ConsoleCommand(name = "gm_message_notice_new", description = "添加公告")
	public Object gm_message_notice_new(int templateId, String []params, String startTime, 
			String vialdTime, int repate){
		return noticeService.notice_new(templateId, 0, params, startTime, vialdTime, repate);
	}
}
