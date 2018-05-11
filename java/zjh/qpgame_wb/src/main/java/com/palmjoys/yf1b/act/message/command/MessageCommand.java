package com.palmjoys.yf1b.act.message.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.message.entity.NoticeEntity;
import com.palmjoys.yf1b.act.message.manager.NoticeManager;
import com.palmjoys.yf1b.act.message.model.NoticeAttrib;
import com.palmjoys.yf1b.act.message.model.NoticeGmVo;
import com.palmjoys.yf1b.act.message.model.NoticeParamAttrib;
import com.palmjoys.yf1b.act.message.service.MessageService;

@Component
@ConsoleBean
public class MessageCommand {
	@Autowired
	private MessageService messageService;
	@Autowired
	private NoticeManager noticeManager;
	
	
	/**
	 * 获取公告列表
	 * */
	@ConsoleCommand(name = "gm_Message_Notice_List", description = "获取公告列表")
	public Object gm_Message_Notice_List(){
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
	@ConsoleCommand(name = "gm_Message_Notice_Query", description = "查询指定公告")
	public Object gm_Message_Notice_Query(String noticeId){
		return messageService.message_notice_Content(noticeId);
	}
	
	/**
	 * 修改公告
	 * noticeId 公告Id
	 * params 公告变参内容
	 * startTime 公告生效时间(毫秒时间戳)
	 * vialdTime 公告有效时长(秒)
	 * repate 公告重复频率时间(秒,0=只播一次)
	 * */
	@ConsoleCommand(name = "gm_Message_Notice_Modfiy", description = "修改公告")
	public Object gm_Message_Notice_Modfiy(String noticeId, String []params, String startTime, 
			String vialdTime, int repate){
		return messageService.message_notice_Modfiy(noticeId, params, startTime, vialdTime, repate);
	}
	
	/**
	 * 删除公告
	 * noticeId 公告Id
	 * */
	@ConsoleCommand(name = "gm_Message_Notice_Delete", description = "删除公告")
	public Object gm_Message_Notice_Delete(String noticeId){
		return messageService.message_notice_Delete(noticeId);
	}
	
	/**
	 * 添加公告
	 * templateId 公告模板Id
	 * params 公告变参内容
	 * startTime 公告生效时间(毫秒时间戳)
	 * vialdTime 公告有效时长(秒)
	 * repate 公告重复频率时间(秒,0=只播一次)
	 * */
	@ConsoleCommand(name = "gm_Message_Notice_New", description = "添加公告")
	public Object gm_Message_Notice_New(int templateId, String []params, String startTime, 
			String vialdTime, int repate){
		return messageService.message_notice_New(templateId, 0, params, startTime, vialdTime, repate);
	}
}
