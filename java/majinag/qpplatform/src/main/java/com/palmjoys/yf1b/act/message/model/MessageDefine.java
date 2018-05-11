package com.palmjoys.yf1b.act.message.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "消息模块")
public interface MessageDefine {
	@SocketModule("消息模块")
	int MESSAGE = 3;

	static final int MESSAGE_COMMAND_BASE = MESSAGE*100;
	static final int MESSAGE_ERROR_BASE = (0-MESSAGE)*1000;
	static final int MESSAGE_COMMAND_BASE_NOTIFY = MESSAGE*10000;
	
	//command id
	//推送消息(公告推送给在线所有玩家)
	int MESSAGE_COMMAND_NOTIFY = MESSAGE_COMMAND_BASE_NOTIFY + 1;
	
	//error id
	@SocketCode("公告不存在")
	int MESSAGE_ERROR_UNEXIST = MESSAGE_ERROR_BASE - 1;
	@SocketCode("不存在的公告模板")
	int MESSAGE_ERROR_TEMPLATE_UNEXIST = MESSAGE_ERROR_BASE - 2;
	@SocketCode("公告模板参数错误")
	int MESSAGE_ERROR_TEMPLATE_PARAM = MESSAGE_ERROR_BASE - 3;
}
