package com.palmjoys.yf1b.act.notice.model;

import java.util.ArrayList;
import java.util.List;
import com.palmjoys.yf1b.act.notice.entity.NoticeEntity;

public class NoticeAttrib {
	//公告Id
	public String msgId;
	//公告内容
	public List<NoticeParamAttrib> contents;
	
	public static NoticeAttrib valueOf(NoticeEntity entry){
		NoticeAttrib retVo = new NoticeAttrib();
		retVo.msgId = String.valueOf(entry.getNoticeId());
		retVo.contents = new ArrayList<NoticeParamAttrib>();
		retVo.contents.addAll(entry.getContentList());
		
		return retVo;
	}
}
