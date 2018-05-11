package com.palmjoys.yf1b.act.notice.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "消息模块")
public interface NoticeDefine {
	@SocketModule("消息模块")
	int NOTICE = 4;

	static final int NOTICE_COMMAND_BASE = NOTICE*100;
	static final int NOTICE_ERROR_BASE = (0-NOTICE)*1000;
	static final int NOTICE_COMMAND_BASE_NOTIFY = NOTICE*10000;
	
	//command id
	//推送消息(公告推送给在线所有玩家)
	int NOTICE_COMMAND_NOTIFY = NOTICE_COMMAND_BASE_NOTIFY + 1;
	
	//error id
	@SocketCode("公告不存在")
	int NOTICE_ERROR_UNEXIST = NOTICE_ERROR_BASE - 1;
	@SocketCode("不存在的公告模板")
	int NOTICE_ERROR_TEMPLATE_UNEXIST = NOTICE_ERROR_BASE - 2;
	@SocketCode("公告模板参数错误")
	int NOTICE_ERROR_TEMPLATE_PARAM = NOTICE_ERROR_BASE - 3;
}
