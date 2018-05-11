package com.palmjoys.yf1b.act.notice.facade;

import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.notice.model.NoticeDefine;

@NetworkFacade
public interface NoticeFacade {	
	
	@NetworkApi(value = NoticeDefine.NOTICE_COMMAND_NOTIFY,
			desc="推送消息(公告数据)")
	Object message_notice_notify();
}
